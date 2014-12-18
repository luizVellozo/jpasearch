package jpasearch.repository.query.selector;

import java.util.List;

import jpasearch.repository.query.Path;

/**
 * @author speralta
 */
public class ObjectTermSelector<FROM> extends TermSelector<FROM, Object> {

    private static final long serialVersionUID = 201412151125L;

    public ObjectTermSelector(List<Path<FROM, ?>> paths) {
        super(paths);
    }

    public ObjectTermSelector(Path<FROM, ?> path) {
        super(path);
    }

}
