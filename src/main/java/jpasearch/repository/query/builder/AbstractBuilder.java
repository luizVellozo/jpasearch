package jpasearch.repository.query.builder;

/**
 * @author speralta
 */
public abstract class AbstractBuilder<PARENT> {

    private final PARENT parent;

    public AbstractBuilder(PARENT parent) {
        this.parent = parent;
    }

    protected final PARENT toParent() {
        return parent;
    }

}
