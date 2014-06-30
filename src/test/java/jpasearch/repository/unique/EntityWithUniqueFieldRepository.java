package jpasearch.repository.unique;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.unique.EntityWithUniqueField;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityWithUniqueFieldRepository extends JpaSimpleRepository<EntityWithUniqueField, Integer> {

    public EntityWithUniqueFieldRepository() {
        super(EntityWithUniqueField.class);
    }

}
