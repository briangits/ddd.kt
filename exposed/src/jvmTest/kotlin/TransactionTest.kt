
import domain.user.PersonName
import domain.user.User
import domain.user.UserRepository
import infrastructure.user.ExposedUserRepository
import io.github.briangits.ddd.exposed.transaction.ExposedTransaction
import io.github.briangits.ddd.query.filters.eq
import io.github.briangits.ddd.repository.get
import io.github.briangits.ddd.repository.save
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TransactionTest {
    private fun createTransaction(db: Database): ExposedTransaction =
        ExposedTransaction(
            control = createTransactionControl(db),
            registry = mapOf(
                UserRepository::class to ::ExposedUserRepository
            )
        )

    @Test
    fun `data is persisted for committed transaction`() = runTest {
        val db = createDB()
        val transaction = createTransaction(db)

        val repository = transaction.get<UserRepository>()
        val user = repository.save {
            User(
                name = PersonName("Jane", "Doe"),
                email = "janedoe@example.com",
                age = 25
            )
        }

        transaction.commit()

        runInTransaction(db) { control, _ ->
            val repository = ExposedUserRepository(control)

            val found = repository.find { id eq user.id }

            assertEquals(user, found)
        }
    }

    @Test
    fun `data is not persisted for rolled-back transactions`() = runTest {
        val db = createDB()
        val transaction = createTransaction(db)

        val repository = transaction.get<UserRepository>()
        val user = repository.save {
            User(
                name = PersonName("Jane", "Doe"),
                email = "janedoe@example.com",
                age = 25
            )
        }

        transaction.rollback()

        runInTransaction(db) { control, _ ->
            val repository = ExposedUserRepository(control)

            assertNull(
                repository.find {
                     id eq user.id
                }
            )
        }
    }
}
