package jpasearch.repository.comparable;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.comparable.EntityWithDeepComparableField;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityWithDeepComparableFieldRepository extends JpaSimpleRepository<EntityWithDeepComparableField, Integer> {

    public EntityWithDeepComparableFieldRepository() {
        super(EntityWithDeepComparableField.class);
    }

}
