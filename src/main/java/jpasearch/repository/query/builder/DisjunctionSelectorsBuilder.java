package jpasearch.repository.query.builder;

import jpasearch.repository.query.selector.Selectors;

/**
 * @author speralta
 */
public class DisjunctionSelectorsBuilder<FROM, PARENT> extends SelectorsBuilder<FROM, PARENT, DisjunctionSelectorsBuilder<FROM, PARENT>> {

    public DisjunctionSelectorsBuilder(PARENT parent, Selectors<FROM> selectors) {
        super(parent, selectors);
        selectors.or();
    }

    public DisjunctionSelectorsBuilder(PARENT parent) {
        super(parent);
        selectors.or();
    }

    @Override
    protected DisjunctionSelectorsBuilder<FROM, PARENT> getThis() {
        return this;
    }

    public ConjunctionSelectorsBuilder<FROM, DisjunctionSelectorsBuilder<FROM, PARENT>> conjunction() {
        ConjunctionSelectorsBuilder<FROM, DisjunctionSelectorsBuilder<FROM, PARENT>> conjunction = new ConjunctionSelectorsBuilder<FROM, DisjunctionSelectorsBuilder<FROM, PARENT>>(this);
        selectors.add(conjunction.getSelectors());
        return conjunction;
    }

    public PARENT and() {
        return toParent();
    }
}
