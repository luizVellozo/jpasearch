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
public class EntityWithUniqueFieldRepository extends JpaSimpleRepository<Integer, EntityWithUniqueField> {

    public EntityWithUniqueFieldRepository() {
        super(EntityWithUniqueField.class);
    }

}
