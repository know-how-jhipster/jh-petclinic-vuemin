package dev.knowhowto.jh.petclinic.vuemin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import dev.knowhowto.jh.petclinic.vuemin.IntegrationTest;
import dev.knowhowto.jh.petclinic.vuemin.domain.Vets;
import dev.knowhowto.jh.petclinic.vuemin.repository.VetsRepository;
import dev.knowhowto.jh.petclinic.vuemin.service.dto.VetsDTO;
import dev.knowhowto.jh.petclinic.vuemin.service.mapper.VetsMapper;

/**
 * Integration tests for the {@link VetsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VetsResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VetsRepository vetsRepository;

    @Autowired
    private VetsMapper vetsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVetsMockMvc;

    private Vets vets;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vets createEntity(EntityManager em) {
        Vets vets = new Vets().firstname(DEFAULT_FIRSTNAME).lastname(DEFAULT_LASTNAME);
        return vets;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vets createUpdatedEntity(EntityManager em) {
        Vets vets = new Vets().firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);
        return vets;
    }

    @BeforeEach
    public void initTest() {
        vets = createEntity(em);
    }

    @Test
    @Transactional
    void createVets() throws Exception {
        int databaseSizeBeforeCreate = vetsRepository.findAll().size();
        // Create the Vets
        VetsDTO vetsDTO = vetsMapper.toDto(vets);
        restVetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetsDTO)))
            .andExpect(status().isCreated());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeCreate + 1);
        Vets testVets = vetsList.get(vetsList.size() - 1);
        assertThat(testVets.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testVets.getLastname()).isEqualTo(DEFAULT_LASTNAME);
    }

    @Test
    @Transactional
    void createVetsWithExistingId() throws Exception {
        // Create the Vets with an existing ID
        vets.setId(1L);
        VetsDTO vetsDTO = vetsMapper.toDto(vets);

        int databaseSizeBeforeCreate = vetsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vetsRepository.findAll().size();
        // set the field null
        vets.setFirstname(null);

        // Create the Vets, which fails.
        VetsDTO vetsDTO = vetsMapper.toDto(vets);

        restVetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetsDTO)))
            .andExpect(status().isBadRequest());

        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vetsRepository.findAll().size();
        // set the field null
        vets.setLastname(null);

        // Create the Vets, which fails.
        VetsDTO vetsDTO = vetsMapper.toDto(vets);

        restVetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetsDTO)))
            .andExpect(status().isBadRequest());

        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVets() throws Exception {
        // Initialize the database
        vetsRepository.saveAndFlush(vets);

        // Get all the vetsList
        restVetsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vets.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)));
    }

    @Test
    @Transactional
    void getVets() throws Exception {
        // Initialize the database
        vetsRepository.saveAndFlush(vets);

        // Get the vets
        restVetsMockMvc
            .perform(get(ENTITY_API_URL_ID, vets.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vets.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME));
    }

    @Test
    @Transactional
    void getNonExistingVets() throws Exception {
        // Get the vets
        restVetsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVets() throws Exception {
        // Initialize the database
        vetsRepository.saveAndFlush(vets);

        int databaseSizeBeforeUpdate = vetsRepository.findAll().size();

        // Update the vets
        Vets updatedVets = vetsRepository.findById(vets.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVets are not directly saved in db
        em.detach(updatedVets);
        updatedVets.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);
        VetsDTO vetsDTO = vetsMapper.toDto(updatedVets);

        restVetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vetsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vetsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeUpdate);
        Vets testVets = vetsList.get(vetsList.size() - 1);
        assertThat(testVets.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testVets.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void putNonExistingVets() throws Exception {
        int databaseSizeBeforeUpdate = vetsRepository.findAll().size();
        vets.setId(count.incrementAndGet());

        // Create the Vets
        VetsDTO vetsDTO = vetsMapper.toDto(vets);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vetsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVets() throws Exception {
        int databaseSizeBeforeUpdate = vetsRepository.findAll().size();
        vets.setId(count.incrementAndGet());

        // Create the Vets
        VetsDTO vetsDTO = vetsMapper.toDto(vets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVets() throws Exception {
        int databaseSizeBeforeUpdate = vetsRepository.findAll().size();
        vets.setId(count.incrementAndGet());

        // Create the Vets
        VetsDTO vetsDTO = vetsMapper.toDto(vets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVetsWithPatch() throws Exception {
        // Initialize the database
        vetsRepository.saveAndFlush(vets);

        int databaseSizeBeforeUpdate = vetsRepository.findAll().size();

        // Update the vets using partial update
        Vets partialUpdatedVets = new Vets();
        partialUpdatedVets.setId(vets.getId());

        partialUpdatedVets.lastname(UPDATED_LASTNAME);

        restVetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVets))
            )
            .andExpect(status().isOk());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeUpdate);
        Vets testVets = vetsList.get(vetsList.size() - 1);
        assertThat(testVets.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testVets.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void fullUpdateVetsWithPatch() throws Exception {
        // Initialize the database
        vetsRepository.saveAndFlush(vets);

        int databaseSizeBeforeUpdate = vetsRepository.findAll().size();

        // Update the vets using partial update
        Vets partialUpdatedVets = new Vets();
        partialUpdatedVets.setId(vets.getId());

        partialUpdatedVets.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);

        restVetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVets))
            )
            .andExpect(status().isOk());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeUpdate);
        Vets testVets = vetsList.get(vetsList.size() - 1);
        assertThat(testVets.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testVets.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void patchNonExistingVets() throws Exception {
        int databaseSizeBeforeUpdate = vetsRepository.findAll().size();
        vets.setId(count.incrementAndGet());

        // Create the Vets
        VetsDTO vetsDTO = vetsMapper.toDto(vets);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vetsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVets() throws Exception {
        int databaseSizeBeforeUpdate = vetsRepository.findAll().size();
        vets.setId(count.incrementAndGet());

        // Create the Vets
        VetsDTO vetsDTO = vetsMapper.toDto(vets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVets() throws Exception {
        int databaseSizeBeforeUpdate = vetsRepository.findAll().size();
        vets.setId(count.incrementAndGet());

        // Create the Vets
        VetsDTO vetsDTO = vetsMapper.toDto(vets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vetsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vets in the database
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVets() throws Exception {
        // Initialize the database
        vetsRepository.saveAndFlush(vets);

        int databaseSizeBeforeDelete = vetsRepository.findAll().size();

        // Delete the vets
        restVetsMockMvc
            .perform(delete(ENTITY_API_URL_ID, vets.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vets> vetsList = vetsRepository.findAll();
        assertThat(vetsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
