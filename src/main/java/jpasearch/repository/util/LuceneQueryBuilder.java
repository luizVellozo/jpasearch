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

import jpasearch.repository.query.selector.TermSelector;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;

public interface LuceneQueryBuilder {

    <T> Query build(FullTextEntityManager fullTextEntityManager, TermSelector<T> termSelector, Class<? extends T> type);
}