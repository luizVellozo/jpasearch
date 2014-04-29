package jpasearch.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author speralta
 */
@StaticMetamodel(EntityA.class)
public abstract class EntityA_ {

    public static volatile SingularAttribute<EntityA, Integer> id;
    public static volatile SingularAttribute<EntityA, String> value;

}
