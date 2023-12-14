package dev.knowhowto.jh.petclinic.vuemin.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import dev.knowhowto.jh.petclinic.vuemin.domain.Vets;

/**
 * Spring Data JPA repository for the Vets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VetsRepository extends JpaRepository<Vets, Long> {}
