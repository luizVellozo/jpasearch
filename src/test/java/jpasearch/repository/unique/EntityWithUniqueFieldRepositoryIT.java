package jpasearch.repository.unique;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.unique.EntityWithUniqueField;
import jpasearch.domain.unique.EntityWithUniqueField_;
import jpasearch.repository.query.SearchBuilder;
import jpasearch.repository.query.SearchParameters;

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
public class EntityWithUniqueFieldRepositoryIT {

    @Inject
    private EntityWithUniqueFieldRepository entityWithUniqueFieldRepository;

    @Test
    public void findUniqueFailsNoResult() {
        try {
            entityWithUniqueFieldRepository.findUnique(findByValue("test"));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NoResultException.class);
        }
    }

    @Test
    public void findUniqueFailsMoreResults() {
        EntityWithUniqueField entityWithUniqueField1 = new EntityWithUniqueField();
        entityWithUniqueField1.setValue("test1");
        entityWithUniqueFieldRepository.save(entityWithUniqueField1);
        EntityWithUniqueField entityWithUniqueField2 = new EntityWithUniqueField();
        entityWithUniqueField2.setValue("test2");
        entityWithUniqueFieldRepository.save(entityWithUniqueField2);

        try {
            entityWithUniqueFieldRepository.findUnique(new SearchBuilder<EntityWithUniqueField>().build());
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NonUniqueResultException.class);
        }
    }

    @Test
    public void findUnique() {
        final String testValue = "test";

        assertThat(entityWithUniqueFieldRepository.findUniqueOrNone(findByValue(testValue))).isNull();

        EntityWithUniqueField entityWithUniqueField = new EntityWithUniqueField();
        entityWithUniqueField.setValue(testValue);
        entityWithUniqueField = entityWithUniqueFieldRepository.save(entityWithUniqueField);

        assertThat(entityWithUniqueFieldRepository.findUnique(findByValue(testValue))).isEqualTo(entityWithUniqueField);
    }

    private SearchParameters<EntityWithUniqueField> findByValue(String value) {
        return new SearchBuilder<EntityWithUniqueField>() //
                .on(EntityWithUniqueField_.value).equalsTo(value) //
                .build();
    }

}
