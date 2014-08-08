package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;
import jpasearch.repository.query.selector.Range;

/**
 * @author speralta
 */
public class RangeFinalBuilder<F, FROM, TO extends Comparable<TO>, B extends SelectorsBuilder<F, ?, B>> extends AbstractPathBuilder<B, F, FROM, TO> {

    private Boolean includeNull = null;
    private boolean includeLowerBound = true;
    private boolean includeHigherBound = true;

    public RangeFinalBuilder(B parent, Path<F, TO> path) {
        super(parent, path);
    }

    public RangeFinalBuilder(B parent, PluralAttribute<? super F, ?, TO> attribute) {
        super(parent, attribute);
    }

    public RangeFinalBuilder(B parent, SingularAttribute<? super F, TO> attribute) {
        super(parent, attribute);
    }

    <E> RangeFinalBuilder(B parent, AbstractPathBuilder<B, F, E, FROM> pathBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        super(parent, pathBuilder, attribute);
    }

    <E> RangeFinalBuilder(B parent, AbstractPathBuilder<B, F, E, FROM> pathBuilder, SingularAttribute<? super FROM, TO> attribute) {
        super(parent, pathBuilder, attribute);
    }

    public B between(TO from, TO to) {
        return getParent().add(createRange().lowerBound(from).higherBound(to));
    }

    public B moreThan(TO from) {
        return getParent().add(createRange().lowerBound(from));
    }

    public B lessThan(TO to) {
        return getParent().add(createRange().higherBound(to));
    }

    public B and() {
        return getParent().add(createRange());
    }

    public RangeFinalBuilder<F, FROM, TO, B> includingNull() {
        includeNull = true;
        return this;
    }

    public RangeFinalBuilder<F, FROM, TO, B> excludingNull() {
        includeNull = false;
        return this;
    }

    public RangeFinalBuilder<F, FROM, TO, B> excludingLowerBound() {
        includeLowerBound = false;
        return this;
    }

    public RangeFinalBuilder<F, FROM, TO, B> excludingHigherBound() {
        includeHigherBound = false;
        return this;
    }

    public RangeFinalBuilder<F, FROM, TO, B> excludingBounds() {
        return excludingHigherBound().excludingLowerBound();
    }

    private Range<F, TO> createRange() {
        return new Range<>(getPath()).includeNull(includeNull).includeHigherBound(includeHigherBound).includeLowerBound(includeLowerBound);
    }
}
