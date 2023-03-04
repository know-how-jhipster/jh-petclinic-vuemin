package org.ujar.jh.petclinic.vuemin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.ujar.jh.petclinic.vuemin.IntegrationTest;
import org.ujar.jh.petclinic.vuemin.domain.Specialties;
import org.ujar.jh.petclinic.vuemin.repository.SpecialtiesRepository;
import org.ujar.jh.petclinic.vuemin.service.SpecialtiesService;
import org.ujar.jh.petclinic.vuemin.service.dto.SpecialtiesDTO;
import org.ujar.jh.petclinic.vuemin.service.mapper.SpecialtiesMapper;

/**
 * Integration tests for the {@link SpecialtiesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SpecialtiesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specialties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialtiesRepository specialtiesRepository;

    @Mock
    private SpecialtiesRepository specialtiesRepositoryMock;

    @Autowired
    private SpecialtiesMapper specialtiesMapper;

    @Mock
    private SpecialtiesService specialtiesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialtiesMockMvc;

    private Specialties specialties;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialties createEntity(EntityManager em) {
        Specialties specialties = new Specialties().name(DEFAULT_NAME);
        return specialties;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialties createUpdatedEntity(EntityManager em) {
        Specialties specialties = new Specialties().name(UPDATED_NAME);
        return specialties;
    }

    @BeforeEach
    public void initTest() {
        specialties = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialties() throws Exception {
        int databaseSizeBeforeCreate = specialtiesRepository.findAll().size();
        // Create the Specialties
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(specialties);
        restSpecialtiesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialtiesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeCreate + 1);
        Specialties testSpecialties = specialtiesList.get(specialtiesList.size() - 1);
        assertThat(testSpecialties.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSpecialtiesWithExistingId() throws Exception {
        // Create the Specialties with an existing ID
        specialties.setId(1L);
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(specialties);

        int databaseSizeBeforeCreate = specialtiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialtiesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialtiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialtiesRepository.findAll().size();
        // set the field null
        specialties.setName(null);

        // Create the Specialties, which fails.
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(specialties);

        restSpecialtiesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialtiesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialties() throws Exception {
        // Initialize the database
        specialtiesRepository.saveAndFlush(specialties);

        // Get all the specialtiesList
        restSpecialtiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialties.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecialtiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(specialtiesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecialtiesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(specialtiesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecialtiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(specialtiesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecialtiesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(specialtiesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSpecialties() throws Exception {
        // Initialize the database
        specialtiesRepository.saveAndFlush(specialties);

        // Get the specialties
        restSpecialtiesMockMvc
            .perform(get(ENTITY_API_URL_ID, specialties.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialties.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSpecialties() throws Exception {
        // Get the specialties
        restSpecialtiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialties() throws Exception {
        // Initialize the database
        specialtiesRepository.saveAndFlush(specialties);

        int databaseSizeBeforeUpdate = specialtiesRepository.findAll().size();

        // Update the specialties
        Specialties updatedSpecialties = specialtiesRepository.findById(specialties.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialties are not directly saved in db
        em.detach(updatedSpecialties);
        updatedSpecialties.name(UPDATED_NAME);
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(updatedSpecialties);

        restSpecialtiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialtiesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtiesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeUpdate);
        Specialties testSpecialties = specialtiesList.get(specialtiesList.size() - 1);
        assertThat(testSpecialties.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSpecialties() throws Exception {
        int databaseSizeBeforeUpdate = specialtiesRepository.findAll().size();
        specialties.setId(count.incrementAndGet());

        // Create the Specialties
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(specialties);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialtiesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialties() throws Exception {
        int databaseSizeBeforeUpdate = specialtiesRepository.findAll().size();
        specialties.setId(count.incrementAndGet());

        // Create the Specialties
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(specialties);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialtiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialties() throws Exception {
        int databaseSizeBeforeUpdate = specialtiesRepository.findAll().size();
        specialties.setId(count.incrementAndGet());

        // Create the Specialties
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(specialties);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtiesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialtiesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialtiesWithPatch() throws Exception {
        // Initialize the database
        specialtiesRepository.saveAndFlush(specialties);

        int databaseSizeBeforeUpdate = specialtiesRepository.findAll().size();

        // Update the specialties using partial update
        Specialties partialUpdatedSpecialties = new Specialties();
        partialUpdatedSpecialties.setId(specialties.getId());

        partialUpdatedSpecialties.name(UPDATED_NAME);

        restSpecialtiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialties.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialties))
            )
            .andExpect(status().isOk());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeUpdate);
        Specialties testSpecialties = specialtiesList.get(specialtiesList.size() - 1);
        assertThat(testSpecialties.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSpecialtiesWithPatch() throws Exception {
        // Initialize the database
        specialtiesRepository.saveAndFlush(specialties);

        int databaseSizeBeforeUpdate = specialtiesRepository.findAll().size();

        // Update the specialties using partial update
        Specialties partialUpdatedSpecialties = new Specialties();
        partialUpdatedSpecialties.setId(specialties.getId());

        partialUpdatedSpecialties.name(UPDATED_NAME);

        restSpecialtiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialties.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialties))
            )
            .andExpect(status().isOk());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeUpdate);
        Specialties testSpecialties = specialtiesList.get(specialtiesList.size() - 1);
        assertThat(testSpecialties.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialties() throws Exception {
        int databaseSizeBeforeUpdate = specialtiesRepository.findAll().size();
        specialties.setId(count.incrementAndGet());

        // Create the Specialties
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(specialties);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialtiesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialtiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialties() throws Exception {
        int databaseSizeBeforeUpdate = specialtiesRepository.findAll().size();
        specialties.setId(count.incrementAndGet());

        // Create the Specialties
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(specialties);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialtiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialties() throws Exception {
        int databaseSizeBeforeUpdate = specialtiesRepository.findAll().size();
        specialties.setId(count.incrementAndGet());

        // Create the Specialties
        SpecialtiesDTO specialtiesDTO = specialtiesMapper.toDto(specialties);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtiesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(specialtiesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialties in the database
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialties() throws Exception {
        // Initialize the database
        specialtiesRepository.saveAndFlush(specialties);

        int databaseSizeBeforeDelete = specialtiesRepository.findAll().size();

        // Delete the specialties
        restSpecialtiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialties.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specialties> specialtiesList = specialtiesRepository.findAll();
        assertThat(specialtiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
