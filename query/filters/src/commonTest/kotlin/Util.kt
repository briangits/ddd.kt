import io.github.briangits.persistence.query.filters.FilterBlock
import io.github.briangits.persistence.query.filters.Filters
import io.github.briangits.persistence.query.filters.operators.AllOf

fun filterTest(block: UserProperties.() -> Unit) =
    UserProperties().block()

fun createFilters(block: FilterBlock<User>): AllOf<User> =
    Filters<User>().apply(block).build()