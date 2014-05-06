package jpasearch.repository.join;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.domain.join.EntityMany;
import jpasearch.repository.JpaSimpleRepository;

/**
 * @author speralta
 */
@Named
@Singleton
public class EntityManyRepository extends JpaSimpleRepository<Integer, EntityMany> {

    public EntityManyRepository() {
        super(EntityMany.class);
    }

}
