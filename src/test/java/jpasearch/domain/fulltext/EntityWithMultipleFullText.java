package jpasearch.domain.fulltext;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import jpasearch.domain.Identifiable;
import lombok.Data;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * @author speralta
 */
@Entity
@Data
@Indexed
public class EntityWithMultipleFullText implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @Field
    private String value1;

    @Field
    private String value2;

    @IndexedEmbedded
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EntityWithFullText entityWithFullText;

    @Override
    public boolean isIdSet() {
        return id != null;
    }

}
