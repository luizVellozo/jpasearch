package jpasearch.repository;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.EntityB;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityBRepository extends JpaSimpleRepository<Long, EntityB> {

    public EntityBRepository() {
        super(EntityB.class);
    }

}
