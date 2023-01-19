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
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.control.ActivateRequestContext
import javax.persistence.NonUniqueResultException

@ApplicationScoped
class ReactiveIdentityProvider : IdentityProvider<UsernamePasswordAuthenticationRequest> {
  override fun getRequestType(): Class<UsernamePasswordAuthenticationRequest> =
    UsernamePasswordAuthenticationRequest::class.java

  @ActivateRequestContext
  override fun authenticate(
    request: UsernamePasswordAuthenticationRequest,
    context: AuthenticationRequestContext
  ): Uni<SecurityIdentity> =
    getSingleUser(request.username)
      .onItem().ifNull().failWith { AuthenticationFailedException() }
      .flatMap { user ->
        if (!BcryptUtil.matches(String(request.password.password), user!!.password))
          Uni.createFrom().failure { AuthenticationFailedException() }
        else Uni.createFrom().item {
          QuarkusSecurityIdentity.builder().apply {
            setPrincipal(QuarkusPrincipal(request.username))
            addCredential(request.password)
            addRole(user.role)
          }
            .build()
        }
      }

  private fun getSingleUser(username: String): Uni<User?> =
    User.findByUsername(username)
      .onFailure(NonUniqueResultException::class.java).transform { e -> AuthenticationFailedException(e) }
}