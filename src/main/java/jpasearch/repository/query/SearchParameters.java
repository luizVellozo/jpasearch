package jpasearch.repository.query;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import jpasearch.repository.query.selector.Selectors;

/**
 * @author speralta
 */
public class SearchParameters<FROM> implements Serializable {

    private static final long serialVersionUID = 201403271745L;

    private final Set<OrderBy<FROM, ?>> orders;

    // pagination
    private final int maxResults;
    private final int firstResult;

    // fetches
    private final Set<Path<FROM, ?>> fetches;

    // selectors
    private final Selectors<FROM> selectors;

    // extra namedQueryParameters
    private final Map<String, Object> extraParameters;

    private final boolean useDistinct;

    SearchParameters(SearchBuilder<FROM> builder) {
        extraParameters = Collections.unmodifiableMap(builder.getExtraParameters());
        fetches = Collections.unmodifiableSet(builder.getFetches());
        firstResult = builder.getFirstResult();
        maxResults = builder.getMaxResults();
        orders = Collections.unmodifiableSet(builder.getOrders());
        selectors = builder.getSelectors();
        useDistinct = builder.isUseDistinct();
    }

    public Set<OrderBy<FROM, ?>> getOrders() {
        return orders;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public Set<Path<FROM, ?>> getFetches() {
        return fetches;
    }

    public Selectors<FROM> getSelectors() {
        return selectors;
    }

    public Map<String, Object> getExtraParameters() {
        return extraParameters;
    }

    public boolean isUseDistinct() {
        return useDistinct;
    }

}
