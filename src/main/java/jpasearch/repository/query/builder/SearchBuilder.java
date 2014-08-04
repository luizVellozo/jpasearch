package jpasearch.repository.query.builder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.OrderByDirection;
import jpasearch.repository.query.Path;
import jpasearch.repository.query.SearchParameters;
import jpasearch.repository.query.selector.Range;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author speralta
 */
public class SearchBuilder<FROM> implements Serializable {
    private static final long serialVersionUID = 201403271745L;

    private final PaginationBuilder<FROM> paginationBuilder;
    private final OrdersByBuilder<FROM> ordersByBuilder;
    private final FetchesBuilder<FROM> fetchesBuilder;
    private final RootSelectorsBuilder<FROM> rootSelectorsBuilder;

    // extra parameters
    private final Map<String, Object> extraParameters;

    private boolean useDistinct;

    public SearchBuilder() {
        paginationBuilder = new PaginationBuilder<>(this);
        ordersByBuilder = new OrdersByBuilder<>(this);
        fetchesBuilder = new FetchesBuilder<>(this);
        rootSelectorsBuilder = new RootSelectorsBuilder<>(this);
        extraParameters = new HashMap<>();
        useDistinct = false;
    }

    public SearchBuilder(SearchParameters<FROM> searchParameters) {
        paginationBuilder = new PaginationBuilder<>(this, searchParameters.getFirstResult(), searchParameters.getMaxResults());
        ordersByBuilder = new OrdersByBuilder<>(this, searchParameters.getOrders());
        fetchesBuilder = new FetchesBuilder<>(this, searchParameters.getFetches());
        rootSelectorsBuilder = new RootSelectorsBuilder<>(this, searchParameters.getSelectors());
        extraParameters = new HashMap<>(searchParameters.getExtraParameters());
        useDistinct = searchParameters.isUseDistinct();
    }

    public SearchParameters<FROM> build() {
        return new SearchParameters<FROM>(paginationBuilder.getFirstResult(), //
                paginationBuilder.getMaxResults(), //
                rootSelectorsBuilder.selectors, //
                fetchesBuilder.getFetches(), //
                ordersByBuilder.getOrders(), //
                extraParameters, //
                useDistinct);
    }

    // -----------------------------------
    // Order by support
    // -----------------------------------

    public <TO> OrderByBuilder<FROM, FROM, TO> orderBy(SingularAttribute<? super FROM, TO> attribute) {
        return ordersByBuilder.by(attribute);
    }

    public <TO> OrderByBuilder<FROM, FROM, TO> orderBy(PluralAttribute<? super FROM, ?, TO> attribute) {
        return ordersByBuilder.by(attribute);
    }

    public SearchBuilder<FROM> orderBy(OrderByDirection direction, String path, Class<FROM> from) {
        ordersByBuilder.orderBy(direction, path, from);
        return this;
    }

    // -------------------------------------
    // SearchParameters by selectors support
    // -------------------------------------

    public <E extends Comparable<? super E>> SearchBuilder<FROM> between(E from, E to, SingularAttribute<? super FROM, E> attribute) {
        rootSelectorsBuilder.add(new Range<FROM, E>(from, to, new Path<>(attribute)));
        return this;
    }

    public <E extends Comparable<? super E>> SearchBuilder<FROM> between(E from, E to, PluralAttribute<? super FROM, ?, E> attribute) {
        rootSelectorsBuilder.add(new Range<FROM, E>(from, to, new Path<>(attribute)));
        return this;
    }

    public TermSelectorBuilder<FROM, SearchBuilder<FROM>, RootSelectorsBuilder<FROM>> fullText(SingularAttribute<? super FROM, ? extends Serializable> attribute) {
        return new TermSelectorBuilder<>(rootSelectorsBuilder, new Path<>(attribute));
    }

    public <TO> SelectorBuilder<FROM, FROM, TO, RootSelectorsBuilder<FROM>> on(Path<FROM, TO> path) {
        return rootSelectorsBuilder.on(path);
    }

    public <TO> SelectorBuilder<FROM, FROM, TO, RootSelectorsBuilder<FROM>> on(SingularAttribute<? super FROM, TO> attribute) {
        return rootSelectorsBuilder.on(attribute);
    }

    public <TO> SelectorBuilder<FROM, FROM, TO, RootSelectorsBuilder<FROM>> on(PluralAttribute<? super FROM, ?, TO> attribute) {
        return rootSelectorsBuilder.on(attribute);
    }

    public DisjunctionSelectorsBuilder<FROM, RootSelectorsBuilder<FROM>> disjunction() {
        return rootSelectorsBuilder.disjunction();
    }

    // -----------------------------------
    // Pagination support
    // -----------------------------------

    public SearchBuilder<FROM> paginate(int first, int count) {
        paginationBuilder.firstResult(first).maxResult(count);
        return this;
    }

    // -----------------------------------------
    // Fetch associated entity using a LEFT Join
    // -----------------------------------------

    public <TO> FetchBuilder<FROM, FROM, TO> fetch(SingularAttribute<? super FROM, TO> attribute) {
        return fetchesBuilder.fetch(attribute);
    }

    public <TO> FetchBuilder<FROM, FROM, TO> fetch(PluralAttribute<? super FROM, ?, TO> attribute) {
        return fetchesBuilder.fetch(attribute);
    }

    // -----------------------------------
    // Extra parameters
    // -----------------------------------

    /**
     * add additionnal parameter.
     */
    public SearchBuilder<FROM> addExtraParameter(String key, Object o) {
        extraParameters.put(checkNotNull(key), o);
        return this;
    }

    // -----------------------------------
    // Distinct
    // -----------------------------------

    public SearchBuilder<FROM> distinct() {
        useDistinct = true;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}