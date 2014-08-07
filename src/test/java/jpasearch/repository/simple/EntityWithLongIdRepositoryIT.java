package jpasearch.repository.simple;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.simple.EntityWithLongId;
import jpasearch.domain.simple.EntityWithLongId_;
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

    @Test
    public void junction_test() {
        final String testValue1 = "test1";
        final String testValue2 = "test2";

        assertThat(entityWithLongIdRepository.findCount(findByValues(testValue1, testValue2))).isEqualTo(0);

        EntityWithLongId entity1 = new EntityWithLongId();
        entity1.setValue(testValue1);
        entity1 = entityWithLongIdRepository.save(entity1);
        EntityWithLongId entity2 = new EntityWithLongId();
        entity2.setValue(testValue2);
        entity2 = entityWithLongIdRepository.save(entity2);
        EntityWithLongId entity3 = new EntityWithLongId();
        entity3.setValue("test");
        entity3 = entityWithLongIdRepository.save(entity3);

        assertThat(entityWithLongIdRepository.find(findByValues(testValue1, testValue2))).containsExactly(entity1, entity2);
    }

    private SearchParameters<EntityWithLongId> findByValues(String testValue1, String testValue2) {
        return new SearchBuilder<EntityWithLongId>().disjunction() //
                .on(EntityWithLongId_.value).equalsTo(testValue1) //
                .on(EntityWithLongId_.value).equalsTo(testValue2) //
                .conjunction() //
                .on(EntityWithLongId_.value).startingLike("te") //
                .on(EntityWithLongId_.value).not().endingLike("st") //
                .or().and().build();
    }

}
