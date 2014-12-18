package jpasearch.repository.query.selector;

import java.util.List;

import jpasearch.repository.query.Path;

/**
 * @author speralta
 */
public class StringTermSelector<FROM> extends TermSelector<FROM, String> {

    private static final long serialVersionUID = 201412151125L;

    public StringTermSelector(List<Path<FROM, ?>> paths) {
        super(paths);
    }

    public StringTermSelector(Path<FROM, ?> path) {
        super(path);
    }

}