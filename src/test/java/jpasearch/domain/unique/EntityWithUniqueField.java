package jpasearch.domain.unique;

import javax.persistence.Column;
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
public class EntityWithUniqueField implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String value;

    @Override
    public boolean isIdSet() {
        return id != null;
    }

}
