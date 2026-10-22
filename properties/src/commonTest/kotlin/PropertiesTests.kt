import io.github.briangits.ddd.query.properties.Properties
import kotlin.test.Test
import kotlin.test.assertEquals

class PropertiesTests {
    @Test
    fun `creating properties through delegation`() {
        data class User(val id: Long, val name: String)
        
        class UserProperties : Properties<User>() {
            val id by User::id
            val name by User::name
        }
        
        val props = UserProperties()
        val user = User(1L, "John")
        
        assertEquals(1L, props.id.get(user))
        assertEquals("John", props.name.get(user))
    }

    @Test
    fun `property paths contents`() {
        data class User(val id: Long)
        
        class UserProperties : Properties<User>() {
            val id by User::id
        }
        
        val props = UserProperties()
        
        assertEquals(1, props.id.path.size)
        assertEquals(User::id, props.id.path[0])
    }

    @Test
    fun `reusing a property with multiple instances`() {
        data class User(val id: Long)
        
        class UserProperties : Properties<User>() {
            val id by User::id
        }
        
        val props = UserProperties()
        val user1 = User(1L)
        val user2 = User(2L)
        
        assertEquals(1L, props.id.get(user1))
        assertEquals(2L, props.id.get(user2))
    }
}
