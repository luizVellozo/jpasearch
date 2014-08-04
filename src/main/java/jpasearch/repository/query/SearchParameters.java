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

    // sort
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

    public SearchParameters(int firstResult, //
            int maxResults, //
            Selectors<FROM> selectors, //
            Set<Path<FROM, ?>> fetches, //
            Set<OrderBy<FROM, ?>> orders, //
            Map<String, Object> extraParameters, //
            boolean useDistinct) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.selectors = selectors;
        this.fetches = Collections.unmodifiableSet(fetches);
        this.orders = Collections.unmodifiableSet(orders);
        this.extraParameters = Collections.unmodifiableMap(extraParameters);
        this.useDistinct = useDistinct;
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
