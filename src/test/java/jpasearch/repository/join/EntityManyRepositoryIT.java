package jpasearch.repository.join;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.join.EntityMany;
import jpasearch.domain.join.EntityMany_;
import jpasearch.domain.join.EntityOne;
import jpasearch.domain.join.EntityOne_;
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
public class EntityManyRepositoryIT {

    @Inject
    private EntityManyRepository entityManyRepository;

    @Inject
    private EntityOneRepository entityOneRepository;

    @Test
    public void findByDeepString() {
        final String testValue = "test";

        assertThat(entityManyRepository.findCount(findByValue(testValue))).isEqualTo(0);

        EntityOne entityOne = new EntityOne();
        entityOne.setValue(testValue);
        EntityMany entityMany = new EntityMany();
        entityMany.setEntityOne(entityOne);
        entityMany = entityManyRepository.save(entityMany);

        assertThat(entityManyRepository.find(findByValue(testValue))).containsExactly(entityMany);
    }

    private SearchParameters<EntityMany> findByValue(String value) {
        return new SearchBuilder<EntityMany>() //
                .on(EntityMany_.entityOne).to(EntityOne_.value).equalsTo(value) //
                .build();
    }

    @Test
    public void findByEntityOne() {
        EntityOne entityOne = new EntityOne();
        entityOne.setValue("test");
        entityOne = entityOneRepository.save(entityOne);

        assertThat(entityManyRepository.findCount(findByEntityOne(entityOne))).isEqualTo(0);

        EntityMany entityMany = new EntityMany();
        entityMany.setEntityOne(entityOne);
        entityMany = entityManyRepository.save(entityMany);

        assertThat(entityManyRepository.find(findByEntityOne(entityOne))).containsExactly(entityMany);
    }

    private SearchParameters<EntityMany> findByEntityOne(EntityOne entityOne) {
        return new SearchBuilder<EntityMany>() //
                .on(EntityMany_.entityOne).equalsTo(entityOne) //
                .build();
    }

    @Test
    public void orderByDeepString() {
        assertThat(entityManyRepository.findCount(orderByValue())).isEqualTo(0);

        EntityOne entityOne1 = new EntityOne();
        entityOne1.setValue("test1");
        EntityMany entityMany1 = new EntityMany();
        entityMany1.setEntityOne(entityOne1);
        entityMany1 = entityManyRepository.save(entityMany1);

        EntityOne entityOne2 = new EntityOne();
        entityOne2.setValue("test2");
        EntityMany entityMany2 = new EntityMany();
        entityMany2.setEntityOne(entityOne2);
        entityMany2 = entityManyRepository.save(entityMany2);

        assertThat(entityManyRepository.find(orderByValue())).containsExactly(entityMany2, entityMany1);
    }

    private SearchParameters<EntityMany> orderByValue() {
        return new SearchBuilder<EntityMany>() //
                .orderBy(EntityMany_.entityOne).and(EntityOne_.value).desc().build();
    }

}
