package jpasearch.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.simple.EntityWithIntegerId;
import jpasearch.domain.simple.EntityWithIntegerId_;
import jpasearch.repository.query.SearchBuilder;
import jpasearch.repository.query.SearchParameters;
import jpasearch.repository.simple.EntityWithIntegerIdRepository;

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
public class JpaGenericRepositoryIT {

    @Inject
    private EntityWithIntegerIdRepository entityWithIntegerIdRepository;

    @Test
    public void test_findProperty() {
        final String testValue = "test";

        assertThat(entityWithIntegerIdRepository.findPropertyCount(findByValue(testValue), EntityWithIntegerId_.value)).isEqualTo(0);

        EntityWithIntegerId entityWithIntegerId = new EntityWithIntegerId();
        entityWithIntegerId.setValue(testValue);
        entityWithIntegerId = entityWithIntegerIdRepository.save(entityWithIntegerId);

        assertThat(entityWithIntegerIdRepository.findProperty(String.class, findByValue(testValue), EntityWithIntegerId_.value)).containsExactly(testValue);
    }

    private SearchParameters<EntityWithIntegerId> findByValue(String value) {
        return new SearchBuilder<EntityWithIntegerId>() //
                .on(EntityWithIntegerId_.value).equalsTo(value) //
                .build();
    }

}
