package jpasearch.repository.query.selector;

import java.io.Serializable;

/**
 * @author speralta
 */
public interface Selector<FROM, S extends Selector<FROM, S>> extends Serializable {

    S copy();

}
