package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;

/**
 * @author speralta
 */
public abstract class AbstractPathBuilder<PARENT, F, FROM, TO> {
    private final PARENT parent;

    private final Path<F, TO> path;

    public AbstractPathBuilder(PARENT parent, Path<F, TO> path) {
        this.parent = parent;
        this.path = path;
    }

    public AbstractPathBuilder(PARENT parent, SingularAttribute<? super F, TO> attribute) {
        this.parent = parent;
        this.path = new Path<F, TO>(attribute);
    }

    public AbstractPathBuilder(PARENT parent, PluralAttribute<? super F, ?, TO> attribute) {
        this.parent = parent;
        this.path = new Path<F, TO>(attribute);
    }

    protected <E> AbstractPathBuilder(PARENT parent, AbstractPathBuilder<PARENT, F, E, FROM> pathBuilder, SingularAttribute<? super FROM, TO> attribute) {
        this.parent = parent;
        this.path = pathBuilder.path.add(attribute);
    }

    protected <E> AbstractPathBuilder(PARENT parent, AbstractPathBuilder<PARENT, F, E, FROM> pathBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        this.parent = parent;
        this.path = pathBuilder.path.add(attribute);
    }

    protected PARENT getParent() {
        return parent;
    }

    protected Path<F, TO> getPath() {
        return path;
    }

}
