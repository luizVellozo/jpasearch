package jpasearch.repository.embedded;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jpasearch.TestApplication;
import jpasearch.domain.embedded.EmbeddableField;
import jpasearch.domain.embedded.EmbeddableField_;
import jpasearch.domain.embedded.EntityEmbedded;
import jpasearch.domain.embedded.EntityEmbedded_;
import jpasearch.domain.embedded.EntityWithEmbeddedField;
import jpasearch.domain.embedded.EntityWithEmbeddedField_;
import jpasearch.repository.query.SearchBuilder;
import jpasearch.repository.query.SearchParameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author speralta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class EntityWithEmbeddedFieldRepositoryIT {

    @Inject
    private EntityWithEmbeddedFieldRepository entityWithEmbeddedFieldRepository;

    @Test
    public void test() {
        final String testValue = "test";

        assertThat(entityWithEmbeddedFieldRepository.findCount(findByEmbedded(testValue))).isEqualTo(0);

        EntityWithEmbeddedField entityWithEmbeddedField = new EntityWithEmbeddedField();
        EmbeddableField embeddableField = new EmbeddableField();
        EntityEmbedded entityEmbedded = new EntityEmbedded();
        entityEmbedded.setValue(testValue);
        embeddableField.setEntityEmbedded(entityEmbedded);
        entityWithEmbeddedField.setEmbeddableField(embeddableField);
        entityWithEmbeddedField = entityWithEmbeddedFieldRepository.save(entityWithEmbeddedField);

        assertThat(entityWithEmbeddedFieldRepository.find(findByEmbedded(testValue))).containsExactly(entityWithEmbeddedField);
    }

    private SearchParameters<EntityWithEmbeddedField> findByEmbedded(String value) {
        return new SearchBuilder<EntityWithEmbeddedField>() //
                .on(EntityWithEmbeddedField_.embeddableField) //
                .to(EmbeddableField_.entityEmbedded) //
                .to(EntityEmbedded_.value).equalsTo(value) //
                .build();
    }
}
