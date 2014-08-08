package jpasearch.repository.query.selector;

import jpasearch.repository.query.Path;

/**
 * Range support for {@link Comparable} types.
 */
@SuppressWarnings("rawtypes")
public class Range<E, D extends Comparable> implements SingleSelector<E, D, Range<E, D>> {
    private static final long serialVersionUID = 201312031754L;

    private final Path<E, D> path;
    private D lowerBound;
    private boolean includeLowerBound = true;
    private D higherBound;
    private boolean includeHigherBound = true;
    private Boolean includeNull;

    /**
     * Constructs a new {@link Range} with no boundaries and no restrictions on
     * field's nullability.
     * 
     * @param path
     *            the path to the attribute of an existing entity.
     */
    public Range(Path<E, D> path) {
        this.path = path;
    }

    /**
     * Constructs a new Range.
     * 
     * @param lowerBound
     *            the lower boundary of this range. Null means no lower
     *            boundary.
     * @param higherBound
     *            the upper boundary of this range. Null means no upper
     *            boundary.
     * @param path
     *            the path to the attribute of an existing entity.
     */
    public Range(D lowerBound, D higherBound, Path<E, D> path) {
        this(path);
        this.lowerBound = lowerBound;
        this.higherBound = higherBound;
    }

    /**
     * Constructs a new Range.
     * 
     * @param lowerBound
     *            the lower boundary of this range. Null means no lower
     *            boundary.
     * @param higherBound
     *            the upper boundary of this range. Null means no upper
     *            boundary.
     * @param includeNull
     *            tells whether null should be filtered out or not.
     * @param path
     *            the path to the attribute of an existing entity.
     */
    public Range(D lowerBound, D higherBound, Boolean includeNull, Path<E, D> path) {
        this(lowerBound, higherBound, path);
        this.includeNull = includeNull;
    }

    private Range(Range<E, D> other) {
        this.path = other.path;
        this.lowerBound = other.lowerBound;
        this.higherBound = other.higherBound;
        this.includeNull = other.includeNull;
    }

    @Override
    public Range<E, D> copy() {
        return new Range<E, D>(this);
    }

    public String getPath() {
        return path.getPath();
    }

    /**
     * @return the lower range boundary or null for unbound lower range.
     */
    public D getLowerBound() {
        return lowerBound;
    }

    /**
     * Sets the lower range boundary. Accepts null for unbound lower range.
     */
    public void setLowerBound(D lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Range<E, D> lowerBound(D lowerBound) {
        setLowerBound(lowerBound);
        return this;
    }

    public boolean isLowerBoundSet() {
        return getLowerBound() != null;
    }

    /**
     * @return the upper range boundary or null for unbound upper range.
     */
    public D getHigherBound() {
        return higherBound;
    }

    public Range<E, D> higherBound(D higherBound) {
        setHigherBound(higherBound);
        return this;
    }

    /**
     * Sets the upper range boundary. Accepts null for unbound upper range.
     */
    public void setHigherBound(D higherBound) {
        this.higherBound = higherBound;
    }

    public boolean isHigherBoundSet() {
        return getHigherBound() != null;
    }

    public void setIncludeNull(Boolean includeNull) {
        this.includeNull = includeNull;
    }

    public Range<E, D> includeNull(Boolean includeNull) {
        setIncludeNull(includeNull);
        return this;
    }

    public Boolean getIncludeNull() {
        return includeNull;
    }

    public boolean isIncludeNullSet() {
        return includeNull != null;
    }

    public boolean isBetween() {
        return isLowerBoundSet() && isHigherBoundSet();
    }

    public boolean isSet() {
        return isLowerBoundSet() || isHigherBoundSet() || isIncludeNullSet();
    }

    @SuppressWarnings("unchecked")
    public boolean isValid() {
        if (isBetween()) {
            return getLowerBound().compareTo(getHigherBound()) <= 0;
        }
        return true;
    }

    public void resetRange() {
        lowerBound = null;
        higherBound = null;
        includeNull = null;
        includeLowerBound = true;
        includeHigherBound = true;
    }

    /**
     * @return whether lowerBound bound should included or not. Default is true.
     */
    public boolean isIncludeLowerBound() {
        return includeLowerBound;
    }

    public void setIncludeLowerBound(boolean includeLowerBound) {
        this.includeLowerBound = includeLowerBound;
    }

    public Range<E, D> includeLowerBound(boolean includeLowerBound) {
        setIncludeLowerBound(includeLowerBound);
        return this;
    }

    /**
     * @return whether higherBound bound should included or not. Default is
     *         true.
     */
    public boolean isIncludeHigherBound() {
        return includeHigherBound;
    }

    public void setIncludeHigherBound(boolean includeHigherBound) {
        this.includeHigherBound = includeHigherBound;
    }

    public Range<E, D> includeHigherBound(boolean includeHigherBound) {
        setIncludeHigherBound(includeHigherBound);
        return this;
    }
}