<h1 align="center">persistence.kt</h1>

<p align="center">A persistence abstraction layer for Kotlin Multiplatform.</p>

[![Kotlin](https://img.shields.io/badge/kotlin-2.4.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
![Maven Central](https://img.shields.io/maven-central/v/io.github.briangits.persistence/core?color=blue)
![CI](https://github.com/briangits/persistence.kt/actions/workflows/ci.yaml/badge.svg)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)


`persistence.kt` provides a unified, DDD-oriented API for querying and persisting data using entities.

---

## Quick Start

### 1. Add the dependency

```kotlin
dependencies {
    implementation("io.github.briangits.persistence:core:<version>")
}
```

### 2. Define your domain

```kotlin
import kotlin.uuid.Uuid
import io.github.briangits.persistence.repository.Entity

data class User(
    override val id: Uuid = Uuid.random(),
    val name: String,
    val email: String?
) : Entity<Uuid>
```

Define properties to be used in queries when interacting with `User`:

```kotlin
import io.github.briangits.persistence.repository.EntityProperties

object UserProperties : EntityProperties<User, Uuid>() {
    val name by User::name
    val email by User::email
}
```

Then define a repository:

```kotlin
import io.github.briangits.persistence.repository.Repository

interface UserRepository : Repository<User, UserProperties>
```

### 3. Saving and querying entities

```kotlin
import io.github.briangits.persistence.Persistence
import io.github.briangits.persistence.repository.get

suspend fun registerUser(persistence: Persistence) = persistence.transaction {
    val users = get<UserRepository>()

    users.save(User(name = "Jane Doe", email = "jane@company.com"))

    users.findAll {
        name startsWith "Jane"
        email endsWith "@company.com"
    }
}
```
---

## Queries

### Filters

Filters use the defined properties to provide a type safe DSL filtering entities.
Multiple conditions in the top level block are combined with `AND`.

| Operation          | Syntax                                                               |
|:-------------------|:---------------------------------------------------------------------|
| Equality           | `prop eq value`, `prop neq value`                                    |
| Comparison         | `prop gt value`, `prop gte value`, `prop lt value`, `prop lte value` |
| Range              | `prop.between(start, end)`, `prop.notBetween(start, end)`             |
| Text               | `prop contains text`, `prop startsWith text`, `prop endsWith text`   |
| Regular expression | `prop matches pattern`                                               |
| Membership         | `prop includedIn values`, `prop notIn values`                         |
| Nullability        | `prop.isNull()`, `prop.isNotNull()`                                  |

Comparing a nullable property with `null` using `eq` or `neq` produces a null check.
Use `allOf`, `oneOf`, and `not` to group conditions:

```kotlin
users.findAll {
    oneOf {
        name startsWith "Jane"
        name startsWith "John"
    }
    not { email.isNull() }
}
```

### Nested properties

For nested values, define an `explode` object in your properties class.
For example, if `User.name` is a `PersonName` with `first` and `last` properties:

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

Using the exploded object:

```kotlin
users.find {
    name.first eq "Jane"
}
```

Or using the `explode` helper:

```kotlin
users.find {
    explode(name) {
        first eq "Jane"
        last eq "Doe"
    }
}
```

### Pagination

Request a page with a size and offset. The result contains `items`, `total`,
and the pagination metadata.

```kotlin
import io.github.briangits.persistence.repository.pagination.Pagination

val page = users.findAll(page = Pagination(size = 20, offset = 0)) {
    email endsWith "@company.com"
}
```

Or use the convenience extension:

```kotlin
val page = users.findAll(limit = 20, offset = 0) {
    email endsWith "@company.com"
}
```

---

## Unit of Work

Use `UnitOfWork` to group repository operations and register hooks around commit.

```kotlin
dependencies {
    implementation("io.github.briangits.persistence:uow:<version>")
}
```

Define a unit of work with the repositories an operation needs:

```kotlin
import io.github.briangits.persistence.repository.get
import io.github.briangits.persistence.transaction.Transaction
import io.github.briangits.persistence.uow.UnitOfWork

class UserUoW(transaction: Transaction) : UnitOfWork<UserUoW>(transaction) {
    val users = get<UserRepository>()
}
```

Create a factory for your unit of work:

```kotlin
import io.github.briangits.persistence.Persistence
import io.github.briangits.persistence.uow.UoWFactory

val userUoW = UoWFactory(persistence) { transaction -> UserUoW(transaction) }
```

`run` executes the block and commits. `execute` lets you perform operations before
committing explicitly. Both roll back when their block fails.

```kotlin
suspend fun saveUser(user: User) {
    userUoW {
        users.save(user)

        beforeCommit { check(users.exists { id eq user.id }) }
        afterCommit { println("Saved user ${user.id}") }
    }
}
```

---

## Adapters

The core API and unit-of-work module support Kotlin Multiplatform. Adapters provide
the database-specific repository and transaction implementations.

- **[Exposed](docs/Exposed.md)** — JDBC persistence on JVM using JetBrains Exposed.

## License

[Apache-2.0](LICENSE)
