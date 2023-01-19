package com.example

import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.security.AuthenticationFailedException
import io.quarkus.security.identity.AuthenticationRequestContext
import io.quarkus.security.identity.IdentityProvider
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest
import io.quarkus.security.runtime.QuarkusPrincipal
import io.quarkus.security.runtime.QuarkusSecurityIdentity
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.asUni
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.*
import javax.annotation.PreDestroy
import javax.enterprise.context.control.ActivateRequestContext
import javax.inject.Singleton
import javax.persistence.NonUniqueResultException

@Singleton
class ReactiveIdentityProvider(@Suppress("CdiInjectionPointsInspection") vertx: Vertx) :
  IdentityProvider<UsernamePasswordAuthenticationRequest>, AutoCloseable {
  private val coroutineScope = CoroutineScope(vertx.dispatcher() + SupervisorJob())

  override fun getRequestType(): Class<UsernamePasswordAuthenticationRequest> =
    UsernamePasswordAuthenticationRequest::class.java

  @OptIn(ExperimentalCoroutinesApi::class)
  @ActivateRequestContext
  override fun authenticate(
    request: UsernamePasswordAuthenticationRequest,
    context: AuthenticationRequestContext
  ): Uni<SecurityIdentity> =
    coroutineScope.async { authenticate(request) }.asUni()


  private suspend fun authenticate(request: UsernamePasswordAuthenticationRequest): SecurityIdentity {
    val user = getSingleUser(request.username) ?: throw AuthenticationFailedException()
    if (!BcryptUtil.matches(String(request.password.password), user.password)) throw AuthenticationFailedException()
    return QuarkusSecurityIdentity.builder().apply {
      setPrincipal(QuarkusPrincipal(request.username))
      addCredential(request.password)
      addRole(user.role)
    }.build()
  }

  private suspend fun getSingleUser(username: String): User? =
    try {
      User.findByUsername(username)
    } catch (e: NonUniqueResultException) {
      throw AuthenticationFailedException(e)
    }

  @PreDestroy
  override fun close() {
    coroutineScope.cancel()
  }
}