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
package jpasearch.repository.query;

import java.io.Serializable;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.selector.PropertySelector;
import jpasearch.repository.query.selector.TermSelector;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Holder class for path used by the {@link OrderBy}, {@link PropertySelector},
 * {@link TermSelector} and {@link SearchParameters}.
 */
public class Path<FROM, TO> implements Serializable {
    private static final long serialVersionUID = 201403271745L;

    private final String path;
    private final Class<? super FROM> from;

    public Path(SingularAttribute<? super FROM, TO> attribute) {
        this(attribute.getDeclaringType().getJavaType(), attribute.getName());
    }

    public Path(PluralAttribute<? super FROM, ?, TO> attribute) {
        this(attribute.getDeclaringType().getJavaType(), attribute.getName());
    }

    private <T> Path(Path<FROM, T> old, SingularAttribute<? super T, TO> attribute) {
        this(old.from, old.path + "." + attribute.getName());
    }

    private <T> Path(Path<FROM, T> old, PluralAttribute<? super T, ?, TO> attribute) {
        this(old.from, old.path + "." + attribute.getName());
    }

    public Path(Class<? super FROM> from, String path) {
        this.from = from;
        this.path = path;
    }

    public <T> Path<FROM, T> add(SingularAttribute<? super TO, T> attribute) {
        return new Path<FROM, T>(this, attribute);
    }

    public <T> Path<FROM, T> add(PluralAttribute<? super TO, ?, T> attribute) {
        return new Path<FROM, T>(this, attribute);
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((from == null) ? 0 : from.hashCode());
        result = (prime * result) + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Path<?, ?> rhs = (Path<?, ?>) obj;
        return new EqualsBuilder().append(path, rhs.path).append(from, rhs.from).isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}