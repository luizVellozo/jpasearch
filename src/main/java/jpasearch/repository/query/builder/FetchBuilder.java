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

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;
import jpasearch.repository.query.SearchBuilder;

/**
 * @author speralta
 */
public class FetchBuilder<F, FROM, TO> {

    private final FetchesBuilder<F> builder;

    private final Path<F, TO> path;

    public FetchBuilder(FetchesBuilder<F> builder, SingularAttribute<? super F, TO> attribute) {
        this.builder = builder;
        this.path = new Path<F, TO>(attribute);
    }

    public FetchBuilder(FetchesBuilder<F> builder, PluralAttribute<? super F, ?, TO> attribute) {
        this.builder = builder;
        this.path = new Path<F, TO>(attribute);
    }

    private <E> FetchBuilder(FetchesBuilder<F> builder, FetchBuilder<F, E, FROM> fetchBuilder, SingularAttribute<? super FROM, TO> attribute) {
        this.builder = builder;
        this.path = fetchBuilder.path.add(attribute);
    }

    private <E> FetchBuilder(FetchesBuilder<F> builder, FetchBuilder<F, E, FROM> fetchBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        this.builder = builder;
        this.path = fetchBuilder.path.add(attribute);
    }

    public SearchBuilder<F> and() {
        return builder.add(path).toParent();
    }

    public <E> FetchBuilder<F, TO, E> to(SingularAttribute<? super TO, E> attribute) {
        return new FetchBuilder<F, TO, E>(builder, this, attribute);
    }

    public <E> FetchBuilder<F, TO, E> to(PluralAttribute<? super TO, ?, E> attribute) {
        return new FetchBuilder<F, TO, E>(builder, this, attribute);
    }

}
