package jpasearch.repository.util;

import static org.hibernate.search.jpa.Search.getFullTextEntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpasearch.repository.query.selector.ObjectTermSelector;
import jpasearch.repository.query.selector.StringTermSelector;
import jpasearch.repository.query.selector.TermSelector;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Singleton
public class HibernateSearchUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateSearchUtil.class);

    private EntityManager entityManager;

    private LuceneQueryBuilder luceneQueryBuilder;

    @SuppressWarnings("unchecked")
    public <T> List<T> find(Class<? extends T> type, TermSelector<T, ?> termSelector) {
        logger.debug("Searching {} with term {}.", new Object[] { type.getSimpleName(), termSelector });
        FullTextQuery ftq = getQuery(type, termSelector);
        ftq.limitExecutionTimeTo(500, TimeUnit.MILLISECONDS);
        return ftq.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<Serializable> findId(Class<? extends T> type, TermSelector<T, ?> termSelector) {
        logger.debug("Searching {} id with term {}.", new Object[] { type.getSimpleName(), termSelector });
        FullTextQuery ftq = getQuery(type, termSelector);
        ftq.setProjection("id");
        ftq.limitExecutionTimeTo(500, TimeUnit.MILLISECONDS);
        List<Serializable> ids = new ArrayList<>();
        List<Object[]> resultList = ftq.getResultList();
        for (Object[] result : resultList) {
            ids.add((Serializable) result[0]);
        }
        return ids;
    }

    @SuppressWarnings("unchecked")
    private <T> FullTextQuery getQuery(Class<? extends T> type, TermSelector<T, ?> termSelector) {
        FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(entityManager);
        Query query;
        if (termSelector instanceof StringTermSelector) {
            query = luceneQueryBuilder.build(fullTextEntityManager, (StringTermSelector<T>) termSelector, type);
        } else if (termSelector instanceof ObjectTermSelector) {
            query = luceneQueryBuilder.build(fullTextEntityManager, (ObjectTermSelector<T>) termSelector, type);
        } else {
            throw new RuntimeException("Unknown TermSelector: " + termSelector.getClass().getName());
        }

        if (query == null) {
            return null;
        }

        logger.debug("Query: {}.", query.toString());
        FullTextQuery ftq = fullTextEntityManager.createFullTextQuery( //
                query, type);
        return ftq;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Inject
    public void setLuceneQueryBuilder(LuceneQueryBuilder luceneQueryBuilder) {
        this.luceneQueryBuilder = luceneQueryBuilder;
    }

}