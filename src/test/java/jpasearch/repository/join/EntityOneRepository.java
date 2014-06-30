package jpasearch.repository.join;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.join.EntityOne;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityOneRepository extends JpaSimpleRepository<EntityOne, Integer> {

    public EntityOneRepository() {
        super(EntityOne.class);
    }

}
