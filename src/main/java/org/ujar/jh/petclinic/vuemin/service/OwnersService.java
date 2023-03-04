package org.ujar.jh.petclinic.vuemin.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ujar.jh.petclinic.vuemin.service.dto.OwnersDTO;

/**
 * Service Interface for managing {@link org.ujar.jh.petclinic.vuemin.domain.Owners}.
 */
public interface OwnersService {
    /**
     * Save a owners.
     *
     * @param ownersDTO the entity to save.
     * @return the persisted entity.
     */
    OwnersDTO save(OwnersDTO ownersDTO);

    /**
     * Updates a owners.
     *
     * @param ownersDTO the entity to update.
     * @return the persisted entity.
     */
    OwnersDTO update(OwnersDTO ownersDTO);

    /**
     * Partially updates a owners.
     *
     * @param ownersDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OwnersDTO> partialUpdate(OwnersDTO ownersDTO);

    /**
     * Get all the owners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OwnersDTO> findAll(Pageable pageable);

    /**
     * Get the "id" owners.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OwnersDTO> findOne(Long id);

    /**
     * Delete the "id" owners.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
