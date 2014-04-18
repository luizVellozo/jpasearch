/*
 * Copyright 20143, jpasearch
 *
 * This file is part of jpasearch.
 *
 * jpasearch is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * jpasearch is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jpasearch. If not, see <http://www.gnu.org/licenses/>.
 */
package jpasearch.repository.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jpasearch.domain.Identifiable;
import jpasearch.repository.query.selector.TermSelector;

@Named
@Singleton
public class ByFullTextUtil {
    private HibernateSearchUtil hibernateSearchUtil;
    private JpaUtil jpaUtil;

    public <T> Predicate byFullText(Root<T> root, CriteriaBuilder builder, TermSelector<T> termSelector) {
        if (!termSelector.isNotEmpty()) {
            return null;
        }

        if (Identifiable.class.isAssignableFrom(root.getJavaType())) {
            return onIdentifiable(root, builder, termSelector);
        } else {
            return onOther(root, builder, termSelector);
        }

    }

    private <T> Predicate onOther(Root<T> root, CriteriaBuilder builder, TermSelector<T> termSelector) {
        List<? extends T> found = hibernateSearchUtil.find(root.getJavaType(), termSelector);
        if (found == null) {
            return null;
        } else if (found.isEmpty()) {
            return builder.disjunction();
        }

        List<Predicate> predicates = new ArrayList<>();
        for (T t : found) {
            predicates.add(builder.equal(root, t));
        }

        return jpaUtil.orPredicate(builder, predicates);
    }

    private <T> Predicate onIdentifiable(Root<T> root, CriteriaBuilder builder, TermSelector<T> termSelector) {
        List<Serializable> ids = hibernateSearchUtil.findId(root.getJavaType(), termSelector);
        if (ids == null) {
            return null;
        } else if (ids.isEmpty()) {
            return builder.disjunction();
        }

        return root.get("id").in(ids);
    }

    @Inject
    public void setHibernateSearchUtil(HibernateSearchUtil hibernateSearchUtil) {
        this.hibernateSearchUtil = hibernateSearchUtil;
    }

    @Inject
    public void setJpaUtil(JpaUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

}