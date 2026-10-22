package repository

import closeDB
import createDB
import domain.user.PersonName
import domain.user.User
import infrastructure.user.ExposedUserRepository
import io.github.briangits.ddd.query.filters.eq
import io.github.briangits.ddd.repository.save
import org.junit.Test
import runInTransaction
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.Uuid

class ExistsTests {
    @Test
    fun `exists returns true for matching entity`() =
        runInTransaction(createDB()) { control, db ->
            val repository = ExposedUserRepository(control)

            val user = repository.save {
                User(
                    name = PersonName("User", "1"),
                    email = null,
                    age = 20
                )
            }

            assertTrue(
                repository.exists {
                    id eq user.id
                }
            )

            closeDB(db)
        }

    @Test
    fun `exists returns false for non matching entity`() =
        runInTransaction(createDB()) { control, db ->
            val repository = ExposedUserRepository(control)

            assertFalse(
                repository.exists {
                    id eq Uuid.random()
                }
            )

            closeDB(db)
        }
}