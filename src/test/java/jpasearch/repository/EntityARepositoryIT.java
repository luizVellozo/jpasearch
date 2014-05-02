package jpasearch.repository;

import jpasearch.TestApplication;
import jpasearch.domain.EntityA;
import jpasearch.domain.EntityA_;
import jpasearch.repository.query.SearchBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author speralta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class EntityARepositoryIT {

    @Inject
    private EntityARepository entityARepository;

    @Inject
    private Inserter inserter;

    @Test
    public void test() {
        EntityA entityA = new EntityA();
        entityA.setValue("test");
//        entityARepository.save(entityA);

        inserter.insertEntity(entityA);

        List<EntityA> founds = entityARepository.find(new SearchBuilder<EntityA>() //
                .on(EntityA_.value).equalsTo("test") //
                .build());

        assertThat(founds).hasSize(1);
        assertThat(founds.get(0)).isEqualTo(entityA);

    }

}
