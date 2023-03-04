package org.ujar.jh.petclinic.vuemin.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.ujar.jh.petclinic.vuemin.domain.Pets;

/**
 * Spring Data JPA repository for the Pets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PetsRepository extends JpaRepository<Pets, Long> {}
