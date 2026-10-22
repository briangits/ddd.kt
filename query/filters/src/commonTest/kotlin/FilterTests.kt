import UserProperties.name
import io.github.briangits.ddd.query.filters.allOf
import io.github.briangits.ddd.query.filters.between
import io.github.briangits.ddd.query.filters.contains
import io.github.briangits.ddd.query.filters.endsWith
import io.github.briangits.ddd.query.filters.eq
import io.github.briangits.ddd.query.filters.gt
import io.github.briangits.ddd.query.filters.gte
import io.github.briangits.ddd.query.filters.includedIn
import io.github.briangits.ddd.query.filters.isNotNull
import io.github.briangits.ddd.query.filters.isNull
import io.github.briangits.ddd.query.filters.lt
import io.github.briangits.ddd.query.filters.lte
import io.github.briangits.ddd.query.filters.matches
import io.github.briangits.ddd.query.filters.neq
import io.github.briangits.ddd.query.filters.not
import io.github.briangits.ddd.query.filters.notBetween
import io.github.briangits.ddd.query.filters.notIn
import io.github.briangits.ddd.query.filters.oneOf
import io.github.briangits.ddd.query.filters.predicates.AllOf
import io.github.briangits.ddd.query.filters.predicates.Comparison
import io.github.briangits.ddd.query.filters.predicates.Equality
import io.github.briangits.ddd.query.filters.predicates.Membership
import io.github.briangits.ddd.query.filters.predicates.Not
import io.github.briangits.ddd.query.filters.predicates.Nullability
import io.github.briangits.ddd.query.filters.predicates.OneOf
import io.github.briangits.ddd.query.filters.predicates.Range
import io.github.briangits.ddd.query.filters.predicates.StringPredicate
import io.github.briangits.ddd.query.filters.predicates.operators.comparison.Gt
import io.github.briangits.ddd.query.filters.predicates.operators.comparison.Gte
import io.github.briangits.ddd.query.filters.predicates.operators.comparison.Lt
import io.github.briangits.ddd.query.filters.predicates.operators.comparison.Lte
import io.github.briangits.ddd.query.filters.predicates.operators.equality.Eq
import io.github.briangits.ddd.query.filters.predicates.operators.equality.NEq
import io.github.briangits.ddd.query.filters.predicates.operators.membership.In
import io.github.briangits.ddd.query.filters.predicates.operators.membership.NotIn
import io.github.briangits.ddd.query.filters.predicates.operators.nullability.IsNotNull
import io.github.briangits.ddd.query.filters.predicates.operators.nullability.IsNull
import io.github.briangits.ddd.query.filters.predicates.operators.range.Between
import io.github.briangits.ddd.query.filters.predicates.operators.range.NotBetween
import io.github.briangits.ddd.query.filters.predicates.operators.string.Contains
import io.github.briangits.ddd.query.filters.predicates.operators.string.EndsWith
import io.github.briangits.ddd.query.filters.predicates.operators.string.Matches
import io.github.briangits.ddd.query.filters.predicates.operators.string.StartsWith
import io.github.briangits.ddd.query.filters.startsWith
import io.github.briangits.ddd.query.properties.explode
import kotlin.test.Test
import kotlin.test.assertEquals

class FilterTests {
    @Test
    fun `creating filters with multiple operators`() = filterTest {
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
            predicates = listOf(
                Equality(id, 1L, Eq),
                Equality(name.first, "John", NEq),
                StringPredicate(name.last, "Doe", Contains),
                Nullability(email, IsNotNull),
                Comparison(age, 20, Gt)
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `equality operators`() = filterTest {
        val filters = createFilters {
            id eq 1L
            age neq 30
        }

        val expected = AllOf(
            predicates = listOf(
                Equality(id, 1L, Eq),
                Equality(age, 30, NEq)
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `comparison operators`() = filterTest {
        val filters = createFilters {
            age gt 18
            age gte 18
            age lt 65
            age lte 65
        }

        val expected = AllOf(
            predicates = listOf(
                Comparison(age, 18, Gt),
                Comparison(age, 18, Gte),
                Comparison(age, 65, Lt),
                Comparison(age, 65, Lte)
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `range operators`() = filterTest {
        val filters = createFilters {
            age.between(18, 65)
            age.notBetween(30, 50)
        }

        val expected = AllOf(
            predicates = listOf(
                Range(age, 18, 65, Between),
                Range(age, 30, 50, NotBetween)
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `string operators`() = filterTest {
        val filters = createFilters {
            explode(name) {
                first contains "John"
                last startsWith "Doe"
                email endsWith "@example.com"
            }
        }

        val expected = AllOf(
            predicates = listOf(
                StringPredicate(name.first, "John", Contains),
                StringPredicate(name.last, "Doe", StartsWith),
                StringPredicate(email, "@example.com", EndsWith)
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `string matches operator`() = filterTest {
        val filters = createFilters {
            email matches "^[a-z]+@example\\.com$"
        }

        val expected = AllOf(
            predicates = listOf(
                StringPredicate(email, "^[a-z]+@example\\.com$", Matches)
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `string matches with regex`() = filterTest {
        val filters = createFilters {
            email matches Regex("^[a-z]+@example\\.com$")
        }

        val expected = AllOf(
            predicates = listOf(
                StringPredicate(email, "^[a-z]+@example\\.com$", Matches)
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `membership operators`() = filterTest {
        val filters = createFilters {
            id includedIn listOf(1L, 2L, 3L)
            age notIn listOf(10, 20, 30)
        }

        val expected = AllOf(
            predicates = listOf(
                Membership(id, listOf(1L, 2L, 3L), In),
                Membership(age, listOf(10, 20, 30), NotIn)
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `nullability operators`() = filterTest {
        val filters = createFilters {
            email.isNull()
            id.isNotNull()
        }

        val expected = AllOf(
            predicates = listOf(
                Nullability(email, IsNull),
                Nullability(id, IsNotNull)
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `logical allOf operator`() = filterTest {
        val filters = createFilters {
            allOf {
                id eq 1L
                age gt 18
            }
        }

        val expected = AllOf(
            predicates = listOf(
                AllOf(
                    predicates = listOf(
                        Equality(id, 1L, Eq),
                        Comparison(age, 18, Gt)
                    )
                )
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `logical oneOf operator`() = filterTest {
        val filters = createFilters {
            oneOf {
                id eq 1L
                id eq 2L
            }
        }

        val expected = AllOf(
            predicates = listOf(
                OneOf(
                    predicates = listOf(
                        Equality(id, 1L, Eq),
                        Equality(id, 2L, Eq)
                    )
                )
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `logical not operator`() = filterTest {
        val filters = createFilters {
            not {
                id eq 1L
                age lt 18
            }
        }

        val expected = AllOf(
            predicates = listOf(
                Not(
                    AllOf(
                        predicates = listOf(
                            Equality(id, 1L, Eq),
                            Comparison(age, 18, Lt)
                        )
                    )
                )
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `nested logical operators`() = filterTest {
        val filters = createFilters {
            allOf {
                id eq 1L
                oneOf {
                    age gt 18
                    age lt 65
                }
            }
        }

        val expected = AllOf(
            predicates = listOf(
                AllOf(
                    predicates = listOf(
                        Equality(id, 1L, Eq),
                        OneOf(
                            predicates = listOf(
                                Comparison(age, 18, Gt),
                                Comparison(age, 65, Lt)
                            )
                        )
                    )
                )
            )
        )

        assertEquals(expected, filters)
    }

    @Test
    fun `empty filters`() = filterTest {
        val filters = createFilters { }

        val expected = AllOf<User>(predicates = emptyList())

        assertEquals(expected, filters)
    }
}
