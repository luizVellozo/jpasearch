package jpasearch.repository.util;

import jpasearch.repository.query.selector.TermSelector;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;

public interface LuceneQueryBuilder {

    <T> Query build(FullTextEntityManager fullTextEntityManager, TermSelector<T> termSelector, Class<? extends T> type);
}