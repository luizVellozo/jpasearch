package jpasearch.repository.query.selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jpasearch.repository.query.Path;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TermSelector<FROM, TO> implements SingleSelector<FROM, TO, TermSelector<FROM, TO>> {

    private static final long serialVersionUID = 201308010800L;
    private final List<Path<FROM, ?>> paths;
    private List<TO> selected = new ArrayList<>();
    private boolean orMode = true;
    private Integer searchSimilarity = null;
    private boolean notMode = false;

    TermSelector(Path<FROM, ?> path) {
        paths = new ArrayList<>();
        paths.add(path);
    }

    TermSelector(List<Path<FROM, ?>> paths) {
        this.paths = new ArrayList<>(paths);
    }

    private TermSelector(TermSelector<FROM, TO> toCopy) {
        paths = new ArrayList<>(toCopy.paths);
        selected = new ArrayList<>(toCopy.selected);
        orMode = toCopy.orMode;
        searchSimilarity = toCopy.searchSimilarity;
    }

    @Override
    public TermSelector<FROM, TO> copy() {
        return new TermSelector<FROM, TO>(this);
    }

    public List<String> getPaths() {
        List<String> paths = new ArrayList<>();
        for (Path<FROM, ?> path : this.paths) {
            paths.add(path.getPath());
        }
        return paths;
    }

    public Integer getSearchSimilarity() {
        return searchSimilarity;
    }

    public void setSearchSimilarity(Integer searchSimilarity) {
        this.searchSimilarity = searchSimilarity;
    }

    public boolean isOrMode() {
        return orMode;
    }

    public void setOrMode(boolean orMode) {
        this.orMode = orMode;
    }

    public TermSelector<FROM, TO> or() {
        setOrMode(true);
        return this;
    }

    public TermSelector<FROM, TO> and() {
        setOrMode(false);
        return this;
    }

    /**
     * Get the possible candidates for property.
     */
    public List<TO> getSelected() {
        return selected;
    }

    public void setSelected(TO selected) {
        this.selected = Arrays.asList(selected);
    }

    /**
     * Set the possible candidates for property.
     */
    public void setSelected(List<TO> selected) {
        this.selected = selected;
    }

    @SuppressWarnings("unchecked")
    public TermSelector<FROM, TO> selected(TO... selected) {
        setSelected(Arrays.asList(selected));
        return this;
    }

    public boolean isNotEmpty() {
        if ((selected == null) || selected.isEmpty()) {
            return false;
        }
        for (TO word : selected) {
            if ((word instanceof String) && StringUtils.isNotBlank((String) word)) {
                return true;
            } else if (word != null) {
                return true;
            }
        }
        return false;
    }

    public void clearSelected() {
        if (selected != null) {
            selected.clear();
        }
    }

    public boolean isNotMode() {
        return notMode;
    }

    public void setNotMode(boolean notMode) {
        this.notMode = notMode;
    }

    public TermSelector<FROM, TO> notMode(boolean notMode) {
        setNotMode(notMode);
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}