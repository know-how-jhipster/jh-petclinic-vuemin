package org.ujar.jh.petclinic.vuemin.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ujar.jh.petclinic.vuemin.domain.Specialties;

/**
 * Spring Data JPA repository for the Specialties entity.
 *
 * When extending this class, extend SpecialtiesRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SpecialtiesRepository extends SpecialtiesRepositoryWithBagRelationships, JpaRepository<Specialties, Long> {
    default Optional<Specialties> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Specialties> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Specialties> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
