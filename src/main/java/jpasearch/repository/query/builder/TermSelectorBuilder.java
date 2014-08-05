package jpasearch.repository.query.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;
import jpasearch.repository.query.selector.TermSelector;

/**
 * @author speralta
 */
public class TermSelectorBuilder<FROM, PARENT extends SelectorsBuilder<FROM, GRANDPARENT, PARENT>, GRANDPARENT> extends AbstractBuilder<PARENT> {

    private final List<Path<FROM, ?>> paths = new ArrayList<>();
    private Integer searchSimilarity = null;

    public TermSelectorBuilder(PARENT parent) {
        super(parent);
    }

    public <TO> TermSelectorPathBuilder<FROM, FROM, TO, PARENT, GRANDPARENT> on(SingularAttribute<? super FROM, TO> attribute) {
        return new TermSelectorPathBuilder<>(this, attribute);
    }

    public <TO> TermSelectorPathBuilder<FROM, FROM, TO, PARENT, GRANDPARENT> on(PluralAttribute<? super FROM, ?, TO> attribute) {
        return new TermSelectorPathBuilder<>(this, attribute);
    }

    public TermSelectorBuilder<FROM, PARENT, GRANDPARENT> searchSimilarity(Integer searchSimilarity) {
        this.searchSimilarity = searchSimilarity;
        return this;
    }

    public PARENT search(String... selected) {
        TermSelector<FROM> termSelector = new TermSelector<FROM>(paths);
        termSelector.selected(selected);
        termSelector.setSearchSimilarity(searchSimilarity);
        return toParent().add(termSelector);
    }

    protected <TO> TermSelectorPathBuilder<FROM, FROM, TO, PARENT, GRANDPARENT> andOn(TermSelectorPathBuilder<FROM, ?, ?, PARENT, GRANDPARENT> termSelectorPathBuilder,
            SingularAttribute<? super FROM, TO> attribute) {
        addPath(termSelectorPathBuilder);
        return new TermSelectorPathBuilder<>(this, attribute);
    }

    protected <TO> TermSelectorPathBuilder<FROM, FROM, TO, PARENT, GRANDPARENT> andOn(TermSelectorPathBuilder<FROM, ?, ?, PARENT, GRANDPARENT> termSelectorPathBuilder,
            PluralAttribute<? super FROM, ?, TO> attribute) {
        addPath(termSelectorPathBuilder);
        return new TermSelectorPathBuilder<>(this, attribute);
    }

    protected PARENT search(TermSelectorPathBuilder<FROM, ?, ?, PARENT, GRANDPARENT> termSelectorPathBuilder, String... selected) {
        addPath(termSelectorPathBuilder);
        return search(selected);
    }

    protected TermSelectorBuilder<FROM, PARENT, GRANDPARENT> searchSimilarity(TermSelectorPathBuilder<FROM, ?, ?, PARENT, GRANDPARENT> termSelectorPathBuilder, Integer searchSimilarity) {
        addPath(termSelectorPathBuilder);
        return searchSimilarity(searchSimilarity);
    }

    private void addPath(TermSelectorPathBuilder<FROM, ?, ?, PARENT, GRANDPARENT> termSelectorPathBuilder) {
        paths.add(termSelectorPathBuilder.getPath());
    }

}
