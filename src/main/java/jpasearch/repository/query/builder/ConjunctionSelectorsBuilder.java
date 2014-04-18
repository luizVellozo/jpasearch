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
public class ConjunctionSelectorsBuilder<FROM, PARENT> extends SelectorsBuilder<FROM, PARENT, ConjunctionSelectorsBuilder<FROM, PARENT>> {

    public ConjunctionSelectorsBuilder(PARENT parent, Selectors<FROM> propertySelectors) {
        super(parent, propertySelectors);
    }

    public ConjunctionSelectorsBuilder(PARENT parent) {
        super(parent);
    }

    @Override
    protected ConjunctionSelectorsBuilder<FROM, PARENT> getThis() {
        return this;
    }

    public DisjunctionSelectorsBuilder<FROM, ConjunctionSelectorsBuilder<FROM, PARENT>> disjunction() {
        DisjunctionSelectorsBuilder<FROM, ConjunctionSelectorsBuilder<FROM, PARENT>> disjunction = new DisjunctionSelectorsBuilder<FROM, ConjunctionSelectorsBuilder<FROM, PARENT>>(this);
        selectors.add(disjunction.getSelectors().or());
        return disjunction;
    }

    public PARENT or() {
        return toParent();
    }
}
