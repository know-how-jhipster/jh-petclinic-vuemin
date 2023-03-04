package org.ujar.jh.petclinic.vuemin.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ujar.jh.petclinic.vuemin.domain.Owners;
import org.ujar.jh.petclinic.vuemin.repository.OwnersRepository;
import org.ujar.jh.petclinic.vuemin.service.OwnersService;
import org.ujar.jh.petclinic.vuemin.service.dto.OwnersDTO;
import org.ujar.jh.petclinic.vuemin.service.mapper.OwnersMapper;

/**
 * Service Implementation for managing {@link Owners}.
 */
@Service
@Transactional
public class OwnersServiceImpl implements OwnersService {

    private final Logger log = LoggerFactory.getLogger(OwnersServiceImpl.class);

    private final OwnersRepository ownersRepository;

    private final OwnersMapper ownersMapper;

    public OwnersServiceImpl(OwnersRepository ownersRepository, OwnersMapper ownersMapper) {
        this.ownersRepository = ownersRepository;
        this.ownersMapper = ownersMapper;
    }

    @Override
    public OwnersDTO save(OwnersDTO ownersDTO) {
        log.debug("Request to save Owners : {}", ownersDTO);
        Owners owners = ownersMapper.toEntity(ownersDTO);
        owners = ownersRepository.save(owners);
        return ownersMapper.toDto(owners);
    }

    @Override
    public OwnersDTO update(OwnersDTO ownersDTO) {
        log.debug("Request to update Owners : {}", ownersDTO);
        Owners owners = ownersMapper.toEntity(ownersDTO);
        owners = ownersRepository.save(owners);
        return ownersMapper.toDto(owners);
    }

    @Override
    public Optional<OwnersDTO> partialUpdate(OwnersDTO ownersDTO) {
        log.debug("Request to partially update Owners : {}", ownersDTO);

        return ownersRepository
            .findById(ownersDTO.getId())
            .map(existingOwners -> {
                ownersMapper.partialUpdate(existingOwners, ownersDTO);

                return existingOwners;
            })
            .map(ownersRepository::save)
            .map(ownersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OwnersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Owners");
        return ownersRepository.findAll(pageable).map(ownersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OwnersDTO> findOne(Long id) {
        log.debug("Request to get Owners : {}", id);
        return ownersRepository.findById(id).map(ownersMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Owners : {}", id);
        ownersRepository.deleteById(id);
    }
}
