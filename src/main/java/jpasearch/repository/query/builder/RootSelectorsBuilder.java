package jpasearch.repository.query.builder;

import jpasearch.repository.query.SearchBuilder;
import jpasearch.repository.query.SearchParameters;
import jpasearch.repository.query.selector.Selectors;

/**
 * @author speralta
 */
public class RootSelectorsBuilder<FROM> extends SelectorsBuilder<FROM, SearchBuilder<FROM>, RootSelectorsBuilder<FROM>> {

    public RootSelectorsBuilder(SearchBuilder<FROM> parent, Selectors<FROM> propertySelectors) {
        super(parent, propertySelectors);
    }

    public RootSelectorsBuilder(SearchBuilder<FROM> parent) {
        super(parent);
    }

    public DisjunctionSelectorsBuilder<FROM, RootSelectorsBuilder<FROM>> disjunction() {
        DisjunctionSelectorsBuilder<FROM, RootSelectorsBuilder<FROM>> disjunction = new DisjunctionSelectorsBuilder<FROM, RootSelectorsBuilder<FROM>>(this);
        selectors.add(disjunction.getSelectors().or());
        return disjunction;
    }

    @Override
    protected RootSelectorsBuilder<FROM> getThis() {
        return this;
    }

    public SearchParameters<FROM> build() {
        return toParent().build();
    }

    public SearchBuilder<FROM> and() {
        return toParent();
    }

}
