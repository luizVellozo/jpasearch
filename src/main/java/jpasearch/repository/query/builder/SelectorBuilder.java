package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;
import jpasearch.repository.query.SearchMode;
import jpasearch.repository.query.selector.ObjectTermSelector;
import jpasearch.repository.query.selector.PropertySelector;
import jpasearch.repository.query.selector.Range;
import jpasearch.repository.query.selector.StringTermSelector;

/**
 * @author speralta
 */
public class SelectorBuilder<F, FROM, TO, B extends SelectorsBuilder<F, ?, B>> extends AbstractPathBuilder<B, F, FROM, TO> {

    private boolean not = false;

    public SelectorBuilder(B builder, Path<F, TO> path) {
        super(builder, path);
    }

    public SelectorBuilder(B builder, SingularAttribute<? super F, TO> attribute) {
        super(builder, attribute);
    }

    public SelectorBuilder(B builder, PluralAttribute<? super F, ?, TO> attribute) {
        super(builder, attribute);
    }

    private <E> SelectorBuilder(B builder, SelectorBuilder<F, E, FROM, B> selectorBuilder, SingularAttribute<? super FROM, TO> attribute) {
        super(builder, selectorBuilder, attribute);
        this.not = selectorBuilder.not;
    }

    private <E> SelectorBuilder(B builder, SelectorBuilder<F, E, FROM, B> selectorBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        super(builder, selectorBuilder, attribute);
        this.not = selectorBuilder.not;
    }

    @SuppressWarnings("unchecked")
    public B equalsTo(TO... values) {
        return getParent().add(new PropertySelector<>(getPath(), values).searchMode(SearchMode.EQUALS).notMode(not));
    }

    @SuppressWarnings("unchecked")
    public B anywhere(TO... values) {
        return getParent().add(new PropertySelector<>(getPath(), values).searchMode(SearchMode.ANYWHERE).notMode(not));
    }

    @SuppressWarnings("unchecked")
    public B endingLike(TO... values) {
        return getParent().add(new PropertySelector<>(getPath(), values).searchMode(SearchMode.ENDING_LIKE).notMode(not));
    }

    @SuppressWarnings("unchecked")
    public B startingLike(TO... values) {
        return getParent().add(new PropertySelector<>(getPath(), values).searchMode(SearchMode.STARTING_LIKE).notMode(not));
    }

    @SuppressWarnings("unchecked")
    public B like(TO... values) {
        return getParent().add(new PropertySelector<>(getPath(), values).searchMode(SearchMode.LIKE).notMode(not));
    }

    public B fullText(String... selected) {
        return getParent().add(new StringTermSelector<>(getPath()).notMode(not).selected(selected));
    }

    public B fullText(Object... selected) {
        return getParent().add(new ObjectTermSelector<>(getPath()).notMode(not).selected(selected));
    }

    public <E extends Comparable<E>> B between(E from, E to, SingularAttribute<? super TO, E> attribute) {
        return getParent().add(new Range<F, E>(from, to, getPath().add(attribute)));
    }

    public <E extends Comparable<E>> B between(E from, E to, PluralAttribute<? super TO, ?, E> attribute) {
        return getParent().add(new Range<F, E>(from, to, getPath().add(attribute)));
    }

    public <E> SelectorBuilder<F, TO, E, B> to(SingularAttribute<? super TO, E> attribute) {
        return new SelectorBuilder<F, TO, E, B>(getParent(), this, attribute);
    }

    public <E> SelectorBuilder<F, TO, E, B> to(PluralAttribute<? super TO, ?, E> attribute) {
        return new SelectorBuilder<F, TO, E, B>(getParent(), this, attribute);
    }

    public SelectorBuilder<F, FROM, TO, B> not() {
        not = true;
        return this;
    }

}
