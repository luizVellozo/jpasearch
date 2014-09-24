package jpasearch.repository.query.selector;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author speralta
 */
public class Selectors<FROM> implements GroupSelector<FROM, Selectors<FROM>> {
    private static final long serialVersionUID = 201403271745L;

    private boolean andMode;

    private final List<Selector<FROM, ?>> selectors;

    public Selectors() {
        andMode = true;
        selectors = new ArrayList<>();
    }

    private Selectors(Selectors<FROM> toCopy) {
        andMode = toCopy.andMode;
        selectors = new ArrayList<>();
        for (Selector<FROM, ?> selector : toCopy.selectors) {
            selectors.add(selector.copy());
        }
    }

    @Override
    public Selectors<FROM> copy() {
        return new Selectors<FROM>(this);
    }

    public Selectors<FROM> or() {
        andMode = false;
        return this;
    }

    public Selectors<FROM> add(Selector<FROM, ?> selector) {
        selectors.add(selector);
        return this;
    }

    public boolean isAndMode() {
        return andMode;
    }

    public List<Selector<FROM, ?>> getSelectors() {
        return selectors;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
