package jpasearch.repository.comparable;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.comparable.EntityWithComparableField;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityWithComparableFieldRepository extends JpaSimpleRepository<Integer, EntityWithComparableField> {

    public EntityWithComparableFieldRepository() {
        super(EntityWithComparableField.class);
    }

}
