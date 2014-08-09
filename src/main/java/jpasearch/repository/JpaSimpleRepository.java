package jpasearch.repository;

import java.io.Serializable;

import jpasearch.domain.Identifiable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author speralta
 */
public abstract class JpaSimpleRepository<E extends Identifiable<PK>, PK extends Serializable> extends JpaGenericRepository<E, PK> implements SimpleRepository<E, PK> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public JpaSimpleRepository(Class<E> type) {
        super(type);
    }

    @Override
    public final E getById(PK id) {
        return getEntityManager().find(getType(), id);
    }

    @Override
    public final E save(E entity) {
        E result = getEntityManager().merge(entity);
        logger.debug("Entity merged {}.", result);
        postSave(result);
        return result;
    }

    @Override
    public final void delete(E entity) {
        E toRemove = getEntityManager().find(getType(), entity.getId());
        getEntityManager().remove(toRemove);
        logger.debug("Entity removed {}.", toRemove);
        postDelete(toRemove);
    }

    // Could be overriden

    protected void postSave(E entity) {

    }

    protected void postDelete(E entity) {

    }
}
