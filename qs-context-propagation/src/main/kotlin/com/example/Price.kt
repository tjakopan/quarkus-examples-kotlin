package com.example

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import javax.persistence.Entity

@Entity
class Price : PanacheEntity() {
  var value: Double = 0.0

  companion object: PanacheCompanion<Price>
}