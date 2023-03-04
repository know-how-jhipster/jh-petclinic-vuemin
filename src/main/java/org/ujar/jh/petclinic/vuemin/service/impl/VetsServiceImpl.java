package org.ujar.jh.petclinic.vuemin.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ujar.jh.petclinic.vuemin.domain.Vets;
import org.ujar.jh.petclinic.vuemin.repository.VetsRepository;
import org.ujar.jh.petclinic.vuemin.service.VetsService;
import org.ujar.jh.petclinic.vuemin.service.dto.VetsDTO;
import org.ujar.jh.petclinic.vuemin.service.mapper.VetsMapper;

/**
 * Service Implementation for managing {@link Vets}.
 */
@Service
@Transactional
public class VetsServiceImpl implements VetsService {

    private final Logger log = LoggerFactory.getLogger(VetsServiceImpl.class);

    private final VetsRepository vetsRepository;

    private final VetsMapper vetsMapper;

    public VetsServiceImpl(VetsRepository vetsRepository, VetsMapper vetsMapper) {
        this.vetsRepository = vetsRepository;
        this.vetsMapper = vetsMapper;
    }

    @Override
    public VetsDTO save(VetsDTO vetsDTO) {
        log.debug("Request to save Vets : {}", vetsDTO);
        Vets vets = vetsMapper.toEntity(vetsDTO);
        vets = vetsRepository.save(vets);
        return vetsMapper.toDto(vets);
    }

    @Override
    public VetsDTO update(VetsDTO vetsDTO) {
        log.debug("Request to update Vets : {}", vetsDTO);
        Vets vets = vetsMapper.toEntity(vetsDTO);
        vets = vetsRepository.save(vets);
        return vetsMapper.toDto(vets);
    }

    @Override
    public Optional<VetsDTO> partialUpdate(VetsDTO vetsDTO) {
        log.debug("Request to partially update Vets : {}", vetsDTO);

        return vetsRepository
            .findById(vetsDTO.getId())
            .map(existingVets -> {
                vetsMapper.partialUpdate(existingVets, vetsDTO);

                return existingVets;
            })
            .map(vetsRepository::save)
            .map(vetsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VetsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vets");
        return vetsRepository.findAll(pageable).map(vetsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VetsDTO> findOne(Long id) {
        log.debug("Request to get Vets : {}", id);
        return vetsRepository.findById(id).map(vetsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vets : {}", id);
        vetsRepository.deleteById(id);
    }
}
