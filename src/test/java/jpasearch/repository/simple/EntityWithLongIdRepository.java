package jpasearch.repository.simple;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.simple.EntityWithLongId;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityWithLongIdRepository extends JpaSimpleRepository<Long, EntityWithLongId> {

    public EntityWithLongIdRepository() {
        super(EntityWithLongId.class);
    }

}
