package jpasearch.repository;

import jpasearch.domain.Identifiable;

import java.io.Serializable;

/**
 * @author speralta
 */
public interface SimpleRepository<PK extends Serializable, E extends Identifiable<PK>> extends GenericRepository<E, PK> {

    /**
     * @param id
     *            the entity id
     * @return the entity matching the id
     */
    E getById(PK id);

    /**
     * @param entity
     *            the entity to save
     * @return the saved entity
     */
    E save(E entity);

    /**
     * @param entity
     *            the entity to delete
     */
    void delete(E entity);
}
