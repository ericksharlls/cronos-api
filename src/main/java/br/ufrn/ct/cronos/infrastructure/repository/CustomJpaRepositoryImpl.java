package br.ufrn.ct.cronos.infrastructure.repository;

import br.ufrn.ct.cronos.domain.repository.CustomJpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

//SimpleJpaRepository: implementação padrão do repositório SpringDataJpa
public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
        implements CustomJpaRepository<T, ID> {

        private EntityManager manager;

        public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, 
            EntityManager entityManager) {
            super(entityInformation, entityManager);

            this.manager = entityManager;
        }

        @Override
        public void detach(T entity) {
            manager.detach(entity);
        }

}
