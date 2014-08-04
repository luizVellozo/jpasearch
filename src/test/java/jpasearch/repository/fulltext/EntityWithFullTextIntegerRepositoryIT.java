package jpasearch.repository.fulltext;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.fulltext.EntityWithFullTextInteger;
import jpasearch.domain.fulltext.EntityWithFullTextInteger_;
import jpasearch.repository.query.SearchParameters;
import jpasearch.repository.query.builder.SearchBuilder;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author speralta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class EntityWithFullTextIntegerRepositoryIT {

    @Inject
    private EntityWithFullTextIntegerRepository entityWithFullTextIntegerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void test() {
        final Integer testValue = 1995;

        assertThat(entityWithFullTextIntegerRepository.findCount(findByValue(testValue))).isEqualTo(0);

        EntityWithFullTextInteger entityWithFullTextInteger = new EntityWithFullTextInteger();
        entityWithFullTextInteger.setValue(testValue);
        entityWithFullTextInteger = entityWithFullTextIntegerRepository.save(entityWithFullTextInteger);
        EntityWithFullTextInteger another = new EntityWithFullTextInteger();
        another.setValue(1994);
        entityWithFullTextIntegerRepository.save(another);

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.index(entityWithFullTextInteger);
        fullTextEntityManager.flushToIndexes();
        fullTextEntityManager.flush();

        assertThat(entityWithFullTextIntegerRepository.find(findByValue(testValue))).containsExactly(entityWithFullTextInteger);
    }

    private SearchParameters<EntityWithFullTextInteger> findByValue(Integer value) {
        return new SearchBuilder<EntityWithFullTextInteger>() //
                .fullText(EntityWithFullTextInteger_.value) //
                .search(value.toString()) //
                .build();
    }

}
