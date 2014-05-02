package jpasearch.repository;

import java.io.Serializable;

import jpasearch.domain.Identifiable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author speralta
 */
public abstract class JpaSimpleRepository<PK extends Serializable, E extends Identifiable<PK>> extends JpaGenericRepository<E, PK> implements SimpleRepository<PK, E> {

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
        postSave(entity);
        return result;
    }

    @Override
    public final void delete(E entity) {
        E toRemove = getEntityManager().find(getType(), entity.getId());
        getEntityManager().remove(toRemove);
        logger.debug("Entity removed {}.", toRemove);
        postDelete(entity);
    }

    // Could be overriden

    protected void postSave(E entity) {

    }

    protected void postDelete(E entity) {

    }
}
