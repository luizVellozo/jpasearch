package jpasearch.repository.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jpasearch.domain.Identifiable;
import jpasearch.repository.query.selector.PropertySelector;

/**
 * Helper to create a predicate out of {@link PropertySelector}s.
 */
@Named
@Singleton
public class ByPropertySelectorUtil {

    private JpaUtil jpaUtil;

    private MetamodelUtil metamodelUtil;

    @SuppressWarnings("unchecked")
    public <E> Predicate byPropertySelectors(Root<E> root, CriteriaBuilder builder, PropertySelector<? super E, ?> propertySelector) {
        List<Predicate> predicates = new ArrayList<>();
        if (metamodelUtil.isBoolean(root.getJavaType(), propertySelector.getPath())) {
            byBooleanSelector(root, builder, predicates, (PropertySelector<? super E, Boolean>) propertySelector);
        } else if (metamodelUtil.isString(root.getJavaType(), propertySelector.getPath())) {
            byStringSelector(root, builder, predicates, (PropertySelector<? super E, String>) propertySelector);
        } else {
            byObjectSelector(root, builder, predicates, propertySelector);
        }
        if (propertySelector.isOrMode()) {
            return jpaUtil.orPredicate(builder, predicates);
        } else {
            return jpaUtil.andPredicate(builder, predicates);
        }
    }

    private <E> void byBooleanSelector(Root<E> root, CriteriaBuilder builder, List<Predicate> predicates, PropertySelector<? super E, Boolean> selector) {
        if (selector.isNotEmpty()) {
            List<Predicate> selectorPredicates = new ArrayList<>();

            for (Boolean selection : selector.getSelected()) {
                Path<Boolean> path = jpaUtil.getPath(root, selector.getPath());
                if (selection == null) {
                    selectorPredicates.add(builder.isNull(path));
                } else {
                    selectorPredicates.add(selection ? builder.isTrue(path) : builder.isFalse(path));
                }
            }
            Predicate predicate;
            if (selector.isOrMode()) {
                predicate = jpaUtil.orPredicate(builder, selectorPredicates);
                if (selector.isNotMode()) {
                    predicate = builder.not(predicate);
                }
            } else {
                predicate = jpaUtil.andPredicate(builder, selectorPredicates);
                if (selector.isNotMode()) {
                    predicate = builder.not(predicate);
                }
            }
            predicates.add(predicate);
        }
    }

    private <E> void byStringSelector(Root<E> root, CriteriaBuilder builder, List<Predicate> predicates, PropertySelector<? super E, String> selector) {
        if (selector.isNotEmpty()) {
            Path<String> path = null;
            List<Predicate> selectorPredicates = new ArrayList<>();
            List<String> selected = selector.getSelected();
            if (selector.getSelected().contains(null)) {
                path = jpaUtil.getPath(root, selector.getPath());
                selected = new ArrayList<>(selector.getSelected());
                selected.remove(null);
                selectorPredicates.add(builder.isNull(path));
            }
            Predicate predicate;
            if (selector.isOrMode()) {
                // re-use created pat if it exists in 'or' mode : only one join
                if (path == null) {
                    path = jpaUtil.getPath(root, selector.getPath());
                }
                for (String selection : selected) {
                    selectorPredicates.add(jpaUtil.stringPredicate(path, selection, selector.getSearchMode(), selector.isCaseSensitive(), builder));
                }
                predicate = jpaUtil.orPredicate(builder, selectorPredicates);
                if (selector.isNotMode()) {
                    predicate = builder.not(predicate);
                }
            } else {
                for (String selection : selected) {
                    // explicitly create new path for new join in 'and' mode
                    path = jpaUtil.getPath(root, selector.getPath());
                    selectorPredicates.add(jpaUtil.stringPredicate(path, selection, selector.getSearchMode(), selector.isCaseSensitive(), builder));
                }
                predicate = jpaUtil.andPredicate(builder, selectorPredicates);
                if (selector.isNotMode()) {
                    predicate = builder.not(predicate);
                }
            }
            predicates.add(predicate);
        }
    }

    private <E> void byObjectSelector(Root<E> root, CriteriaBuilder builder, List<Predicate> predicates, PropertySelector<? super E, ?> selector) {
        if (selector.isNotEmpty()) {
            if (selector.isOrMode()) {
                byObjectOrModeSelector(root, builder, predicates, selector);
            } else {
                byObjectAndModeSelector(root, builder, predicates, selector);
            }
        } else if (selector.isNotIncludingNullSet()) {
            predicates.add(builder.isNotNull(jpaUtil.getPath(root, selector.getPath())));
        }
    }

    private <E> void byObjectOrModeSelector(Root<E> root, CriteriaBuilder builder, List<Predicate> predicates, PropertySelector<? super E, ?> selector) {
        List<Predicate> selectorPredicates = new ArrayList<>();
        Path<?> path = jpaUtil.getPath(root, selector.getPath());
        List<?> selected = selector.getSelected();
        if (selector.getSelected().contains(null)) {
            selected = new ArrayList<>(selector.getSelected());
            selected.remove(null);
            selectorPredicates.add(builder.isNull(path));
        }
        if (!selected.isEmpty()) {
            if (selected.get(0) instanceof Identifiable) {
                List<Serializable> ids = new ArrayList<>();
                for (Object selection : selected) {
                    ids.add(((Identifiable<?>) selection).getId());
                }
                selectorPredicates.add(path.get("id").in(ids));
            } else {
                selectorPredicates.add(path.in(selected));
            }
        }
        Predicate predicate = jpaUtil.orPredicate(builder, selectorPredicates);
        if (selector.isNotMode()) {
            predicate = builder.not(predicate);
        }
        predicates.add(predicate);
    }

    private <E> void byObjectAndModeSelector(Root<E> root, CriteriaBuilder builder, List<Predicate> predicates, PropertySelector<? super E, ?> selector) {
        List<Predicate> selectorPredicates = new ArrayList<>();
        List<?> selected = selector.getSelected();
        if (selector.getSelected().contains(null)) {
            selected = new ArrayList<>(selector.getSelected());
            selected.remove(null);
            selectorPredicates.add(builder.isNull(jpaUtil.getPath(root, selector.getPath())));
        }
        for (Object selection : selector.getSelected()) {
            Path<?> path = jpaUtil.getPath(root, selector.getPath());
            if (selection instanceof Identifiable) {
                selectorPredicates.add(builder.equal(path.get("id"), ((Identifiable<?>) selection).getId()));
            } else {
                selectorPredicates.add(builder.equal(path, selection));
            }
        }
        Predicate predicate = jpaUtil.andPredicate(builder, selectorPredicates);
        if (selector.isNotMode()) {
            predicate = builder.not(predicate);
        }
        predicates.add(predicate);
    }

    @Inject
    public void setJpaUtil(JpaUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Inject
    public void setMetamodelUtil(MetamodelUtil metamodelUtil) {
        this.metamodelUtil = metamodelUtil;
    }

}