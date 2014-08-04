package jpasearch.repository.query.selector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.Path;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TermSelector<FROM> implements SingleSelector<FROM, String, TermSelector<FROM>> {
    private static final long serialVersionUID = 201308010800L;
    private final Path<FROM, ? extends Serializable> path;
    private List<String> selected = new ArrayList<>();
    private boolean orMode = true;
    private Integer searchSimilarity = null;

    public <T extends Serializable> TermSelector(SingularAttribute<? super FROM, T> attribute) {
        path = new Path<FROM, T>(attribute);
    }

    public TermSelector(Path<FROM, ? extends Serializable> path) {
        this.path = path;
    }

    private TermSelector(TermSelector<FROM> toCopy) {
        path = toCopy.path;
        selected = new ArrayList<>(toCopy.selected);
        orMode = toCopy.orMode;
        searchSimilarity = toCopy.searchSimilarity;
    }

    @Override
    public TermSelector<FROM> copy() {
        return new TermSelector<FROM>(this);
    }

    public String getPath() {
        return path.getPath();
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

    public TermSelector<FROM> or() {
        setOrMode(true);
        return this;
    }

    public TermSelector<FROM> and() {
        setOrMode(false);
        return this;
    }

    /**
     * Get the possible candidates for property.
     */
    public List<String> getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = Arrays.asList(selected);
    }

    /**
     * Set the possible candidates for property.
     */
    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

    public TermSelector<FROM> selected(String... selected) {
        setSelected(Arrays.asList(selected));
        return this;
    }

    public boolean isNotEmpty() {
        if ((selected == null) || selected.isEmpty()) {
            return false;
        }
        for (String word : selected) {
            if (StringUtils.isNotBlank(word)) {
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}