package infrastructure.user

import domain.user.PersonName
import domain.user.User
import domain.user.UserProperties
import domain.user.UserProperties.name
import io.github.briangits.persistence.exposed.relations.dsl.by
import io.github.briangits.persistence.exposed.repository.EntityOperator
import io.github.briangits.persistence.query.properties.explode

object UserEntityOperator : EntityOperator<User, UserProperties> {
    override val properties = properties { UserProperties }

    override val relations = relations(Users) {
        id by it.id
        explode(name) {
            first by it.firstName
            last by it.lastName
        }
        email by it.email
        age by it.age
    }

    override val fromDB = fromDB {
        User(
            id = it[id],
            name = explode(name) {
                PersonName(
                    first = it[first],
                    last = it[last]
                )
            },
            email = it[email],
            age = it[age]
        )
    }
}
