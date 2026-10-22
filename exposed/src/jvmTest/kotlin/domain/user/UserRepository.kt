package domain.user

import io.github.briangits.ddd.repository.Repository

interface UserRepository : Repository<User, UserProperties>
