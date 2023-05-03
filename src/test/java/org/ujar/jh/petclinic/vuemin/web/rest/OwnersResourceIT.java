package org.ujar.jh.petclinic.vuemin.web.rest;

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
import org.ujar.jh.petclinic.vuemin.IntegrationTest;
import org.ujar.jh.petclinic.vuemin.domain.Owners;
import org.ujar.jh.petclinic.vuemin.repository.OwnersRepository;
import org.ujar.jh.petclinic.vuemin.service.dto.OwnersDTO;
import org.ujar.jh.petclinic.vuemin.service.mapper.OwnersMapper;

/**
 * Integration tests for the {@link OwnersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OwnersResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OwnersRepository ownersRepository;

    @Autowired
    private OwnersMapper ownersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOwnersMockMvc;

    private Owners owners;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owners createEntity(EntityManager em) {
        Owners owners = new Owners()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .telephone(DEFAULT_TELEPHONE);
        return owners;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owners createUpdatedEntity(EntityManager em) {
        Owners owners = new Owners()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);
        return owners;
    }

    @BeforeEach
    public void initTest() {
        owners = createEntity(em);
    }

    @Test
    @Transactional
    void createOwners() throws Exception {
        int databaseSizeBeforeCreate = ownersRepository.findAll().size();
        // Create the Owners
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);
        restOwnersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownersDTO)))
            .andExpect(status().isCreated());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeCreate + 1);
        Owners testOwners = ownersList.get(ownersList.size() - 1);
        assertThat(testOwners.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testOwners.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testOwners.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOwners.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testOwners.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void createOwnersWithExistingId() throws Exception {
        // Create the Owners with an existing ID
        owners.setId(1L);
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        int databaseSizeBeforeCreate = ownersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownersRepository.findAll().size();
        // set the field null
        owners.setFirstname(null);

        // Create the Owners, which fails.
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        restOwnersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownersDTO)))
            .andExpect(status().isBadRequest());

        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownersRepository.findAll().size();
        // set the field null
        owners.setLastname(null);

        // Create the Owners, which fails.
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        restOwnersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownersDTO)))
            .andExpect(status().isBadRequest());

        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownersRepository.findAll().size();
        // set the field null
        owners.setAddress(null);

        // Create the Owners, which fails.
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        restOwnersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownersDTO)))
            .andExpect(status().isBadRequest());

        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownersRepository.findAll().size();
        // set the field null
        owners.setTelephone(null);

        // Create the Owners, which fails.
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        restOwnersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownersDTO)))
            .andExpect(status().isBadRequest());

        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOwners() throws Exception {
        // Initialize the database
        ownersRepository.saveAndFlush(owners);

        // Get all the ownersList
        restOwnersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owners.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    void getOwners() throws Exception {
        // Initialize the database
        ownersRepository.saveAndFlush(owners);

        // Get the owners
        restOwnersMockMvc
            .perform(get(ENTITY_API_URL_ID, owners.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(owners.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    void getNonExistingOwners() throws Exception {
        // Get the owners
        restOwnersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOwners() throws Exception {
        // Initialize the database
        ownersRepository.saveAndFlush(owners);

        int databaseSizeBeforeUpdate = ownersRepository.findAll().size();

        // Update the owners
        Owners updatedOwners = ownersRepository.findById(owners.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOwners are not directly saved in db
        em.detach(updatedOwners);
        updatedOwners
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);
        OwnersDTO ownersDTO = ownersMapper.toDto(updatedOwners);

        restOwnersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ownersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeUpdate);
        Owners testOwners = ownersList.get(ownersList.size() - 1);
        assertThat(testOwners.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testOwners.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testOwners.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOwners.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOwners.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void putNonExistingOwners() throws Exception {
        int databaseSizeBeforeUpdate = ownersRepository.findAll().size();
        owners.setId(count.incrementAndGet());

        // Create the Owners
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ownersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOwners() throws Exception {
        int databaseSizeBeforeUpdate = ownersRepository.findAll().size();
        owners.setId(count.incrementAndGet());

        // Create the Owners
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ownersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOwners() throws Exception {
        int databaseSizeBeforeUpdate = ownersRepository.findAll().size();
        owners.setId(count.incrementAndGet());

        // Create the Owners
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ownersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOwnersWithPatch() throws Exception {
        // Initialize the database
        ownersRepository.saveAndFlush(owners);

        int databaseSizeBeforeUpdate = ownersRepository.findAll().size();

        // Update the owners using partial update
        Owners partialUpdatedOwners = new Owners();
        partialUpdatedOwners.setId(owners.getId());

        partialUpdatedOwners.address(UPDATED_ADDRESS).telephone(UPDATED_TELEPHONE);

        restOwnersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwners.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwners))
            )
            .andExpect(status().isOk());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeUpdate);
        Owners testOwners = ownersList.get(ownersList.size() - 1);
        assertThat(testOwners.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testOwners.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testOwners.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOwners.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testOwners.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void fullUpdateOwnersWithPatch() throws Exception {
        // Initialize the database
        ownersRepository.saveAndFlush(owners);

        int databaseSizeBeforeUpdate = ownersRepository.findAll().size();

        // Update the owners using partial update
        Owners partialUpdatedOwners = new Owners();
        partialUpdatedOwners.setId(owners.getId());

        partialUpdatedOwners
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);

        restOwnersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwners.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwners))
            )
            .andExpect(status().isOk());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeUpdate);
        Owners testOwners = ownersList.get(ownersList.size() - 1);
        assertThat(testOwners.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testOwners.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testOwners.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOwners.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOwners.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void patchNonExistingOwners() throws Exception {
        int databaseSizeBeforeUpdate = ownersRepository.findAll().size();
        owners.setId(count.incrementAndGet());

        // Create the Owners
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ownersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOwners() throws Exception {
        int databaseSizeBeforeUpdate = ownersRepository.findAll().size();
        owners.setId(count.incrementAndGet());

        // Create the Owners
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ownersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOwners() throws Exception {
        int databaseSizeBeforeUpdate = ownersRepository.findAll().size();
        owners.setId(count.incrementAndGet());

        // Create the Owners
        OwnersDTO ownersDTO = ownersMapper.toDto(owners);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ownersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owners in the database
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOwners() throws Exception {
        // Initialize the database
        ownersRepository.saveAndFlush(owners);

        int databaseSizeBeforeDelete = ownersRepository.findAll().size();

        // Delete the owners
        restOwnersMockMvc
            .perform(delete(ENTITY_API_URL_ID, owners.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Owners> ownersList = ownersRepository.findAll();
        assertThat(ownersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
