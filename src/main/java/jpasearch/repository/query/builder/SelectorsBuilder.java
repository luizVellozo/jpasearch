package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;
import jpasearch.repository.query.selector.Selector;
import jpasearch.repository.query.selector.Selectors;

/**
 * @author speralta
 */
public abstract class SelectorsBuilder<FROM, PARENT, CURRENT extends SelectorsBuilder<FROM, PARENT, CURRENT>> extends AbstractBuilder<PARENT> {

    protected final Selectors<FROM> selectors;

    public SelectorsBuilder(PARENT parent) {
        super(parent);
        selectors = new Selectors<>();
    }

    public SelectorsBuilder(PARENT parent, Selectors<FROM> selectors) {
        super(parent);
        this.selectors = selectors.copy();
    }

    public <TO> SelectorBuilder<FROM, FROM, TO, CURRENT> on(Path<FROM, TO> path) {
        return new SelectorBuilder<>(getThis(), path);
    }

    public <TO> SelectorBuilder<FROM, FROM, TO, CURRENT> on(SingularAttribute<? super FROM, TO> attribute) {
        return new SelectorBuilder<>(getThis(), attribute);
    }

    public <TO> SelectorBuilder<FROM, FROM, TO, CURRENT> on(PluralAttribute<? super FROM, ?, TO> attribute) {
        return new SelectorBuilder<>(getThis(), attribute);
    }

    public <TO> TermSelectorPathBuilder<FROM, FROM, TO, CURRENT, PARENT> fullText(SingularAttribute<? super FROM, TO> attribute) {
        return new TermSelectorBuilder<FROM, CURRENT, PARENT>(getThis()).on(attribute);
    }

    public <TO> TermSelectorPathBuilder<FROM, FROM, TO, CURRENT, PARENT> fullText(PluralAttribute<? super FROM, ?, TO> attribute) {
        return new TermSelectorBuilder<FROM, CURRENT, PARENT>(getThis()).on(attribute);
    }

    public Selectors<FROM> getSelectors() {
        return selectors;
    }

    public CURRENT add(Selector<FROM, ?> selector) {
        selectors.add(selector);
        return getThis();
    }

    protected abstract CURRENT getThis();

}
