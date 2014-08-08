package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;

/**
 * @author speralta
 */
public class RangeBuilder<F, FROM, TO, B extends SelectorsBuilder<F, ?, B>> extends AbstractPathBuilder<B, F, FROM, TO> {

    public RangeBuilder(B parent, Path<F, TO> path) {
        super(parent, path);
    }

    public RangeBuilder(B parent, PluralAttribute<? super F, ?, TO> attribute) {
        super(parent, attribute);
    }

    public RangeBuilder(B parent, SingularAttribute<? super F, TO> attribute) {
        super(parent, attribute);
    }

    private <E> RangeBuilder(B parent, AbstractPathBuilder<B, F, E, FROM> pathBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        super(parent, pathBuilder, attribute);
    }

    private <E> RangeBuilder(B parent, AbstractPathBuilder<B, F, E, FROM> pathBuilder, SingularAttribute<? super FROM, TO> attribute) {
        super(parent, pathBuilder, attribute);
    }

    public <E> RangeBuilder<F, TO, E, B> to(SingularAttribute<? super TO, E> attribute) {
        return new RangeBuilder<>(getParent(), this, attribute);
    }

    public <E> RangeBuilder<F, TO, E, B> to(PluralAttribute<? super TO, ?, E> attribute) {
        return new RangeBuilder<>(getParent(), this, attribute);
    }

    public <E extends Comparable<E>> RangeFinalBuilder<F, TO, E, B> finallyOn(SingularAttribute<? super TO, E> attribute) {
        return new RangeFinalBuilder<>(getParent(), this, attribute);
    }

    public <E extends Comparable<E>> RangeFinalBuilder<F, TO, E, B> finallyOn(PluralAttribute<? super TO, ?, E> attribute) {
        return new RangeFinalBuilder<>(getParent(), this, attribute);
    }

}
