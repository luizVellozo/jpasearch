package jpasearch.repository.embedded;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.embedded.EntityWithEmbeddedField;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityWithEmbeddedFieldRepository extends JpaSimpleRepository<EntityWithEmbeddedField, Integer> {

    public EntityWithEmbeddedFieldRepository() {
        super(EntityWithEmbeddedField.class);
    }

}
