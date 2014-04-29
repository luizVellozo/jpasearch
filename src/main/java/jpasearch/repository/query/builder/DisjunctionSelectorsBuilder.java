package jpasearch.repository.query.builder;

import jpasearch.repository.query.selector.Selectors;

/**
 * @author speralta
 */
public class DisjunctionSelectorsBuilder<FROM, PARENT> extends SelectorsBuilder<FROM, PARENT, DisjunctionSelectorsBuilder<FROM, PARENT>> {

    public DisjunctionSelectorsBuilder(PARENT parent, Selectors<FROM> propertySelectors) {
        super(parent, propertySelectors);
    }

    public DisjunctionSelectorsBuilder(PARENT parent) {
        super(parent);
    }

    @Override
    protected DisjunctionSelectorsBuilder<FROM, PARENT> getThis() {
        return this;
    }

    public ConjunctionSelectorsBuilder<FROM, DisjunctionSelectorsBuilder<FROM, PARENT>> conjunction() {
        ConjunctionSelectorsBuilder<FROM, DisjunctionSelectorsBuilder<FROM, PARENT>> conjunction = new ConjunctionSelectorsBuilder<FROM, DisjunctionSelectorsBuilder<FROM, PARENT>>(this);
        selectors.add(conjunction.getSelectors().or());
        return conjunction;
    }

    public PARENT and() {
        return toParent();
    }
}
