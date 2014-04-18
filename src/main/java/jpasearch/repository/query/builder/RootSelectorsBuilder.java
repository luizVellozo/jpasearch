/*
 * Copyright 20144, jpasearch
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
package jpasearch.repository.query.builder;

import jpasearch.repository.query.SearchBuilder;
import jpasearch.repository.query.SearchParameters;
import jpasearch.repository.query.selector.Selectors;

/**
 * @author speralta
 */
public class RootSelectorsBuilder<FROM> extends SelectorsBuilder<FROM, SearchBuilder<FROM>, RootSelectorsBuilder<FROM>> {

    public RootSelectorsBuilder(SearchBuilder<FROM> parent, Selectors<FROM> propertySelectors) {
        super(parent, propertySelectors);
    }

    public RootSelectorsBuilder(SearchBuilder<FROM> parent) {
        super(parent);
    }

    public DisjunctionSelectorsBuilder<FROM, RootSelectorsBuilder<FROM>> disjunction() {
        DisjunctionSelectorsBuilder<FROM, RootSelectorsBuilder<FROM>> disjunction = new DisjunctionSelectorsBuilder<FROM, RootSelectorsBuilder<FROM>>(this);
        selectors.add(disjunction.getSelectors().or());
        return disjunction;
    }

    @Override
    protected RootSelectorsBuilder<FROM> getThis() {
        return this;
    }

    public SearchParameters<FROM> build() {
        return toParent().build();
    }

    public SearchBuilder<FROM> and() {
        return toParent();
    }

}
