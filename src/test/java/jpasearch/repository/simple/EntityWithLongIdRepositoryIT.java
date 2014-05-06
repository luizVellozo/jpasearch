package jpasearch.repository.simple;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.simple.EntityWithLongId;
import jpasearch.domain.simple.EntityWithLongId_;
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
public class EntityWithLongIdRepositoryIT {

    @Inject
    private EntityWithLongIdRepository entityWithLongIdRepository;

    @Test
    public void test() {
        final String testValue = "test";

        assertThat(entityWithLongIdRepository.findCount(findByValue(testValue))).isEqualTo(0);

        EntityWithLongId entityWithLongId = new EntityWithLongId();
        entityWithLongId.setValue(testValue);
        entityWithLongId = entityWithLongIdRepository.save(entityWithLongId);

        assertThat(entityWithLongIdRepository.find(findByValue(testValue))).containsExactly(entityWithLongId);
    }

    private SearchParameters<EntityWithLongId> findByValue(String value) {
        return new SearchBuilder<EntityWithLongId>() //
                .on(EntityWithLongId_.value).equalsTo(value) //
                .build();
    }

}
