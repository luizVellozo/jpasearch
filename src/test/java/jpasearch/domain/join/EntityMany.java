package jpasearch.domain.join;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import jpasearch.domain.Identifiable;
import lombok.Data;

/**
 * @author speralta
 */
@Entity
@Data
public class EntityMany implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    private EntityOne entityOne;

    @Override
    public boolean isIdSet() {
        return id != null;
    }

}
