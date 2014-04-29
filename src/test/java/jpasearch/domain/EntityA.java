package jpasearch.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author speralta
 */
@Entity
public class EntityA implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    private String value;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean isIdSet() {
        return id != null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
