package jpasearch.repository.util;

import jpasearch.repository.query.selector.ObjectTermSelector;
import jpasearch.repository.query.selector.StringTermSelector;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;

public interface LuceneQueryBuilder {

    <T> Query build(FullTextEntityManager fullTextEntityManager, StringTermSelector<T> termSelector, Class<? extends T> type);

    <T> Query build(FullTextEntityManager fullTextEntityManager, ObjectTermSelector<T> termSelector, Class<? extends T> type);

}
