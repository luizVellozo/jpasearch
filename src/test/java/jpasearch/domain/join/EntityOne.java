package jpasearch.domain.join;

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
public class EntityOne implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    private String value;

    @Override
    public boolean isIdSet() {
        return id != null;
    }

}

