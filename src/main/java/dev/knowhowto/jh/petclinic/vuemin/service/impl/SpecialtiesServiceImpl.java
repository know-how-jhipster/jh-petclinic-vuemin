package dev.knowhowto.jh.petclinic.vuemin.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.knowhowto.jh.petclinic.vuemin.domain.Specialties;
import dev.knowhowto.jh.petclinic.vuemin.repository.SpecialtiesRepository;
import dev.knowhowto.jh.petclinic.vuemin.service.SpecialtiesService;
import dev.knowhowto.jh.petclinic.vuemin.service.dto.SpecialtiesDTO;
import dev.knowhowto.jh.petclinic.vuemin.service.mapper.SpecialtiesMapper;

/**
 * Service Implementation for managing {@link Specialties}.
 */
@Service
@Transactional
public class SpecialtiesServiceImpl implements SpecialtiesService {

    private final Logger log = LoggerFactory.getLogger(SpecialtiesServiceImpl.class);

    private final SpecialtiesRepository specialtiesRepository;

    private final SpecialtiesMapper specialtiesMapper;

    public SpecialtiesServiceImpl(SpecialtiesRepository specialtiesRepository, SpecialtiesMapper specialtiesMapper) {
        this.specialtiesRepository = specialtiesRepository;
        this.specialtiesMapper = specialtiesMapper;
    }

    @Override
    public SpecialtiesDTO save(SpecialtiesDTO specialtiesDTO) {
        log.debug("Request to save Specialties : {}", specialtiesDTO);
        Specialties specialties = specialtiesMapper.toEntity(specialtiesDTO);
        specialties = specialtiesRepository.save(specialties);
        return specialtiesMapper.toDto(specialties);
    }

    @Override
    public SpecialtiesDTO update(SpecialtiesDTO specialtiesDTO) {
        log.debug("Request to update Specialties : {}", specialtiesDTO);
        Specialties specialties = specialtiesMapper.toEntity(specialtiesDTO);
        specialties = specialtiesRepository.save(specialties);
        return specialtiesMapper.toDto(specialties);
    }

    @Override
    public Optional<SpecialtiesDTO> partialUpdate(SpecialtiesDTO specialtiesDTO) {
        log.debug("Request to partially update Specialties : {}", specialtiesDTO);

        return specialtiesRepository
            .findById(specialtiesDTO.getId())
            .map(existingSpecialties -> {
                specialtiesMapper.partialUpdate(existingSpecialties, specialtiesDTO);

                return existingSpecialties;
            })
            .map(specialtiesRepository::save)
            .map(specialtiesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialtiesDTO> findAll() {
        log.debug("Request to get all Specialties");
        return specialtiesRepository.findAll().stream().map(specialtiesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<SpecialtiesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return specialtiesRepository.findAllWithEagerRelationships(pageable).map(specialtiesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpecialtiesDTO> findOne(Long id) {
        log.debug("Request to get Specialties : {}", id);
        return specialtiesRepository.findOneWithEagerRelationships(id).map(specialtiesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Specialties : {}", id);
        specialtiesRepository.deleteById(id);
    }
}
