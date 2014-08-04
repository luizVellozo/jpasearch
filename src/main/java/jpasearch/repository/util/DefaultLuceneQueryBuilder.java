package jpasearch.repository.util;

import static com.google.common.base.Throwables.propagate;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.repository.query.selector.TermSelector;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

@Named
@Singleton
public class DefaultLuceneQueryBuilder implements LuceneQueryBuilder {

    private static final String PUNCTUATION = "\\p{Punct}";

    @Override
    public <T> Query build(FullTextEntityManager fullTextEntityManager, TermSelector<T> termSelector, Class<? extends T> type) {
        QueryBuilder builder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(type).get();

        BooleanJunction<?> context = builder.bool();
        boolean valid = false;
        if (termSelector.isNotEmpty()) {
            boolean hasTerms = false;
            BooleanJunction<?> termContext = builder.bool();
            for (String selected : termSelector.getSelected()) {
                if (isNotBlank(selected)) {
                    String value = StringUtils.join(selected.split(PUNCTUATION), " ");
                    if (isNotBlank(value)) {
                        BooleanJunction<?> valueContext = builder.bool();
                        if (termSelector.getSearchSimilarity() != null) {
                            valueContext.should(builder.keyword().fuzzy() //
                                    .withEditDistanceUpTo(termSelector.getSearchSimilarity()) //
                                    .onField(termSelector.getPath()) //
                                    .matching(value).createQuery());
                        }
                        valueContext.should(builder.keyword() //
                                .onField(termSelector.getPath()) //
                                .matching(value).createQuery());
                        valueContext.should(builder.keyword().wildcard() //
                                .onField(termSelector.getPath()) //
                                .matching("*" + value + "*").createQuery());
                        if (termSelector.isOrMode()) {
                            termContext.should(valueContext.createQuery());
                        } else {
                            termContext.must(valueContext.createQuery());
                        }
                        hasTerms = true;
                    }
                }
            }
            if (hasTerms) {
                context.must(termContext.createQuery());
                valid = true;
            }
        }
        try {
            if (valid) {
                return context.createQuery();
            } else {
                return builder.all().except(builder.all().createQuery()).createQuery();
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

}
