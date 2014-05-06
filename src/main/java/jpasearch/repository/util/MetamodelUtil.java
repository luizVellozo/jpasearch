package jpasearch.repository.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Named
@Singleton
public class MetamodelUtil {

    private final LoadingCache<Class<?>, Class<?>> metamodelCache = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<Class<?>, Class<?>>() {
        @Override
        public Class<?> load(Class<?> key) throws ClassNotFoundException {
            return Class.forName(key.getName() + "_");
        }
    });

    public SingularAttribute<?, ?> toAttribute(Class<?> from, String property) {
        try {
            Class<?> metamodelClass = metamodelCache.get(from);
            Field field = metamodelClass.getField(property);
            return (SingularAttribute<?, ?>) field.get(null);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<Attribute<?, ?>> toAttributes(Class<?> from, String path) {
        try {
            List<Attribute<?, ?>> attributes = new ArrayList<>();
            Class<?> current = from;
            for (String pathItem : Splitter.on(".").split(path)) {
                Class<?> metamodelClass = metamodelCache.get(current);
                Field field = metamodelClass.getField(pathItem);
                Attribute<?, ?> attribute = (Attribute<?, ?>) field.get(null);
                attributes.add(attribute);
                if (attribute instanceof PluralAttribute) {
                    current = ((PluralAttribute<?, ?, ?>) attribute).getElementType().getJavaType();
                } else {
                    current = attribute.getJavaType();
                }
            }
            return attributes;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String toPath(List<Attribute<?, ?>> attributes) {
        StringBuilder path = new StringBuilder();
        for (Attribute<?, ?> attribute : attributes) {
            if (path.length() > 0) {
                path.append(".");
            }
            path.append(attribute.getName());
        }
        return path.toString();
    }

    public boolean isBoolean(Class<?> from, String path) {
        return isType(Boolean.class, toAttributes(from, path));
    }

    public boolean isString(Class<?> from, String path) {
        return isType(String.class, toAttributes(from, path));
    }

    public boolean isNumber(Class<?> from, String path) {
        return isType(Number.class, toAttributes(from, path));
    }

    public boolean isType(Class<?> type, List<Attribute<?, ?>> attributes) {
        return type.isAssignableFrom(attributes.get(attributes.size() - 1).getJavaType());
    }

    /**
     * Retrieves cascade from metamodel attribute
     * 
     * @return an empty collection if no jpa relation annotation can be found.
     */
    public Collection<CascadeType> getCascades(PluralAttribute<?, ?, ?> attribute) {
        if (attribute.getJavaMember() instanceof AccessibleObject) {
            AccessibleObject accessibleObject = (AccessibleObject) attribute.getJavaMember();
            OneToMany oneToMany = accessibleObject.getAnnotation(OneToMany.class);
            if (oneToMany != null) {
                return Arrays.asList(oneToMany.cascade());
            }
            ManyToMany manyToMany = accessibleObject.getAnnotation(ManyToMany.class);
            if (manyToMany != null) {
                return Arrays.asList(manyToMany.cascade());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Retrieves cascade from metamodel attribute on a xToMany relation.
     * 
     * @return an empty collection if no jpa relation annotation can be found.
     */
    public Collection<CascadeType> getCascades(SingularAttribute<?, ?> attribute) {
        if (attribute.getJavaMember() instanceof AccessibleObject) {
            AccessibleObject accessibleObject = (AccessibleObject) attribute.getJavaMember();
            OneToOne oneToOne = accessibleObject.getAnnotation(OneToOne.class);
            if (oneToOne != null) {
                return Arrays.asList(oneToOne.cascade());
            }
            ManyToOne manyToOne = accessibleObject.getAnnotation(ManyToOne.class);
            if (manyToOne != null) {
                return Arrays.asList(manyToOne.cascade());
            }
        }
        return new ArrayList<>();
    }

    public boolean isOrphanRemoval(PluralAttribute<?, ?, ?> attribute) {
        if (attribute.getJavaMember() instanceof AccessibleObject) {
            AccessibleObject accessibleObject = (AccessibleObject) attribute.getJavaMember();
            OneToMany oneToMany = accessibleObject.getAnnotation(OneToMany.class);
            if (oneToMany != null) {
                return oneToMany.orphanRemoval();
            }
        }
        return true;
    }
}
