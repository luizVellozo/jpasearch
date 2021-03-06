package jpasearch.repository.fulltext;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.fulltext.EntityWithFullText;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityWithFullTextRepository extends JpaSimpleRepository<EntityWithFullText, Integer> {

    public EntityWithFullTextRepository() {
        super(EntityWithFullText.class);
    }

}
