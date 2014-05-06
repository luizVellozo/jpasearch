package jpasearch.repository.util;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.domain.Identifiable;
import jpasearch.repository.query.SearchMode;
import jpasearch.repository.query.SearchParameters;

@Named
@Singleton
public class JpaUtil {

    private MetamodelUtil metamodelUtil;

    public Predicate andPredicate(CriteriaBuilder builder, Predicate... predicatesNullAllowed) {
        return andPredicate(builder, Arrays.asList(predicatesNullAllowed));
    }

    public Predicate orPredicate(CriteriaBuilder builder, Predicate... predicatesNullAllowed) {
        return orPredicate(builder, Arrays.asList(predicatesNullAllowed));
    }

    public Predicate andPredicate(CriteriaBuilder builder, Iterable<Predicate> predicatesNullAllowed) {
        List<Predicate> predicates = newArrayList(filter(predicatesNullAllowed, notNull()));
        if ((predicates == null) || predicates.isEmpty()) {
            return null;
        } else if (predicates.size() == 1) {
            return predicates.get(0);
        } else {
            return builder.and(toArray(predicates, Predicate.class));
        }
    }

    public Predicate orPredicate(CriteriaBuilder builder, Iterable<Predicate> predicatesNullAllowed) {
        List<Predicate> predicates = newArrayList(filter(predicatesNullAllowed, notNull()));
        if ((predicates == null) || predicates.isEmpty()) {
            return null;
        } else if (predicates.size() == 1) {
            return predicates.get(0);
        } else {
            return builder.or(toArray(predicates, Predicate.class));
        }
    }

    public <E> Predicate stringPredicate(Expression<String> path, Object attrValue, SearchMode searchMode, boolean caseSensitive, CriteriaBuilder builder) {
        if (!caseSensitive) {
            path = builder.lower(path);
            attrValue = ((String) attrValue).toLowerCase(Locale.FRANCE);
        }

        switch (searchMode) {
        case EQUALS:
            return builder.equal(path, attrValue);
        case ENDING_LIKE:
            return builder.like(path, "%" + attrValue);
        case STARTING_LIKE:
            return builder.like(path, attrValue + "%");
        case ANYWHERE:
            return builder.like(path, "%" + attrValue + "%");
        case LIKE:
            // assume user provide the wild cards
            return builder.like(path, (String) attrValue);
        default:
            throw new IllegalStateException("expecting a search mode!");
        }
    }

    public <E, F> Path<F> getPath(Root<E> root, String path) {
        return getPath(root, metamodelUtil.toAttributes(root.getJavaType(), path));
    }

    @SuppressWarnings("unchecked")
    public <E, F> Path<F> getPath(Root<E> root, List<Attribute<?, ?>> attributes) {
        Path<?> path = root;
        for (Attribute<?, ?> attribute : attributes) {
            boolean found = false;
            if (path instanceof FetchParent) {
                for (Fetch<E, ?> fetch : ((FetchParent<?, E>) path).getFetches()) {
                    if (attribute.getName().equals(fetch.getAttribute().getName()) && (fetch instanceof Join<?, ?>)) {
                        path = (Join<E, ?>) fetch;
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                if ((attributes.indexOf(attribute) != (attributes.size() - 1)) && (attribute instanceof Bindable)
                        && Identifiable.class.isAssignableFrom(((Bindable<?>) attribute).getBindableJavaType())) {
                    path = ((From<?, ?>) path).join(attribute.getName(), JoinType.LEFT);
                } else {
                    path = path.get(attribute.getName());
                }
            }
        }
        return (Path<F>) path;
    }

    public void applyPagination(Query query, SearchParameters<?> sp) {
        if (sp.getFirstResult() > 0) {
            query.setFirstResult(sp.getFirstResult());
        }
        if (sp.getMaxResults() > 0) {
            query.setMaxResults(sp.getMaxResults());
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <E> void fetches(SearchParameters<E> sp, Root<E> root) {
        for (jpasearch.repository.query.Path<E, ?> path : sp.getFetches()) {
            FetchParent<?, ?> from = root;
            for (Attribute<?, ?> arg : metamodelUtil.toAttributes(root.getJavaType(), path.getPath())) {
                boolean found = false;
                for (Fetch<?, ?> fetch : from.getFetches()) {
                    if (arg.equals(fetch.getAttribute())) {
                        from = fetch;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    if (arg instanceof PluralAttribute) {
                        from = from.fetch((PluralAttribute) arg, JoinType.LEFT);
                    } else {
                        from = from.fetch((SingularAttribute) arg, JoinType.LEFT);
                    }
                }
            }
        }
    }

    @Inject
    public void setMetamodelUtil(MetamodelUtil metamodelUtil) {
        this.metamodelUtil = metamodelUtil;
    }

}