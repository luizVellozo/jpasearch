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
                .between(from, to, EntityWithComparableField_.date) //
                .build();
    }

}
