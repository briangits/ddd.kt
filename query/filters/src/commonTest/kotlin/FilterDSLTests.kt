
import UserProperties.name
import io.github.briangits.persistence.query.filters.operators.AllOf
import io.github.briangits.persistence.query.filters.operators.Contains
import io.github.briangits.persistence.query.filters.operators.Eq
import io.github.briangits.persistence.query.filters.operators.Gt
import io.github.briangits.persistence.query.filters.operators.IsNotNull
import io.github.briangits.persistence.query.filters.operators.NEq
import io.github.briangits.persistence.query.properties.explode
import kotlin.test.Test
import kotlin.test.assertEquals

class FilterDSLTests {
    @Test
    fun `creating filters`() = filterTest {
        val filters = createFilters {
            id eq 1L
            explode(name) {
                first neq "John"
                last contains "Doe"
            }
            email.isNotNull()
            age gt 20
        }

        val expected = AllOf(
            operators = listOf(
                Eq(id, 1L),
                NEq(name.first, "John"),
                Contains(name.last, "Doe"),
                IsNotNull(email),
                Gt(age, 20)
            )
        )

        assertEquals(expected, filters)
    }
}
