package jpasearch.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.EntityB;
import jpasearch.domain.EntityB_;
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
public class EntityBRepositoryIT {

    @Inject
    private EntityBRepository entityBRepository;

    @Test
    public void test() {
        final String testValue = "test";

        assertThat(entityBRepository.findCount(findByValue(testValue))).isEqualTo(0);

        EntityB entityB = new EntityB();
        entityB.setValue(testValue);
        entityB = entityBRepository.save(entityB);

        List<EntityB> founds = entityBRepository.find(findByValue(testValue));

        assertThat(founds).containsExactly(entityB);
    }

    private SearchParameters<EntityB> findByValue(String value) {
        return new SearchBuilder<EntityB>() //
                .on(EntityB_.value).equalsTo(value) //
                .build();
    }

}
