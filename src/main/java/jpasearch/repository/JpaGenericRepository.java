package jpasearch.repository;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jpasearch.domain.Identifiable;
import jpasearch.repository.query.ResultParameters;
import jpasearch.repository.query.SearchParameters;
import jpasearch.repository.query.builder.SearchBuilder;
import jpasearch.repository.util.BySelectorUtil;
import jpasearch.repository.util.JpaUtil;
import jpasearch.repository.util.MetamodelUtil;
import jpasearch.repository.util.OrderByUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JPA 2 Generic DAO with find by example/range/pattern and CRUD support.
 */
public abstract class JpaGenericRepository<E extends Identifiable<PK>, PK extends Serializable> implements GenericRepository<E, PK> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private BySelectorUtil bySelectorUtil;
    private OrderByUtil orderByUtil;
    private JpaUtil jpaUtil;
    private MetamodelUtil metamodelUtil;

    private EntityManager entityManager;

    private final Class<E> type;
    protected String cacheRegion;

    /**
     * This constructor needs the real type of the generic type E so it can be
     * passed to the {@link EntityManager}.
     */
    public JpaGenericRepository(Class<E> type) {
        this.type = type;
    }

    public final Class<E> getType() {
        return type;
    }

    @Override
    public List<E> find(SearchParameters<E> sp) {
        checkNotNull(sp, "The searchParameters cannot be null");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = builder.createQuery(type);
        if (sp.isUseDistinct()) {
            criteriaQuery.distinct(true);
        }
        Root<E> root = criteriaQuery.from(type);

        // predicate
        Predicate predicate = getPredicate(criteriaQuery, root, builder, sp);
        if (predicate != null) {
            criteriaQuery = criteriaQuery.where(predicate);
        }

        // fetches
        jpaUtil.fetches(sp, root);

        // order by
        criteriaQuery.orderBy(orderByUtil.buildJpaOrders(sp.getOrders(), root, builder));

        TypedQuery<E> typedQuery = entityManager.createQuery(criteriaQuery);
        jpaUtil.applyPagination(typedQuery, sp);
        List<E> entities = typedQuery.getResultList();
        logger.trace("Returned {} elements for {}.", entities.size(), sp);

        return entities;
    }

    @Override
    public long findCount(SearchParameters<E> sp) {
        checkNotNull(sp, "The searchParameters cannot be null");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(type);

        if (sp.isUseDistinct()) {
            criteriaQuery = criteriaQuery.select(builder.countDistinct(root));
        } else {
            criteriaQuery = criteriaQuery.select(builder.count(root));
        }

        // predicate
        Predicate predicate = getPredicate(criteriaQuery, root, builder, sp);
        if (predicate != null) {
            criteriaQuery = criteriaQuery.where(predicate);
        }

        // construct order by to fetch or joins if needed
        orderByUtil.buildJpaOrders(sp.getOrders(), root, builder);

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getSingleResult().longValue();
    }

    @Override
    public <T> List<T> findProperty(SearchParameters<E> sp, ResultParameters<E, T> resultParameters) {
        checkNotNull(sp, "The searchParameters cannot be null");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(resultParameters.getTo());
        if (sp.isUseDistinct()) {
            criteriaQuery.distinct(true);
        }
        Root<E> root = criteriaQuery.from(type);
        criteriaQuery.select(jpaUtil.<E, T> getPath(root, resultParameters.getPath()));

        // predicate
        Predicate predicate = getPredicate(criteriaQuery, root, builder, sp);
        if (predicate != null) {
            criteriaQuery = criteriaQuery.where(predicate);
        }

        // fetches
        jpaUtil.fetches(sp, root);

        // order by
        criteriaQuery.orderBy(orderByUtil.buildJpaOrders(sp.getOrders(), root, builder));

        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        jpaUtil.applyPagination(typedQuery, sp);
        List<T> entities = typedQuery.getResultList();
        logger.debug("Returned {} elements for {}.", entities.size(), sp);

        return entities;
    }

    @Override
    public long findPropertyCount(SearchParameters<E> sp, ResultParameters<E, ?> resultParameters) {
        checkNotNull(sp, "The searchParameters cannot be null");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(type);
        Path<?> pathToCount = jpaUtil.getPath(root, resultParameters.getPath());

        if (sp.isUseDistinct()) {
            criteriaQuery = criteriaQuery.select(builder.countDistinct(pathToCount));
        } else {
            criteriaQuery = criteriaQuery.select(builder.count(pathToCount));
        }

        // predicate
        Predicate predicate = getPredicate(criteriaQuery, root, builder, sp);
        if (predicate != null) {
            criteriaQuery = criteriaQuery.where(predicate);
        }

        // construct order by to fetch or joins if needed
        orderByUtil.buildJpaOrders(sp.getOrders(), root, builder);

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getSingleResult().longValue();
    }

    @Override
    public E findUnique(SearchParameters<E> sp) {
        E result = findUniqueOrNone(sp);

        if (result == null) {
            throw new NoResultException("Developper: You expected 1 result but we found none !");
        }

        return result;
    }

    @Override
    public E findUniqueOrNone(SearchParameters<E> sp) {
        // this code is an optimization to prevent using a count
        List<E> results = find(new SearchBuilder<E>(sp).paginate(0, 2).build());

        if ((results == null) || results.isEmpty()) {
            return null;
        }

        if (results.size() > 1) {
            throw new NonUniqueResultException("Developper: You expected 1 result but we found more !");
        }

        return results.iterator().next();
    }

    protected <R> Predicate getPredicate(CriteriaQuery<?> criteriaQuery, Root<E> root, CriteriaBuilder builder, SearchParameters<E> sp) {
        return jpaUtil.andPredicate(builder, //
                bySearchPredicate(root, builder, sp), //
                byMandatoryPredicate(criteriaQuery, root, builder, sp));
    }

    protected <R> Predicate bySearchPredicate(Root<E> root, CriteriaBuilder builder, SearchParameters<E> sp) {
        return bySelectors(root, builder, sp);
    }

    protected Predicate bySelectors(Root<E> root, CriteriaBuilder builder, SearchParameters<E> sp) {
        return bySelectorUtil.bySelectors(root, builder, sp);
    }

    /**
     * You may override this method to add a Predicate to the default find
     * method.
     */
    protected <R> Predicate byMandatoryPredicate(CriteriaQuery<?> criteriaQuery, Root<E> root, CriteriaBuilder builder, SearchParameters<E> sp) {
        return null;
    }

    // BEANS METHODS

    protected final BySelectorUtil getBySelectorUtil() {
        return bySelectorUtil;
    }

    @Inject
    public final void setBySelectorUtil(BySelectorUtil bySelectorUtil) {
        this.bySelectorUtil = bySelectorUtil;
    }

    protected final EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public final void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected final OrderByUtil getOrderByUtil() {
        return orderByUtil;
    }

    @Inject
    public final void setOrderByUtil(OrderByUtil orderByUtil) {
        this.orderByUtil = orderByUtil;
    }

    protected final JpaUtil getJpaUtil() {
        return jpaUtil;
    }

    @Inject
    public final void setJpaUtil(JpaUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    protected final MetamodelUtil getMetamodelUtil() {
        return metamodelUtil;
    }

    @Inject
    public final void setMetamodelUtil(MetamodelUtil metamodelUtil) {
        this.metamodelUtil = metamodelUtil;
    }

}