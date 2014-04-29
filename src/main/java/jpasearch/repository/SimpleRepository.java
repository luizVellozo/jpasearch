package jpasearch.repository;

import jpasearch.domain.Identifiable;

/**
 * @author speralta
 */
public interface SimpleRepository<E extends Identifiable<Integer>> extends GenericRepository<E, Integer> {

    /**
     * @param id
     *            the entity id
     * @return the entity matching the id
     */
    E getById(Integer id);

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
