package domain.user

import io.github.briangits.ddd.query.properties.ExplodedProperty
import io.github.briangits.ddd.repository.EntityProperties
import kotlin.uuid.Uuid

object UserProperties : EntityProperties<User, Uuid>() {
    object name : ExplodedProperty<User, PersonName>(User::name) {
        val first by PersonName::first
        val last by PersonName::last
    }
    val email by User::email
    val age by User::age
}
