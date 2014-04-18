/*
 * Copyright 2014, jpasearch
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
package jpasearch.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

/**
 * By making entities implement this interface we can easily retrieve from the
 * {@link jpasearch.repository.GenericRepository} the identifier property of the
 * entity.
 */
public interface Identifiable<PK extends Serializable> {

    /**
     * @return the primary key
     */
    PK getId();

    /**
     * Helper method to know whether the primary key is set or not.
     * 
     * @return true if the primary key is set, false otherwise
     */
    @XmlTransient
    boolean isIdSet();
}