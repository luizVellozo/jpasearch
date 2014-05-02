package jpasearch.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author speralta
 */
@Entity
@Data
public class EntityA implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    private String value;

    @Override
    public boolean isIdSet() {
        return id != null;
    }


}
