package org.ujar.jh.petclinic.vuemin.web.rest;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.ujar.jh.petclinic.vuemin.repository.VetsRepository;
import org.ujar.jh.petclinic.vuemin.service.VetsService;
import org.ujar.jh.petclinic.vuemin.service.dto.VetsDTO;
import org.ujar.jh.petclinic.vuemin.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.ujar.jh.petclinic.vuemin.domain.Vets}.
 */
@RestController
@RequestMapping("/api")
public class VetsResource {

    private final Logger log = LoggerFactory.getLogger(VetsResource.class);

    private static final String ENTITY_NAME = "vets";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VetsService vetsService;

    private final VetsRepository vetsRepository;

    public VetsResource(VetsService vetsService, VetsRepository vetsRepository) {
        this.vetsService = vetsService;
        this.vetsRepository = vetsRepository;
    }

    /**
     * {@code POST  /vets} : Create a new vets.
     *
     * @param vetsDTO the vetsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vetsDTO, or with status {@code 400 (Bad Request)} if the vets has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vets")
    public ResponseEntity<VetsDTO> createVets(@Valid @RequestBody VetsDTO vetsDTO) throws URISyntaxException {
        log.debug("REST request to save Vets : {}", vetsDTO);
        if (vetsDTO.getId() != null) {
            throw new BadRequestAlertException("A new vets cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VetsDTO result = vetsService.save(vetsDTO);
        return ResponseEntity
            .created(new URI("/api/vets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vets/:id} : Updates an existing vets.
     *
     * @param id the id of the vetsDTO to save.
     * @param vetsDTO the vetsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vetsDTO,
     * or with status {@code 400 (Bad Request)} if the vetsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vetsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vets/{id}")
    public ResponseEntity<VetsDTO> updateVets(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VetsDTO vetsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vets : {}, {}", id, vetsDTO);
        if (vetsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vetsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VetsDTO result = vetsService.update(vetsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vetsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vets/:id} : Partial updates given fields of an existing vets, field will ignore if it is null
     *
     * @param id the id of the vetsDTO to save.
     * @param vetsDTO the vetsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vetsDTO,
     * or with status {@code 400 (Bad Request)} if the vetsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vetsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vetsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VetsDTO> partialUpdateVets(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VetsDTO vetsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vets partially : {}, {}", id, vetsDTO);
        if (vetsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vetsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VetsDTO> result = vetsService.partialUpdate(vetsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vetsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vets} : get all the vets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vets in body.
     */
    @GetMapping("/vets")
    public ResponseEntity<List<VetsDTO>> getAllVets(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Vets");
        Page<VetsDTO> page = vetsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vets/:id} : get the "id" vets.
     *
     * @param id the id of the vetsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vetsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vets/{id}")
    public ResponseEntity<VetsDTO> getVets(@PathVariable Long id) {
        log.debug("REST request to get Vets : {}", id);
        Optional<VetsDTO> vetsDTO = vetsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vetsDTO);
    }

    /**
     * {@code DELETE  /vets/:id} : delete the "id" vets.
     *
     * @param id the id of the vetsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vets/{id}")
    public ResponseEntity<Void> deleteVets(@PathVariable Long id) {
        log.debug("REST request to delete Vets : {}", id);
        vetsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
