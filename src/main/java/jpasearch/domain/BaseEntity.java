package jpasearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * User: blep
 * Date: 02/05/14
 * Time: 20:13
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity<PK extends Serializable> implements Identifiable<PK> {
    @Id
    @GeneratedValue
    private PK id;


    @Override
    public boolean isIdSet() {
        return id != null;
    }
}
