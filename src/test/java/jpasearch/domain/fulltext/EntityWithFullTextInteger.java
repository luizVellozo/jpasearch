package jpasearch.domain.fulltext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import jpasearch.domain.Identifiable;
import lombok.Data;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * @author speralta
 */
@Entity
@Data
@Indexed
public class EntityWithFullTextInteger implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @Field
    private Integer value;

    @Override
    public boolean isIdSet() {
        return id != null;
    }

}
