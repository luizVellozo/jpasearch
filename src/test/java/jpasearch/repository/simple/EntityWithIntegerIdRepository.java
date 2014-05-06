package jpasearch.repository.simple;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.simple.EntityWithIntegerId;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityWithIntegerIdRepository extends JpaSimpleRepository<Integer, EntityWithIntegerId> {

    public EntityWithIntegerIdRepository() {
        super(EntityWithIntegerId.class);
    }

}
