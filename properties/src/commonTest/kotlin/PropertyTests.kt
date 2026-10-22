import io.github.briangits.ddd.query.properties.property.Property
import io.github.briangits.ddd.query.properties.property.child
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class PropertyTests {
    @Test
    fun `retrieving property value`() {
        data class User(val id: Long, val name: String)
        
        val user = User(1L, "John")
        val property = Property(User::id)
        
        assertEquals(1L, property.get(user))
    }

    @Test
    fun `retrieving nested property value`() {
        data class Address(val city: String)
        data class User(val address: Address)
        
        val user = User(Address("New York"))
        val cityProperty = Property(User::address).child(Address::city)
        
        assertEquals("New York", cityProperty.get(user))
    }

    @Test
    fun `properties with same path are equal`() {
        data class User(val id: Long, val name: String)
        
        val property1 = Property(User::id)
        val property2 = Property(User::id)
        
        assertEquals(property1, property2)
    }

    @Test
    fun `properties with same path have same hash code`() {
        data class User(val id: Long, val name: String)
        
        val property1 = Property(User::id)
        val property2 = Property(User::id)
        
        assertEquals(property1.hashCode(), property2.hashCode())
    }

    @Test
    fun `properties with different paths are not equal`() {
        data class User(val id: Long, val name: String)
        
        val property1 = Property(User::id)
        val property2 = Property(User::name)
        
        assertNotEquals<Any>(property1, property2)
    }
}
