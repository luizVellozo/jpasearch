package jpasearch.repository;

import jpasearch.domain.EntityA;
import jpasearch.domain.Identifiable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
    public class Inserter<T extends Identifiable>{
        @PersistenceContext
        private EntityManager em;

        @Transactional(Transactional.TxType.REQUIRED)
        public void insertEntity(T entity){
            em.persist(entity);
        }
    }