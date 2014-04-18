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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import jpasearch.repository.query.OrderBy;
import jpasearch.repository.query.OrderByDirection;

/**
 * Helper to create list of {@link Order} out of {@link OrderBy}s.
 */
@Named
@Singleton
public class OrderByUtil {

    private JpaUtil jpaUtil;

    public <E> List<Order> buildJpaOrders(Iterable<OrderBy<E, ?>> orders, Root<E> root, CriteriaBuilder builder) {
        List<Order> jpaOrders = new ArrayList<>();
        for (OrderBy<E, ?> ob : orders) {
            Path<?> path = jpaUtil.getPath(root, ob.getPath());
            jpaOrders.add(ob.getDirection() == OrderByDirection.DESC ? builder.desc(path) : builder.asc(path));
        }
        return jpaOrders;
    }

    @Inject
    public void setJpaUtil(JpaUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

}