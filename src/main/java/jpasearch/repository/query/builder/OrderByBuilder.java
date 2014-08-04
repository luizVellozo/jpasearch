package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

/**
 * @author speralta
 */
public class OrderByBuilder<F, FROM, TO> extends AbstractPathBuilder<OrdersByBuilder<F>, F, FROM, TO> {

    public OrderByBuilder(OrdersByBuilder<F> parent, PluralAttribute<? super F, ?, TO> attribute) {
        super(parent, attribute);
    }

    public OrderByBuilder(OrdersByBuilder<F> parent, SingularAttribute<? super F, TO> attribute) {
        super(parent, attribute);
    }

    private <E> OrderByBuilder(OrdersByBuilder<F> parent, OrderByBuilder<F, E, FROM> orderByBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        super(parent, orderByBuilder, attribute);
    }

    private <E> OrderByBuilder(OrdersByBuilder<F> parent, OrderByBuilder<F, E, FROM> orderByBuilder, SingularAttribute<? super FROM, TO> attribute) {
        super(parent, orderByBuilder, attribute);
    }

    public SearchBuilder<F> asc() {
        return getParent().asc(getPath()).toParent();
    }

    public SearchBuilder<F> desc() {
        return getParent().desc(getPath()).toParent();
    }

    public <E> OrderByBuilder<F, TO, E> and(SingularAttribute<? super TO, E> attribute) {
        return new OrderByBuilder<F, TO, E>(getParent(), this, attribute);
    }

    public <E> OrderByBuilder<F, TO, E> and(PluralAttribute<? super TO, ?, E> attribute) {
        return new OrderByBuilder<F, TO, E>(getParent(), this, attribute);
    }

}
