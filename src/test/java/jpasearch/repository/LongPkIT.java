package jpasearch.repository;

import jpasearch.domain.BaseEntity;
import jpasearch.domain.EntityA;
import jpasearch.domain.Identifiable;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Related to https://github.com/jpasearch/jpasearch/issues/2 : if it compiles, the issue is fixed!
 * User: blep
 */
public class LongPkIT {

    @Data
    public static class A extends BaseEntity<Long>{
        private String name;

    }

    public class ARepository extends JpaSimpleRepository<Long,A> {

        public ARepository() {
            super(A.class);
        }

    }

}
