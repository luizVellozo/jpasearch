package jpasearch.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

/**
 * By making entities implement this interface we can easily retrieve from the
 * {@link jpasearch.repository.GenericRepository} the identifier property of the
 * entity.
 */
public interface Identifiable<PK extends Serializable> {

    /**
     * @return the primary key
     */
    PK getId();

    /**
     * Helper method to know whether the primary key is set or not.
     * 
     * @return true if the primary key is set, false otherwise
     */
    @XmlTransient
    boolean isIdSet();
}