package jpasearch.repository.query.builder;


/**
 * @author speralta
 */
public class PaginationBuilder<FROM> extends AbstractBuilder<SearchBuilder<FROM>> {

    private int maxResults;
    private int firstResult;

    public PaginationBuilder(SearchBuilder<FROM> searchParameters) {
        super(searchParameters);
        maxResults = -1;
        firstResult = 0;
    }

    public PaginationBuilder(SearchBuilder<FROM> searchParameters, int firstResult, int maxResults) {
        super(searchParameters);
        this.maxResults = maxResults;
        this.firstResult = firstResult;
    }

    public PaginationBuilder<FROM> firstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public PaginationBuilder<FROM> maxResult(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getFirstResult() {
        return firstResult;
    }

}
