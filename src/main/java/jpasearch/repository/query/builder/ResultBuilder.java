package jpasearch.repository.query.builder;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import jpasearch.repository.query.ResultParameters;

/**
 * @author speralta
 */
public class ResultBuilder<F, FROM, TO> extends AbstractPathBuilder<Void, F, FROM, TO> {

    private final Class<TO> to;

    public ResultBuilder(PluralAttribute<? super F, ?, TO> attribute) {
        super(null, attribute);
        this.to = attribute.getBindableJavaType();
    }

    public ResultBuilder(SingularAttribute<? super F, TO> attribute) {
        super(null, attribute);
        this.to = attribute.getBindableJavaType();
    }

    private <E> ResultBuilder(ResultBuilder<F, E, FROM> resultBuilder, PluralAttribute<? super FROM, ?, TO> attribute) {
        super(null, resultBuilder, attribute);
        this.to = attribute.getBindableJavaType();
    }

    private <E> ResultBuilder(ResultBuilder<F, E, FROM> resultBuilder, SingularAttribute<? super FROM, TO> attribute) {
        super(null, resultBuilder, attribute);
        this.to = attribute.getBindableJavaType();
    }

    public <E> ResultBuilder<F, TO, E> to(SingularAttribute<? super TO, E> attribute) {
        return new ResultBuilder<F, TO, E>(this, attribute);
    }

    public <E> ResultBuilder<F, TO, E> to(PluralAttribute<? super TO, ?, E> attribute) {
        return new ResultBuilder<F, TO, E>(this, attribute);
    }

    public ResultParameters<F, TO> build() {
        return new ResultParameters<>(getPath(), to);
    }

}
