package jpasearch.domain.simple;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import jpasearch.domain.Identifiable;
import lombok.Data;

/**
 * @author speralta
 */
@Entity
@Data
public class EntityWithLongId implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    private String value;

    @Override
    public boolean isIdSet() {
        return id != null;
    }

}
