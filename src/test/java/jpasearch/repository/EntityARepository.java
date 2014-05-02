package jpasearch.repository;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.EntityA;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityARepository extends JpaSimpleRepository<Integer, EntityA> {

    public EntityARepository() {
        super(EntityA.class);
    }

}
