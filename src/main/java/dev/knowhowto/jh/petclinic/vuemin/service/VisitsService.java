package dev.knowhowto.jh.petclinic.vuemin.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dev.knowhowto.jh.petclinic.vuemin.service.dto.VisitsDTO;

/**
 * Service Interface for managing {@link dev.knowhowto.jh.petclinic.vuemin.domain.Visits}.
 */
public interface VisitsService {
    /**
     * Save a visits.
     *
     * @param visitsDTO the entity to save.
     * @return the persisted entity.
     */
    VisitsDTO save(VisitsDTO visitsDTO);

    /**
     * Updates a visits.
     *
     * @param visitsDTO the entity to update.
     * @return the persisted entity.
     */
    VisitsDTO update(VisitsDTO visitsDTO);

    /**
     * Partially updates a visits.
     *
     * @param visitsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VisitsDTO> partialUpdate(VisitsDTO visitsDTO);

    /**
     * Get all the visits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VisitsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" visits.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VisitsDTO> findOne(Long id);

    /**
     * Delete the "id" visits.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
