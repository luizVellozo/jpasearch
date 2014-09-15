package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

/**
 * @author speralta
 */
public class TermSelectorPathBuilder<F, FROM, TO, PARENT extends SelectorsBuilder<F, GRANDPARENT, PARENT>, GRANDPARENT> extends
        AbstractPathBuilder<TermSelectorBuilder<F, PARENT, GRANDPARENT>, F, FROM, TO> {

    public TermSelectorPathBuilder(TermSelectorBuilder<F, PARENT, GRANDPARENT> parent, PluralAttribute<? super F, ?, TO> attribute) {
        super(parent, attribute);
    }

    public TermSelectorPathBuilder(TermSelectorBuilder<F, PARENT, GRANDPARENT> parent, SingularAttribute<? super F, TO> attribute) {
        super(parent, attribute);
    }

    private <E> TermSelectorPathBuilder(TermSelectorBuilder<F, PARENT, GRANDPARENT> parent, TermSelectorPathBuilder<F, E, FROM, PARENT, GRANDPARENT> termSelectorPathBuilder,
            PluralAttribute<? super FROM, ?, TO> attribute) {
        super(parent, termSelectorPathBuilder, attribute);
    }

    private <E> TermSelectorPathBuilder(TermSelectorBuilder<F, PARENT, GRANDPARENT> parent, TermSelectorPathBuilder<F, E, FROM, PARENT, GRANDPARENT> termSelectorPathBuilder,
            SingularAttribute<? super FROM, TO> attribute) {
        super(parent, termSelectorPathBuilder, attribute);
    }

    public TermSelectorBuilder<F, PARENT, GRANDPARENT> searchSimilarity(Integer searchSimilarity) {
        return getParent().searchSimilarity(this, searchSimilarity);
    }

    public TermSelectorBuilder<F, PARENT, GRANDPARENT> andMode() {
        return getParent().andMode(this);
    }

    public PARENT search(String... selected) {
        return getParent().search(this, selected);
    }

    public <NEWTO> TermSelectorPathBuilder<F, F, NEWTO, PARENT, GRANDPARENT> andOn(SingularAttribute<? super F, NEWTO> attribute) {
        return getParent().andOn(this, attribute);
    }

    public <NEWTO> TermSelectorPathBuilder<F, F, NEWTO, PARENT, GRANDPARENT> andOn(PluralAttribute<? super F, ?, NEWTO> attribute) {
        return getParent().andOn(this, attribute);
    }

    public <E> TermSelectorPathBuilder<F, TO, E, PARENT, GRANDPARENT> to(SingularAttribute<? super TO, E> attribute) {
        return new TermSelectorPathBuilder<>(getParent(), this, attribute);
    }

    public <E> TermSelectorPathBuilder<F, TO, E, PARENT, GRANDPARENT> to(PluralAttribute<? super TO, ?, E> attribute) {
        return new TermSelectorPathBuilder<>(getParent(), this, attribute);
    }

}
