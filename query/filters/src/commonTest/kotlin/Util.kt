import io.github.briangits.persistence.query.filters.FilterBlock
import io.github.briangits.persistence.query.filters.Filters
import io.github.briangits.persistence.query.filters.predicates.AllOf

fun filterTest(block: UserProperties.() -> Unit) =
    UserProperties.block()

fun createFilters(block: FilterBlock<User, UserProperties>): AllOf<User> {
    val filters = Filters<User>()

    context(filters) { block(UserProperties) }

    return AllOf(predicates = filters.build())
}