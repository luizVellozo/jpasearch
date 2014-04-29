package jpasearch.repository.query.builder;

import jpasearch.repository.query.selector.Selectors;

/**
 * @author speralta
 */
public class ConjunctionSelectorsBuilder<FROM, PARENT> extends SelectorsBuilder<FROM, PARENT, ConjunctionSelectorsBuilder<FROM, PARENT>> {

    public ConjunctionSelectorsBuilder(PARENT parent, Selectors<FROM> propertySelectors) {
        super(parent, propertySelectors);
    }

    public ConjunctionSelectorsBuilder(PARENT parent) {
        super(parent);
    }

    @Override
    protected ConjunctionSelectorsBuilder<FROM, PARENT> getThis() {
        return this;
    }

    public DisjunctionSelectorsBuilder<FROM, ConjunctionSelectorsBuilder<FROM, PARENT>> disjunction() {
        DisjunctionSelectorsBuilder<FROM, ConjunctionSelectorsBuilder<FROM, PARENT>> disjunction = new DisjunctionSelectorsBuilder<FROM, ConjunctionSelectorsBuilder<FROM, PARENT>>(this);
        selectors.add(disjunction.getSelectors().or());
        return disjunction;
    }

    public PARENT or() {
        return toParent();
    }
}
