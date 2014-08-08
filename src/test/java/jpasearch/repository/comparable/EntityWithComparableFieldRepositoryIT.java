package jpasearch.repository.comparable;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.comparable.EntityWithComparableField;
import jpasearch.domain.comparable.EntityWithComparableField_;
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
public class EntityWithComparableFieldRepositoryIT {

    @Inject
    private EntityWithComparableFieldRepository entityWithComparableFieldRepository;

    @Test
    public void test() {
        final Date testValue = new Date();

        assertThat(entityWithComparableFieldRepository.findCount(findByRange(testValue))).isEqualTo(0);

        EntityWithComparableField entityWithComparableField = new EntityWithComparableField();
        entityWithComparableField.setDate(testValue);
        entityWithComparableField = entityWithComparableFieldRepository.save(entityWithComparableField);

        assertThat(entityWithComparableFieldRepository.find(findByRange(testValue))).containsExactly(entityWithComparableField);
    }

    private SearchParameters<EntityWithComparableField> findByRange(Date value) {
        Date from = new Date();
        from.setTime(value.getTime() - 10000);
        Date to = new Date();
        to.setTime(value.getTime() + 10000);
        return new SearchBuilder<EntityWithComparableField>() //
                .rangeOn(EntityWithComparableField_.date).between(from, to) //
                .build();
    }

    @Test
    public void test_includeNull() {
        final Date testValue = new Date();

        assertThat(entityWithComparableFieldRepository.findCount(findByRangeIncludingNull(testValue))).isEqualTo(0);

        EntityWithComparableField entityWithComparableField = new EntityWithComparableField();
        entityWithComparableField.setDate(testValue);
        entityWithComparableField = entityWithComparableFieldRepository.save(entityWithComparableField);
        EntityWithComparableField entityWithNullField = new EntityWithComparableField();
        entityWithNullField.setDate(null);
        entityWithNullField = entityWithComparableFieldRepository.save(entityWithNullField);

        assertThat(entityWithComparableFieldRepository.find(findByRange(testValue))).containsExactly(entityWithComparableField);
        assertThat(entityWithComparableFieldRepository.find(findByRangeIncludingNull(testValue))).containsExactly(entityWithComparableField, entityWithNullField);
    }

    private SearchParameters<EntityWithComparableField> findByRangeIncludingNull(Date value) {
        Date from = new Date();
        from.setTime(value.getTime() - 10000);
        Date to = new Date();
        to.setTime(value.getTime() + 10000);
        return new SearchBuilder<EntityWithComparableField>() //
                .rangeOn(EntityWithComparableField_.date).includingNull().between(from, to) //
                .build();
    }

    @Test
    public void test_excludeBounds() {
        final Date testValue = new Date();
        final Date from = new Date();
        from.setTime(testValue.getTime() - 1000);
        final Date to = new Date();
        to.setTime(testValue.getTime() + 1000);

        assertThat(entityWithComparableFieldRepository.findCount(findByRangeExcludingBounds(from, to))).isEqualTo(0);

        EntityWithComparableField entityWithComparableField = new EntityWithComparableField();
        entityWithComparableField.setDate(testValue);
        entityWithComparableField = entityWithComparableFieldRepository.save(entityWithComparableField);
        EntityWithComparableField entityFrom = new EntityWithComparableField();
        entityFrom.setDate(from);
        entityFrom = entityWithComparableFieldRepository.save(entityFrom);
        EntityWithComparableField entityTo = new EntityWithComparableField();
        entityTo.setDate(to);
        entityTo = entityWithComparableFieldRepository.save(entityTo);

        assertThat(entityWithComparableFieldRepository.find(findByRange(testValue))).containsExactly(entityWithComparableField, entityFrom, entityTo);
        assertThat(entityWithComparableFieldRepository.find(findByRangeExcludingBounds(from, to))).containsExactly(entityWithComparableField);
    }

    private SearchParameters<EntityWithComparableField> findByRangeExcludingBounds(Date from, Date to) {
        return new SearchBuilder<EntityWithComparableField>() //
                .rangeOn(EntityWithComparableField_.date).excludingBounds().between(from, to) //
                .build();
    }

}
