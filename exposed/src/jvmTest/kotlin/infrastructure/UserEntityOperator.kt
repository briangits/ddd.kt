package infrastructure

import domain.PersonName
import domain.User
import domain.UserProperties
import io.github.briangits.persistence.exposed.repository.EntityOperator
import io.github.briangits.persistence.query.properties.explode

object UserEntityOperator : UserProperties(), EntityOperator<User, UserProperties> {
    override val properties = properties { UserProperties() }

    override val relations = relations {
        id by Users.id
        explode(name) {
            first by Users.firstName
            last by Users.lastName
        }
        email by Users.email
        age by Users.age
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
