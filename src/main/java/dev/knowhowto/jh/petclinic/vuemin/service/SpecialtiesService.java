package dev.knowhowto.jh.petclinic.vuemin.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dev.knowhowto.jh.petclinic.vuemin.service.dto.SpecialtiesDTO;

/**
 * Service Interface for managing {@link dev.knowhowto.jh.petclinic.vuemin.domain.Specialties}.
 */
public interface SpecialtiesService {
    /**
     * Save a specialties.
     *
     * @param specialtiesDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialtiesDTO save(SpecialtiesDTO specialtiesDTO);

    /**
     * Updates a specialties.
     *
     * @param specialtiesDTO the entity to update.
     * @return the persisted entity.
     */
    SpecialtiesDTO update(SpecialtiesDTO specialtiesDTO);

    /**
     * Partially updates a specialties.
     *
     * @param specialtiesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpecialtiesDTO> partialUpdate(SpecialtiesDTO specialtiesDTO);

    /**
     * Get all the specialties.
     *
     * @return the list of entities.
     */
    List<SpecialtiesDTO> findAll();

    /**
     * Get all the specialties with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpecialtiesDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" specialties.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialtiesDTO> findOne(Long id);

    /**
     * Delete the "id" specialties.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
