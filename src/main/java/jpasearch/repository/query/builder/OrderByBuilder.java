package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;
import jpasearch.repository.query.SearchBuilder;

/**
 * @author speralta
 */
public class OrderByBuilder<F, FROM, TO> {

    private final OrdersByBuilder<F> builder;

    private final Path<F, TO> path;

    public OrderByBuilder(OrdersByBuilder<F> ordersByBuilder, SingularAttribute<? super F, TO> attribute) {
        this.builder = ordersByBuilder;
        this.path = new Path<F, TO>(attribute);
    }

    public OrderByBuilder(OrdersByBuilder<F> ordersByBuilder, PluralAttribute<? super F, ?, TO> attribute) {
        this.builder = ordersByBuilder;
        this.path = new Path<F, TO>(attribute);
    }

    private <E> OrderByBuilder(OrdersByBuilder<F> builder, OrderByBuilder<F, E, FROM> orderByBuilder, SingularAttribute<? super FROM, TO> attribute) {
        this.builder = builder;
        this.path = orderByBuilder.path.add(attribute);
    }

    private <E> OrderByBuilder(OrdersByBuilder<F> builder, OrderByBuilder<F, E, FROM> orderByBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        this.builder = builder;
        this.path = orderByBuilder.path.add(attribute);
    }

    public SearchBuilder<F> asc() {
        return builder.asc(path).toParent();
    }

    public SearchBuilder<F> desc() {
        return builder.desc(path).toParent();
    }

    public <E> OrderByBuilder<F, TO, E> and(SingularAttribute<? super TO, E> attribute) {
        return new OrderByBuilder<F, TO, E>(builder, this, attribute);
    }

    public <E> OrderByBuilder<F, TO, E> and(PluralAttribute<? super TO, ?, E> attribute) {
        return new OrderByBuilder<F, TO, E>(builder, this, attribute);
    }

}
