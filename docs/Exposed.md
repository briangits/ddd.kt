# Implementing Persistence with Exposed

This guide explains how to implement the persistence API using [Exposed](https://www.jetbrains.com/help/exposed/home.html).

The Exposed adapter provides the database-specific implementations
for `Persistence`, `Transaction`, and `Repository`.

---

## Setup

Add the Exposed adapter to your project:

```kotlin
implementation("io.github.briangits.persistence:exposed:<version>")
```

You'll also need the appropriate Exposed JDBC and database driver dependencies.
See [Connecting to a Database](https://www.jetbrains.com/help/exposed/working-with-database.html#connecting-to-a-database) for a guide on setting this up.

---

## Configuring Persistence

Create an `ExposedPersistence` instance with an Exposed `Database`
and bind your repository implementations.

```kotlin
import io.github.briangits.persistence.Persistence
import io.github.briangits.persistence.exposed.ExposedPersistence
import org.jetbrains.exposed.v1.jdbc.Database

val database = Database.connect(
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
    driver = "org.h2.Driver"
)

val persistence: Persistence = ExposedPersistence(database) {
    bind<UserRepository>(::ExposedUserRepository)
}
```

The `bind` call tells the adapter which implementation should be used 
whenever `UserRepository` is requested.

---

## Define the Persistence Model

Domain models need to be mapped to the database's persistence models.

For example, given the following domain model:

```kotlin
import io.github.briangits.persistence.repository.Entity
import kotlin.uuid.Uuid

data class User(
    override val id: Uuid,
    val name: String,
    val email: String?
) : Entity<Uuid>
```

Define the corresponding Exposed table:

```kotlin
import org.jetbrains.exposed.v1.core.Table

object Users : Table("users") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val email = varchar("email", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}
```

The table is an Exposed-specific representation of the domain model
and does not need to be exposed to the domain layer.

---

## Implementing Repositories

To implement a repository, extend `ExposedRepository` and provide:

1. An `EntityOperator` for mapping database rows to domain entities.
2. Accept a transaction which the repository will be bound to.

### Create an Entity Operator

`EntityOperator` defines how an Exposed result row is converted into a domain entity 
and how domain properties map to database columns.

```kotlin
import io.github.briangits.persistence.exposed.repository.EntityOperator
import io.github.briangits.persistence.exposed.relations.dsl.by

object UserEntityOperator : EntityOperator<User, UserProperties> {
    override val properties = properties { UserProperties }

    override val relations = relations(Users) {
        id by it.id
        name by it.name
        email by it.email
    }

    override val fromDB = fromDB {
        User(
            id = it[id],
            name = it[name],
            email = it[email]
        )
    }
}
```

The `fromDB` function handles the database-to-domain mapping,
while the `relations` block associates domain properties with their corresponding
table columns.

---

### Implement the Repository

Now implement the repository interface defined by the domain layer:

```kotlin
import io.github.briangits.persistence.exposed.repository.ExposedRepository
import io.github.briangits.persistence.exposed.transaction.TransactionControl

class ExposedUserRepository(
    transaction: TransactionControl
) : UserRepository, ExposedRepository<User, UserProperties>(
    transaction = transaction,
    operator = UserEntityOperator
)
```

The `TransactionControl` is provided by the underlying `ExposedPersistence` implementation
when the repository is created.

---

## Using the Repository

Once the repository has been bound to `ExposedPersistence`,
it can then be resolved from a transaction:

```kotlin
suspend fun createUser(persistence: Persistence, user: User) {
    persistence.transaction {
        val users = get<UserRepository>()
        users.save(user)
    }
}
```

---

## Complete Example

Putting everything together:

```kotlin
import io.github.briangits.persistence.Persistence
import io.github.briangits.persistence.exposed.ExposedPersistence
import io.github.briangits.persistence.exposed.repository.EntityOperator
import io.github.briangits.persistence.exposed.repository.ExposedRepository
import io.github.briangits.persistence.exposed.transaction.TransactionControl
import io.github.briangits.persistence.exposed.relations.dsl.by
import io.github.briangits.persistence.repository.Entity
import io.github.briangits.persistence.repository.EntityProperties
import io.github.briangits.persistence.repository.Repository
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.uuid.Uuid

// Domain layer

data class User(
    override val id: Uuid,
    val name: String,
    val email: String?
) : Entity<Uuid>

object UserProperties : EntityProperties<User, Uuid>() {
    val name by User::name
    val email by User::email
}

interface UserRepository : Repository<User, UserProperties>

// Persistence model

object Users : Table("users") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val email = varchar("email", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}

// Entity mapping

object UserEntityOperator : EntityOperator<User, UserProperties> {
    override val properties = properties { UserProperties }

    override val relations = relations(Users) {
        id by it.id
        name by it.name
        email by it.email
    }

    override val fromDB = fromDB {
        User(
            id = it[id],
            name = it[name],
            email = it[email]
        )
    }
}

// Repository implementation

class ExposedUserRepository(
    transaction: TransactionControl
) : UserRepository, ExposedRepository<User, UserProperties>(
    transaction = transaction,
    operator = UserEntityOperator
)

// Persistence configuration

val database = Database.connect(
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
    driver = "org.h2.Driver"
)

val persistence: Persistence = ExposedPersistence(database) {
    bind<UserRepository>(::ExposedUserRepository)
}
```

---

## Nested Properties

For nested domain models, use `explode` in your properties and the `explode` helper in the entity operator.

Given a nested `PersonName`:

```kotlin
data class PersonName(
    val first: String,
    val last: String
)

data class User(
    override val id: Uuid,
    val name: PersonName,
    val email: String?
) : Entity<Uuid>
```

Define the properties with an exploded object:

```kotlin
import io.github.briangits.persistence.query.properties.explode

object UserProperties : EntityProperties<User, Uuid>() {
    object name : explode<User, PersonName>(User::name) {
        val first by PersonName::first
        val last by PersonName::last
    }
    val email by User::email
}
```

Define the table with flattened columns:

```kotlin
object Users : Table("users") {
    val id = uuid("id")
    val firstName = varchar("firstName", 255)
    val lastName = varchar("lastName", 255)
    val email = varchar("email", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}
```

Map the exploded properties in the entity operator:

```kotlin
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
            email = it[email]
        )
    }
}
```
