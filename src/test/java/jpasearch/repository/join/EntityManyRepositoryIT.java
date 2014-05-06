package jpasearch.repository.join;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.join.EntityMany;
import jpasearch.domain.join.EntityMany_;
import jpasearch.domain.join.EntityOne;
import jpasearch.domain.join.EntityOne_;
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

}
