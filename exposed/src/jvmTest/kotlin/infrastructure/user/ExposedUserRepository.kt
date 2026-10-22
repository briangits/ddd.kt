package infrastructure.user

import domain.user.User
import domain.user.UserProperties
import domain.user.UserRepository
import io.github.briangits.ddd.exposed.repository.ExposedRepository
import io.github.briangits.ddd.exposed.transaction.TransactionControl

class ExposedUserRepository(
    transaction: TransactionControl
) : UserRepository, ExposedRepository<User, UserProperties>(
    transaction = transaction,
    operator = UserEntityOperator
)
