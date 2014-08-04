package jpasearch.repository.query.builder;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.OrderBy;
import jpasearch.repository.query.OrderByDirection;
import jpasearch.repository.query.Path;

/**
 * @author speralta
 */
public class OrdersByBuilder<FROM> extends AbstractBuilder<SearchBuilder<FROM>> {

    private final Set<OrderBy<FROM, ?>> orders;

    public OrdersByBuilder(SearchBuilder<FROM> searchParameters) {
        super(searchParameters);
        orders = new LinkedHashSet<>();
    }

    public OrdersByBuilder(SearchBuilder<FROM> searchParameters, Set<OrderBy<FROM, ?>> orders) {
        super(searchParameters);
        this.orders = new LinkedHashSet<>(orders);
    }

    public <TO> OrderByBuilder<FROM, FROM, TO> by(SingularAttribute<? super FROM, TO> attribute) {
        return new OrderByBuilder<>(this, attribute);
    }

    public <TO> OrderByBuilder<FROM, FROM, TO> by(PluralAttribute<? super FROM, ?, TO> attribute) {
        return new OrderByBuilder<>(this, attribute);
    }

    public OrdersByBuilder<FROM> orderBy(OrderByDirection direction, String path, Class<FROM> from) {
        orders.add(new OrderBy<>(direction, from, path));
        return this;
    }

    public Set<OrderBy<FROM, ?>> getOrders() {
        return Collections.unmodifiableSet(orders);
    }

    <TO> OrdersByBuilder<FROM> asc(Path<FROM, TO> path) {
        orders.add(new OrderBy<FROM, TO>(OrderByDirection.ASC, path));
        return this;
    }

    <TO> OrdersByBuilder<FROM> desc(Path<FROM, TO> path) {
        orders.add(new OrderBy<FROM, TO>(OrderByDirection.DESC, path));
        return this;
    }
}
