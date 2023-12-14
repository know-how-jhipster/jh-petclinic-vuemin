package dev.knowhowto.jh.petclinic.vuemin.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.knowhowto.jh.petclinic.vuemin.repository.SpecialtiesRepository;
import dev.knowhowto.jh.petclinic.vuemin.service.SpecialtiesService;
import dev.knowhowto.jh.petclinic.vuemin.service.dto.SpecialtiesDTO;
import dev.knowhowto.jh.petclinic.vuemin.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dev.knowhowto.jh.petclinic.vuemin.domain.Specialties}.
 */
@RestController
@RequestMapping("/api")
public class SpecialtiesResource {

    private final Logger log = LoggerFactory.getLogger(SpecialtiesResource.class);

    private static final String ENTITY_NAME = "specialties";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialtiesService specialtiesService;

    private final SpecialtiesRepository specialtiesRepository;

    public SpecialtiesResource(SpecialtiesService specialtiesService, SpecialtiesRepository specialtiesRepository) {
        this.specialtiesService = specialtiesService;
        this.specialtiesRepository = specialtiesRepository;
    }

    /**
     * {@code POST  /specialties} : Create a new specialties.
     *
     * @param specialtiesDTO the specialtiesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialtiesDTO, or with status {@code 400 (Bad Request)} if the specialties has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specialties")
    public ResponseEntity<SpecialtiesDTO> createSpecialties(@Valid @RequestBody SpecialtiesDTO specialtiesDTO) throws URISyntaxException {
        log.debug("REST request to save Specialties : {}", specialtiesDTO);
        if (specialtiesDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialties cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialtiesDTO result = specialtiesService.save(specialtiesDTO);
        return ResponseEntity
            .created(new URI("/api/specialties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specialties/:id} : Updates an existing specialties.
     *
     * @param id the id of the specialtiesDTO to save.
     * @param specialtiesDTO the specialtiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialtiesDTO,
     * or with status {@code 400 (Bad Request)} if the specialtiesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialtiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specialties/{id}")
    public ResponseEntity<SpecialtiesDTO> updateSpecialties(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialtiesDTO specialtiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Specialties : {}, {}", id, specialtiesDTO);
        if (specialtiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialtiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialtiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecialtiesDTO result = specialtiesService.update(specialtiesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specialtiesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /specialties/:id} : Partial updates given fields of an existing specialties, field will ignore if it is null
     *
     * @param id the id of the specialtiesDTO to save.
     * @param specialtiesDTO the specialtiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialtiesDTO,
     * or with status {@code 400 (Bad Request)} if the specialtiesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specialtiesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialtiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/specialties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialtiesDTO> partialUpdateSpecialties(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialtiesDTO specialtiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Specialties partially : {}, {}", id, specialtiesDTO);
        if (specialtiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialtiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialtiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialtiesDTO> result = specialtiesService.partialUpdate(specialtiesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specialtiesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /specialties} : get all the specialties.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialties in body.
     */
    @GetMapping("/specialties")
    public List<SpecialtiesDTO> getAllSpecialties(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Specialties");
        return specialtiesService.findAll();
    }

    /**
     * {@code GET  /specialties/:id} : get the "id" specialties.
     *
     * @param id the id of the specialtiesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialtiesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specialties/{id}")
    public ResponseEntity<SpecialtiesDTO> getSpecialties(@PathVariable Long id) {
        log.debug("REST request to get Specialties : {}", id);
        Optional<SpecialtiesDTO> specialtiesDTO = specialtiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialtiesDTO);
    }

    /**
     * {@code DELETE  /specialties/:id} : delete the "id" specialties.
     *
     * @param id the id of the specialtiesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specialties/{id}")
    public ResponseEntity<Void> deleteSpecialties(@PathVariable Long id) {
        log.debug("REST request to delete Specialties : {}", id);
        specialtiesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
