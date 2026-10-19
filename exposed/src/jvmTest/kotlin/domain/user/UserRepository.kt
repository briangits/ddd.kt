package domain.user

import io.github.briangits.persistence.repository.Repository

interface UserRepository : Repository<User, UserProperties>
