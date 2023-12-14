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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import dev.knowhowto.jh.petclinic.vuemin.repository.OwnersRepository;
import dev.knowhowto.jh.petclinic.vuemin.service.OwnersService;
import dev.knowhowto.jh.petclinic.vuemin.service.dto.OwnersDTO;
import dev.knowhowto.jh.petclinic.vuemin.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dev.knowhowto.jh.petclinic.vuemin.domain.Owners}.
 */
@RestController
@RequestMapping("/api")
public class OwnersResource {

    private final Logger log = LoggerFactory.getLogger(OwnersResource.class);

    private static final String ENTITY_NAME = "owners";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OwnersService ownersService;

    private final OwnersRepository ownersRepository;

    public OwnersResource(OwnersService ownersService, OwnersRepository ownersRepository) {
        this.ownersService = ownersService;
        this.ownersRepository = ownersRepository;
    }

    /**
     * {@code POST  /owners} : Create a new owners.
     *
     * @param ownersDTO the ownersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ownersDTO, or with status {@code 400 (Bad Request)} if the owners has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/owners")
    public ResponseEntity<OwnersDTO> createOwners(@Valid @RequestBody OwnersDTO ownersDTO) throws URISyntaxException {
        log.debug("REST request to save Owners : {}", ownersDTO);
        if (ownersDTO.getId() != null) {
            throw new BadRequestAlertException("A new owners cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OwnersDTO result = ownersService.save(ownersDTO);
        return ResponseEntity
            .created(new URI("/api/owners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /owners/:id} : Updates an existing owners.
     *
     * @param id the id of the ownersDTO to save.
     * @param ownersDTO the ownersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownersDTO,
     * or with status {@code 400 (Bad Request)} if the ownersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ownersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/owners/{id}")
    public ResponseEntity<OwnersDTO> updateOwners(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OwnersDTO ownersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Owners : {}, {}", id, ownersDTO);
        if (ownersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OwnersDTO result = ownersService.update(ownersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ownersDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /owners/:id} : Partial updates given fields of an existing owners, field will ignore if it is null
     *
     * @param id the id of the ownersDTO to save.
     * @param ownersDTO the ownersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownersDTO,
     * or with status {@code 400 (Bad Request)} if the ownersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ownersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ownersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/owners/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OwnersDTO> partialUpdateOwners(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OwnersDTO ownersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Owners partially : {}, {}", id, ownersDTO);
        if (ownersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OwnersDTO> result = ownersService.partialUpdate(ownersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ownersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /owners} : get all the owners.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of owners in body.
     */
    @GetMapping("/owners")
    public ResponseEntity<List<OwnersDTO>> getAllOwners(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Owners");
        Page<OwnersDTO> page = ownersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /owners/:id} : get the "id" owners.
     *
     * @param id the id of the ownersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ownersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/owners/{id}")
    public ResponseEntity<OwnersDTO> getOwners(@PathVariable Long id) {
        log.debug("REST request to get Owners : {}", id);
        Optional<OwnersDTO> ownersDTO = ownersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ownersDTO);
    }

    /**
     * {@code DELETE  /owners/:id} : delete the "id" owners.
     *
     * @param id the id of the ownersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/owners/{id}")
    public ResponseEntity<Void> deleteOwners(@PathVariable Long id) {
        log.debug("REST request to delete Owners : {}", id);
        ownersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
