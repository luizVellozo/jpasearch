package jpasearch.repository.query.builder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;
import jpasearch.repository.query.SearchBuilder;

/**
 * @author speralta
 */
public class FetchesBuilder<FROM> extends AbstractBuilder<SearchBuilder<FROM>> {

    private final Set<Path<FROM, ?>> fetches;

    public FetchesBuilder(SearchBuilder<FROM> searchParameters) {
        super(searchParameters);
        fetches = new HashSet<>();
    }

    public FetchesBuilder(SearchBuilder<FROM> searchParameters, Set<Path<FROM, ?>> fetches) {
        super(searchParameters);
        this.fetches = new HashSet<>(fetches);
    }

    public <TO> FetchBuilder<FROM, FROM, TO> fetch(SingularAttribute<? super FROM, TO> attribute) {
        return new FetchBuilder<>(this, attribute);
    }

    public <TO> FetchBuilder<FROM, FROM, TO> fetch(PluralAttribute<? super FROM, ?, TO> attribute) {
        return new FetchBuilder<>(this, attribute);
    }

    public Set<Path<FROM, ?>> getFetches() {
        return Collections.unmodifiableSet(fetches);
    }

    <TO> FetchesBuilder<FROM> add(Path<FROM, TO> path) {
        fetches.add(path);
        return this;
    }

}
