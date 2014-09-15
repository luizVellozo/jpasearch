package jpasearch.repository.fulltext;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.fulltext.EntityWithFullText;
import jpasearch.domain.fulltext.EntityWithFullText_;
import jpasearch.domain.fulltext.EntityWithMultipleFullText;
import jpasearch.domain.fulltext.EntityWithMultipleFullText_;
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
public class EntityWithMultipleFullTextRepositoryIT {

    @Inject
    private EntityWithMultipleFullTextRepository entityWithMultipleFullTextRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void test() {
        final String testValue = "test";

        assertThat(entityWithMultipleFullTextRepository.findCount(findByValue(testValue))).isEqualTo(0);

        EntityWithMultipleFullText entityWithMultipleFullText = new EntityWithMultipleFullText();
        entityWithMultipleFullText.setValue1(testValue);
        entityWithMultipleFullText.setValue2("abcdef");
        entityWithMultipleFullText = entityWithMultipleFullTextRepository.save(entityWithMultipleFullText);

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.index(entityWithMultipleFullText);
        fullTextEntityManager.flushToIndexes();
        fullTextEntityManager.flush();

        assertThat(entityWithMultipleFullTextRepository.find(findByValue(testValue))).containsExactly(entityWithMultipleFullText);
    }

    @Test
    public void testAnd() {
        final String testValue1 = "abc";
        final String testValue2 = "def";

        assertThat(entityWithMultipleFullTextRepository.findCount(findByValue(testValue1, testValue2))).isEqualTo(0);

        EntityWithMultipleFullText entityWithMultipleFullText = new EntityWithMultipleFullText();
        entityWithMultipleFullText.setValue1(testValue1);
        entityWithMultipleFullText.setValue2(testValue2);
        entityWithMultipleFullText = entityWithMultipleFullTextRepository.save(entityWithMultipleFullText);

        EntityWithMultipleFullText entityWithMultipleFullText2 = new EntityWithMultipleFullText();
        entityWithMultipleFullText2.setValue1(testValue1);
        entityWithMultipleFullText2.setValue2("toto");
        entityWithMultipleFullText2 = entityWithMultipleFullTextRepository.save(entityWithMultipleFullText2);

        EntityWithMultipleFullText entityWithMultipleFullText3 = new EntityWithMultipleFullText();
        entityWithMultipleFullText3.setValue1("toto");
        entityWithMultipleFullText3.setValue2(testValue2);
        entityWithMultipleFullText3 = entityWithMultipleFullTextRepository.save(entityWithMultipleFullText3);

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.index(entityWithMultipleFullText);
        fullTextEntityManager.index(entityWithMultipleFullText2);
        fullTextEntityManager.index(entityWithMultipleFullText3);
        fullTextEntityManager.flushToIndexes();
        fullTextEntityManager.flush();

        assertThat(entityWithMultipleFullTextRepository.find(findByValue(testValue1, testValue2))).containsExactly(entityWithMultipleFullText);
    }

    private SearchParameters<EntityWithMultipleFullText> findByValue(String... value) {
        return new SearchBuilder<EntityWithMultipleFullText>() //
                .fullText(EntityWithMultipleFullText_.value1) //
                .andOn(EntityWithMultipleFullText_.value2) //
                .andMode() //
                .search(value) //
                .build();
    }

    @Test
    public void deep_test() {
        final String testValue = "test";

        assertThat(entityWithMultipleFullTextRepository.findCount(findByDeepValue(testValue))).isEqualTo(0);

        EntityWithMultipleFullText entityWithMultipleFullText = new EntityWithMultipleFullText();
        entityWithMultipleFullText.setValue1("abcdef");
        EntityWithFullText entityWithFullText = new EntityWithFullText();
        entityWithFullText.setValue(testValue);
        entityWithMultipleFullText.setEntityWithFullText(entityWithFullText);
        entityWithMultipleFullText = entityWithMultipleFullTextRepository.save(entityWithMultipleFullText);

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.index(entityWithMultipleFullText);
        fullTextEntityManager.flushToIndexes();
        fullTextEntityManager.flush();

        assertThat(entityWithMultipleFullTextRepository.find(findByDeepValue(testValue))).containsExactly(entityWithMultipleFullText);
    }

    private SearchParameters<EntityWithMultipleFullText> findByDeepValue(String value) {
        return new SearchBuilder<EntityWithMultipleFullText>() //
                .fullText(EntityWithMultipleFullText_.value1) //
                .andOn(EntityWithMultipleFullText_.value2) //
                .andOn(EntityWithMultipleFullText_.entityWithFullText) //
                .to(EntityWithFullText_.value) //
                .search(value).build();
    }

}
