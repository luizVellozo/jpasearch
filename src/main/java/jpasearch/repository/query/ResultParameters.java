package jpasearch.repository.query;

import java.io.Serializable;

/**
 * @author speralta
 */
public class ResultParameters<FROM, TO> implements Serializable {

    private static final long serialVersionUID = 201407292153L;

    private final Path<FROM, TO> path;
    private final Class<TO> to;

    public ResultParameters(Path<FROM, TO> path, Class<TO> to) {
        this.path = path;
        this.to = to;
    }

    public String getPath() {
        return path.getPath();
    }

    public Class<TO> getTo() {
        return to;
    }

}
