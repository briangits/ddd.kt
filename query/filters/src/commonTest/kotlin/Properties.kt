
import io.github.briangits.persistence.query.properties.Properties
import io.github.briangits.persistence.query.properties.explode

data class PersonName(val first: String, val last: String)

data class User(
    val id: Long,
    val name: PersonName,
    val email: String?,
    val age: Int
)

object UserProperties : Properties<User>() {
    val id by User::id
    object name : explode<User, PersonName>(User::name) {
        val first by PersonName::first
        val last by PersonName::last
    }
    val email by User::email
    val age by User::age
}