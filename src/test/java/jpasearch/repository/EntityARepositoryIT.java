package jpasearch.repository;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.EntityA;
import jpasearch.domain.EntityA_;
import jpasearch.repository.query.SearchBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author speralta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@Transactional
public class EntityARepositoryIT {

    @Inject
    private EntityARepository entityARepository;

    @Test
    @Rollback
    public void test() {
        EntityA entityA = new EntityA();
        entityA.setValue("test");
        entityARepository.save(entityA);

        entityARepository.find(new SearchBuilder<EntityA>() //
                .on(EntityA_.value).equalsTo("test") //
                .build());
    }

}
