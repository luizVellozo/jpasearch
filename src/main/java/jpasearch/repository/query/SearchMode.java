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

import jpasearch.repository.query.selector.PropertySelector;

/**
 * Static values to use in conjunction with {@link PropertySelector} object. It
 * maps the kind of search you can do in SQL.
 */
public enum SearchMode {
    /**
     * Match exactly the properties
     */
    EQUALS("eq"),
    /**
     * Activates LIKE search and add a '%' prefix and suffix before searching.
     */
    ANYWHERE("any"),
    /**
     * Activate LIKE search and add a '%' prefix before searching.
     */
    STARTING_LIKE("sl"),
    /**
     * Activate LIKE search. User provides the wildcard.
     */
    LIKE("li"),
    /**
     * Activate LIKE search and add a '%' suffix before searching.
     */
    ENDING_LIKE("el");

    private final String code;

    SearchMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static final SearchMode convert(String code) {
        for (SearchMode searchMode : SearchMode.values()) {
            if (searchMode.getCode().equals(code)) {
                return searchMode;
            }
        }

        return EQUALS; // default
    }
}
