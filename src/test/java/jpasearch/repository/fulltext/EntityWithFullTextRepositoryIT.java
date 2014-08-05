package jpasearch.repository.fulltext;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.fulltext.EntityWithFullText;
import jpasearch.domain.fulltext.EntityWithFullText_;
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
public class EntityWithFullTextRepositoryIT {

    @Inject
    private EntityWithFullTextRepository entityWithFullTextRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void test() {
        final String testValue = "test";

        assertThat(entityWithFullTextRepository.findCount(findByValue(testValue))).isEqualTo(0);

        EntityWithFullText entityWithFullText = new EntityWithFullText();
        entityWithFullText.setValue(testValue);
        entityWithFullText = entityWithFullTextRepository.save(entityWithFullText);

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.index(entityWithFullText);
        fullTextEntityManager.flushToIndexes();
        fullTextEntityManager.flush();

        assertThat(entityWithFullTextRepository.find(findByValue(testValue))).containsExactly(entityWithFullText);
    }

    private SearchParameters<EntityWithFullText> findByValue(String value) {
        return new SearchBuilder<EntityWithFullText>() //
                .fullText(EntityWithFullText_.value) //
                .search(value) //
                .build();
    }

    @Test
    public void test_similarity() {
        assertThat(entityWithFullTextRepository.findCount(findByValueWithSimilarity("tast"))).isEqualTo(0);

        EntityWithFullText entityWithFullText = new EntityWithFullText();
        entityWithFullText.setValue("test");
        entityWithFullText = entityWithFullTextRepository.save(entityWithFullText);

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.index(entityWithFullText);
        fullTextEntityManager.flushToIndexes();
        fullTextEntityManager.flush();

        assertThat(entityWithFullTextRepository.find(findByValueWithSimilarity("tast"))).containsExactly(entityWithFullText);
    }

    private SearchParameters<EntityWithFullText> findByValueWithSimilarity(String value) {
        return new SearchBuilder<EntityWithFullText>() //
                .fullText(EntityWithFullText_.value) //
                .searchSimilarity(2) //
                .search(value) //
                .build();
    }

}
