package infrastructure

import domain.User
import domain.UserProperties
import domain.UserRepository
import io.github.briangits.persistence.exposed.repository.ExposedRepository
import io.github.briangits.persistence.exposed.transaction.TransactionControl

class ExposedUserRepository(
    transaction: TransactionControl
) : UserRepository, ExposedRepository<Users, User, UserProperties>(
    transaction = transaction,
    table = Users,
    operator = UserEntityOperator
)
