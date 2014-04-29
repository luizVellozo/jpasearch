package jpasearch.repository.query.builder;

import jpasearch.repository.query.Path;
import jpasearch.repository.query.selector.TermSelector;

/**
 * @author speralta
 */
public class TermSelectorBuilder<FROM, PARENT, CURRENT extends SelectorsBuilder<FROM, PARENT, CURRENT>> {

    private final SelectorsBuilder<FROM, PARENT, CURRENT> selectorsBuilder;
    private final TermSelector<FROM> termSelector;

    public TermSelectorBuilder(SelectorsBuilder<FROM, PARENT, CURRENT> selectorsBuilder, Path<FROM, String> path) {
        this.selectorsBuilder = selectorsBuilder;
        termSelector = new TermSelector<FROM>(path);
    }

    public TermSelectorBuilder<FROM, PARENT, CURRENT> searchSimilarity(Integer searchSimilarity) {
        termSelector.setSearchSimilarity(searchSimilarity);
        return this;
    }

    public CURRENT search(String... selected) {
        termSelector.selected(selected);
        return selectorsBuilder.add(termSelector);
    }

}
