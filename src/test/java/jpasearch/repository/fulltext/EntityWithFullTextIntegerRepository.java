package jpasearch.repository.fulltext;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.fulltext.EntityWithFullTextInteger;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityWithFullTextIntegerRepository extends JpaSimpleRepository<EntityWithFullTextInteger, Integer> {

    public EntityWithFullTextIntegerRepository() {
        super(EntityWithFullTextInteger.class);
    }

}
