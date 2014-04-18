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

import jpasearch.repository.query.selector.Selectors;

/**
 * @author speralta
 */
public class DisjunctionSelectorsBuilder<FROM, PARENT> extends SelectorsBuilder<FROM, PARENT, DisjunctionSelectorsBuilder<FROM, PARENT>> {

    public DisjunctionSelectorsBuilder(PARENT parent, Selectors<FROM> propertySelectors) {
        super(parent, propertySelectors);
    }

    public DisjunctionSelectorsBuilder(PARENT parent) {
        super(parent);
    }

    @Override
    protected DisjunctionSelectorsBuilder<FROM, PARENT> getThis() {
        return this;
    }

    public ConjunctionSelectorsBuilder<FROM, DisjunctionSelectorsBuilder<FROM, PARENT>> conjunction() {
        ConjunctionSelectorsBuilder<FROM, DisjunctionSelectorsBuilder<FROM, PARENT>> conjunction = new ConjunctionSelectorsBuilder<FROM, DisjunctionSelectorsBuilder<FROM, PARENT>>(this);
        selectors.add(conjunction.getSelectors().or());
        return conjunction;
    }

    public PARENT and() {
        return toParent();
    }
}
