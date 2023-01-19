package com.example

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import javax.persistence.Cacheable
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@Cacheable
class Fruit : PanacheEntity() {
  @Column(length = 40, unique = true)
  lateinit var name: String

  companion object: PanacheCompanion<Fruit>
}