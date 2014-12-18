package jpasearch.repository.util;

import static com.google.common.base.Throwables.propagate;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import jpasearch.repository.query.selector.ObjectTermSelector;
import jpasearch.repository.query.selector.StringTermSelector;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.hibernate.search.query.dsl.WildcardContext;

@Named
@Singleton
public class DefaultLuceneQueryBuilder implements LuceneQueryBuilder {

    private static final String PUNCTUATION = "\\p{Punct}|\\p{Space}";

    @Override
    public <T> Query build(FullTextEntityManager fullTextEntityManager, StringTermSelector<T> termSelector, Class<? extends T> type) {
        QueryBuilder builder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(type).get();

        BooleanJunction<?> context = builder.bool();
        boolean valid = false;
        if (termSelector.isNotEmpty()) {
            boolean hasTerms = false;
            BooleanJunction<?> termContext = builder.bool();
            for (String selected : termSelector.getSelected()) {
                if (isNotBlank(selected)) {
                    String[] values = selected.split(PUNCTUATION);
                    for (String value : values) {
                        if (isNotBlank(value) && (value.length() > 2)) {
                            List<String> fields = termSelector.getPaths();
                            BooleanJunction<?> valueContext = builder.bool();

                            addFuzzyMatch(builder, value, fields, termSelector.getSearchSimilarity(), valueContext);

                            addKeywordMatch(builder, value, fields, valueContext);

                            // wildcard search
                            // no #onFields on wildcardContext
                            WildcardContext wildcardContext = builder.keyword().wildcard();
                            TermMatchingContext termMatchingContext = null;
                            for (String field : fields) {
                                if (termMatchingContext != null) {
                                    termMatchingContext = termMatchingContext.andField(field);
                                } else {
                                    termMatchingContext = wildcardContext.onField(field);
                                }
                            }
                            valueContext.should(termMatchingContext. //
                                    matching("*" + value + "*").createQuery());

                            if (termSelector.isOrMode()) {
                                termContext.should(valueContext.createQuery());
                            } else {
                                termContext.must(valueContext.createQuery());
                            }
                            hasTerms = true;
                        }
                    }
                }
            }
            if (hasTerms) {
                context.must(termContext.createQuery());
                valid = true;
            }
        }
        return createQuery(builder, context, valid);
    }

    @Override
    public <T> Query build(FullTextEntityManager fullTextEntityManager, ObjectTermSelector<T> termSelector, Class<? extends T> type) {
        QueryBuilder builder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(type).get();

        BooleanJunction<?> context = builder.bool();
        boolean valid = false;
        if (termSelector.isNotEmpty()) {
            boolean hasTerms = false;
            BooleanJunction<?> termContext = builder.bool();
            for (Object selected : termSelector.getSelected()) {
                if ((selected != null) && isNotBlank(selected.toString())) {
                    List<String> fields = termSelector.getPaths();
                    BooleanJunction<?> valueContext = builder.bool();

                    addFuzzyMatch(builder, selected, fields, termSelector.getSearchSimilarity(), valueContext);

                    addKeywordMatch(builder, selected, fields, valueContext);

                    if (termSelector.isOrMode()) {
                        termContext.should(valueContext.createQuery());
                    } else {
                        termContext.must(valueContext.createQuery());
                    }
                    hasTerms = true;
                }
            }
            if (hasTerms) {
                context.must(termContext.createQuery());
                valid = true;
            }
        }
        return createQuery(builder, context, valid);
    }

    private <T> void addFuzzyMatch(QueryBuilder builder, Object value, List<String> fields, Integer editDistance, BooleanJunction<?> valueContext) {
        if (editDistance != null) {
            valueContext.should(builder.keyword().fuzzy() //
                    .withEditDistanceUpTo(editDistance) //
                    .onFields(fields.toArray(new String[fields.size()])) //
                    .matching(value).createQuery());
        }
    }

    private void addKeywordMatch(QueryBuilder builder, Object selected, List<String> fields, BooleanJunction<?> valueContext) {
        valueContext.should(builder.keyword() //
                .onFields(fields.toArray(new String[fields.size()])) //
                .matching(selected).createQuery());
    }

    private Query createQuery(QueryBuilder builder, BooleanJunction<?> context, boolean valid) {
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
