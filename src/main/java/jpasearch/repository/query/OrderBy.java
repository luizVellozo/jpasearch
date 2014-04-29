package jpasearch.repository.query;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Holder class for search ordering used by the {@link SearchParameters}.
 */
public class OrderBy<FROM, TO> implements Serializable {
    private static final long serialVersionUID = 201403271745L;

    private final Path<FROM, TO> path;
    private final OrderByDirection direction;

    public OrderBy(OrderByDirection direction, Path<FROM, TO> path) {
        this.direction = direction;
        this.path = path;
    }

    public OrderBy(OrderByDirection direction, Class<FROM> from, String path) {
        this.direction = checkNotNull(direction);
        this.path = new Path<FROM, TO>(checkNotNull(from), checkNotNull(path));
    }

    public String getPath() {
        return path.getPath();
    }

    public OrderByDirection getDirection() {
        return direction;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OrderBy<?, ?> other = (OrderBy<?, ?>) obj;
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}