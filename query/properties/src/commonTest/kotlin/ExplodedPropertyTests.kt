
import io.github.briangits.persistence.query.properties.ExplodedProperty
import kotlin.test.Test
import kotlin.test.assertEquals

class ExplodedPropertyTests {
    @Test
    fun `creating properties through delegation`() {
        data class Address(val city: String, val country: String)
        data class User(val address: Address)
        
        class UserProperties : ExplodedProperty<User, Address>(User::address) {
            val city by Address::city
            val country by Address::country
        }
        
        val props = UserProperties()
        val user = User(Address("New York", "USA"))
        
        assertEquals("New York", props.city.get(user))
        assertEquals("USA", props.country.get(user))
    }

    @Test
    fun `exploded property path includes parent property`() {
        data class Address(val city: String)
        data class User(val address: Address)
        
        class UserProperties : ExplodedProperty<User, Address>(User::address) {
            val city by Address::city
        }
        
        val props = UserProperties()
        
        assertEquals(2, props.city.path.size)
        assertEquals(User::address, props.city.path[0])
        assertEquals(Address::city, props.city.path[1])
    }

    @Test
    fun `retrieving property value`() {
        data class Address(val city: String)
        data class User(val address: Address)

        class NestedProperties : ExplodedProperty<User, Address>(User::address) {
            val city by Address::city
        }
        
        val nested = NestedProperties()
        val user = User(Address("Boston"))
        
        assertEquals("Boston", nested.city.get(user))
    }
}
