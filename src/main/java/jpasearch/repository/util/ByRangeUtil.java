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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jpasearch.repository.query.selector.Range;

/**
 * Helper to create a predicate out of {@link Range}s.
 */
@SuppressWarnings("unchecked")
@Named
@Singleton
public class ByRangeUtil {

    @Inject
    private JpaUtil jpaUtil;

    public <E> Predicate byRange(Root<E> root, CriteriaBuilder builder, Range<E, ?> range) {
        if (range.isSet()) {
            return buildRangePredicate(range, root, builder);
        } else {
            return null;
        }
    }

    private <D extends Comparable<? super D>, E> Predicate buildRangePredicate(Range<E, D> range, Root<E> root, CriteriaBuilder builder) {
        Predicate rangePredicate = null;
        Path<D> path = jpaUtil.getPath(root, range.getPath());
        if (range.isBetween()) {
            rangePredicate = builder.between(path, range.getLowerBound(), range.getHigherBound());
        } else if (range.isLowerBoundSet()) {
            if (range.isIncludeLowerBound()) {
                rangePredicate = builder.greaterThanOrEqualTo(path, range.getLowerBound());
            } else {
                rangePredicate = builder.greaterThan(path, range.getLowerBound());
            }
        } else if (range.isHigherBoundSet()) {
            if (range.isIncludeHigherBound()) {
                rangePredicate = builder.lessThanOrEqualTo(path, range.getHigherBound());
            } else {
                rangePredicate = builder.lessThan(path, range.getHigherBound());
            }
        }

        if (rangePredicate != null) {
            if (!range.isIncludeNullSet() || (range.getIncludeNull() == FALSE)) {
                return rangePredicate;
            } else {
                return builder.or(rangePredicate, builder.isNull(path));
            }
        } else {
            // no from/to is set, but include null or not could be:
            if (TRUE == range.getIncludeNull()) {
                return builder.isNull(path);
            } else if (FALSE == range.getIncludeNull()) {
                return builder.isNotNull(path);
            }
        }
        return null;
    }
}