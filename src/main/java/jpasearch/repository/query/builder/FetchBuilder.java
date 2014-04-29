package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;
import jpasearch.repository.query.SearchBuilder;

/**
 * @author speralta
 */
public class FetchBuilder<F, FROM, TO> {

    private final FetchesBuilder<F> builder;

    private final Path<F, TO> path;

    public FetchBuilder(FetchesBuilder<F> builder, SingularAttribute<? super F, TO> attribute) {
        this.builder = builder;
        this.path = new Path<F, TO>(attribute);
    }

    public FetchBuilder(FetchesBuilder<F> builder, PluralAttribute<? super F, ?, TO> attribute) {
        this.builder = builder;
        this.path = new Path<F, TO>(attribute);
    }

    private <E> FetchBuilder(FetchesBuilder<F> builder, FetchBuilder<F, E, FROM> fetchBuilder, SingularAttribute<? super FROM, TO> attribute) {
        this.builder = builder;
        this.path = fetchBuilder.path.add(attribute);
    }

    private <E> FetchBuilder(FetchesBuilder<F> builder, FetchBuilder<F, E, FROM> fetchBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        this.builder = builder;
        this.path = fetchBuilder.path.add(attribute);
    }

    public SearchBuilder<F> and() {
        return builder.add(path).toParent();
    }

    public <E> FetchBuilder<F, TO, E> to(SingularAttribute<? super TO, E> attribute) {
        return new FetchBuilder<F, TO, E>(builder, this, attribute);
    }

    public <E> FetchBuilder<F, TO, E> to(PluralAttribute<? super TO, ?, E> attribute) {
        return new FetchBuilder<F, TO, E>(builder, this, attribute);
    }

}
