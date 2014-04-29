package jpasearch.repository.util;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jpasearch.repository.query.SearchParameters;
import jpasearch.repository.query.selector.PropertySelector;
import jpasearch.repository.query.selector.Range;
import jpasearch.repository.query.selector.Selector;
import jpasearch.repository.query.selector.Selectors;
import jpasearch.repository.query.selector.TermSelector;

/**
 * @author speralta
 */
@Named
@Singleton
public class BySelectorUtil {

    private ByFullTextUtil byFullTextUtil;
    private ByRangeUtil byRangeUtil;
    private ByPropertySelectorUtil byPropertySelectorUtil;
    private JpaUtil jpaUtil;

    public <E> Predicate bySelectors(Root<E> root, CriteriaBuilder builder, SearchParameters<E> sp) {
        return bySelectors(root, builder, sp.getSelectors());
    }

    @SuppressWarnings("unchecked")
    private <E> Predicate bySelectors(Root<E> root, CriteriaBuilder builder, Selectors<E> selectors) {
        List<Predicate> predicates = new ArrayList<>();
        for (Selector<E, ?> selector : selectors.getSelectors()) {
            if (selector instanceof Selectors) {
                predicates.add(bySelectors(root, builder, (Selectors<E>) selector));
            } else if (selector instanceof PropertySelector) {
                predicates.add(byPropertySelectorUtil.byPropertySelectors(root, builder, (PropertySelector<? super E, ?>) selector));
            } else if (selector instanceof Range) {
                predicates.add(byRangeUtil.byRange(root, builder, (Range<E, ?>) selector));
            } else if (selector instanceof TermSelector) {
                predicates.add(byFullTextUtil.byFullText(root, builder, (TermSelector<E>) selector));
            }
        }
        if (selectors.isAndMode()) {
            return jpaUtil.andPredicate(builder, predicates);
        } else {
            return jpaUtil.orPredicate(builder, predicates);
        }
    }

    @Inject
    public void setByFullTextUtil(ByFullTextUtil byFullTextUtil) {
        this.byFullTextUtil = byFullTextUtil;
    }

    @Inject
    public void setByRangeUtil(ByRangeUtil byRangeUtil) {
        this.byRangeUtil = byRangeUtil;
    }

    @Inject
    public void setByPropertySelectorUtil(ByPropertySelectorUtil byPropertySelectorUtil) {
        this.byPropertySelectorUtil = byPropertySelectorUtil;
    }

    @Inject
    public void setJpaUtil(JpaUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

}
