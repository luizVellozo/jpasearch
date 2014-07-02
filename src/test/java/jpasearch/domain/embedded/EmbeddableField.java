package jpasearch.domain.embedded;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * @author speralta
 */
@Embeddable
@Data
public class EmbeddableField {

    @ManyToOne(cascade = CascadeType.ALL)
    private EntityEmbedded entityEmbedded;

}
