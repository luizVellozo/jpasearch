package jpasearch.domain.comparable;

import java.util.Date;

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
public class EntityWithComparableField implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    private Date date;

    @Override
    public boolean isIdSet() {
        return id != null;
    }

}
