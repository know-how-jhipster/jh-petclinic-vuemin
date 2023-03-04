package org.ujar.jh.petclinic.vuemin.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ujar.jh.petclinic.vuemin.service.dto.VetsDTO;

/**
 * Service Interface for managing {@link org.ujar.jh.petclinic.vuemin.domain.Vets}.
 */
public interface VetsService {
    /**
     * Save a vets.
     *
     * @param vetsDTO the entity to save.
     * @return the persisted entity.
     */
    VetsDTO save(VetsDTO vetsDTO);

    /**
     * Updates a vets.
     *
     * @param vetsDTO the entity to update.
     * @return the persisted entity.
     */
    VetsDTO update(VetsDTO vetsDTO);

    /**
     * Partially updates a vets.
     *
     * @param vetsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VetsDTO> partialUpdate(VetsDTO vetsDTO);

    /**
     * Get all the vets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VetsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vets.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VetsDTO> findOne(Long id);

    /**
     * Delete the "id" vets.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
