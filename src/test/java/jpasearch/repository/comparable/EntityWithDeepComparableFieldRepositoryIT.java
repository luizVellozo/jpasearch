package jpasearch.repository.comparable;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.comparable.EntityWithComparableField;
import jpasearch.domain.comparable.EntityWithComparableField_;
import jpasearch.domain.comparable.EntityWithDeepComparableField;
import jpasearch.domain.comparable.EntityWithDeepComparableField_;
import jpasearch.repository.query.SearchParameters;
import jpasearch.repository.query.builder.SearchBuilder;

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
public class EntityWithDeepComparableFieldRepositoryIT {

    @Inject
    private EntityWithDeepComparableFieldRepository entityWithDeepComparableFieldRepository;

    @Test
    public void test() {
        final Date testValue = new Date();

        assertThat(entityWithDeepComparableFieldRepository.findCount(findByRange(testValue))).isEqualTo(0);

        EntityWithDeepComparableField entityWithDeepComparableField = new EntityWithDeepComparableField();
        EntityWithComparableField entityWithComparableField = new EntityWithComparableField();
        entityWithComparableField.setDate(testValue);
        entityWithDeepComparableField.setEntityWithComparableField(entityWithComparableField);
        entityWithDeepComparableField = entityWithDeepComparableFieldRepository.save(entityWithDeepComparableField);

        assertThat(entityWithDeepComparableFieldRepository.find(findByRange(testValue))).containsExactly(entityWithDeepComparableField);
    }

    private SearchParameters<EntityWithDeepComparableField> findByRange(Date value) {
        Date from = new Date();
        from.setTime(value.getTime() - 10000);
        Date to = new Date();
        to.setTime(value.getTime() + 10000);
        return new SearchBuilder<EntityWithDeepComparableField>() //
                .range(EntityWithDeepComparableField_.entityWithComparableField) //
                .finallyOn(EntityWithComparableField_.date).between(from, to) //
                .build();
    }

}
