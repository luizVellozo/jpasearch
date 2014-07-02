package jpasearch.domain.embedded;

import javax.persistence.Embedded;
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
public class EntityWithEmbeddedField implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @Embedded
    private EmbeddableField embeddableField;

    @Override
    public boolean isIdSet() {
        return id != null;
    }

}
