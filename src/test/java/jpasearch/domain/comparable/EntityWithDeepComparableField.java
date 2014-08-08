package jpasearch.domain.comparable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class EntityWithDeepComparableField implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EntityWithComparableField entityWithComparableField;

    @Override
    public boolean isIdSet() {
        return id != null;
    }

}
