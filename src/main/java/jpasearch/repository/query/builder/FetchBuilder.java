package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

/**
 * @author speralta
 */
public class FetchBuilder<F, FROM, TO> extends AbstractPathBuilder<FetchesBuilder<F>, F, FROM, TO> {

    public FetchBuilder(FetchesBuilder<F> parent, PluralAttribute<? super F, ?, TO> attribute) {
        super(parent, attribute);
    }

    public FetchBuilder(FetchesBuilder<F> parent, SingularAttribute<? super F, TO> attribute) {
        super(parent, attribute);
    }

    private <E> FetchBuilder(FetchesBuilder<F> parent, FetchBuilder<F, E, FROM> fetchBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        super(parent, fetchBuilder, attribute);
    }

    private <E> FetchBuilder(FetchesBuilder<F> parent, FetchBuilder<F, E, FROM> fetchBuilder, SingularAttribute<? super FROM, TO> attribute) {
        super(parent, fetchBuilder, attribute);
    }

    public SearchBuilder<F> and() {
        return getParent().add(getPath()).toParent();
    }

    public <E> FetchBuilder<F, TO, E> to(SingularAttribute<? super TO, E> attribute) {
        return new FetchBuilder<F, TO, E>(getParent(), this, attribute);
    }

    public <E> FetchBuilder<F, TO, E> to(PluralAttribute<? super TO, ?, E> attribute) {
        return new FetchBuilder<F, TO, E>(getParent(), this, attribute);
    }

}
