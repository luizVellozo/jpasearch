package jpasearch.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import jpasearch.domain.Identifiable;
import jpasearch.repository.query.ResultParameters;
import jpasearch.repository.query.SearchParameters;

/**
 * @author speralta
 * 
 * @param <E>
 * @param <PK>
 */
public interface GenericRepository<E extends Identifiable<PK>, PK extends Serializable> {

    /**
     * Find and load a list of E instance.
     * 
     * @param searchParameters
     *            carries additional search information
     * @return the entities matching the search.
     */
    List<E> find(SearchParameters<E> searchParameters);

    /**
     * Count the number of E instances.
     * 
     * @param searchParameters
     *            carries additional search information
     * @return the number of entities matching the search.
     */
    long findCount(SearchParameters<E> searchParameters);

    /**
     * Find a list of E property.
     * 
     * @param searchParameters
     *            carries additional search information
     * @param resultParameters
     *            carries result information
     * @return the entities property matching the search.
     */
    <T> List<T> findProperty(SearchParameters<E> searchParameters, ResultParameters<E, T> resultParameters);

    /**
     * Count the number of instances of the path.
     * 
     * @param searchParameters
     *            carries additional search information
     * @param resultParameters
     *            carries result information
     * @return the number of entities matching the search.
     */
    long findPropertyCount(SearchParameters<E> searchParameters, ResultParameters<E, ?> resultParameters);

    /**
     * Find and load a unique E instance.
     * 
     * @param searchParameters
     *            carries additional search information
     * @return the entity matching the search or null.
     */
    E findUnique(SearchParameters<E> searchParameters);

    /**
     * We request at most 2, if there's more than one then we throw a
     * {@link NonUniqueResultException}
     * 
     * @param searchParameters
     *            carries additional search information
     * @return the entity matching the search.
     * @throws NonUniqueResultException
     */
    E findUniqueOrNone(SearchParameters<E> searchParameters);

}