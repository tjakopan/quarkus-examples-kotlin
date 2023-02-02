package com.example

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Entity

@Entity
class Price : PanacheEntity() {
  var value: Double = 0.0

  companion object : PanacheCompanion<Price>
}