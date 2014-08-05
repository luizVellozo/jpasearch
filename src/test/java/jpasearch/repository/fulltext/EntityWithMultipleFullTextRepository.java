package jpasearch.repository.fulltext;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.fulltext.EntityWithMultipleFullText;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityWithMultipleFullTextRepository extends JpaSimpleRepository<EntityWithMultipleFullText, Integer> {

    public EntityWithMultipleFullTextRepository() {
        super(EntityWithMultipleFullText.class);
    }

}
