package org.ujar.jh.petclinic.vuemin.service.mapper;

import org.mapstruct.*;
import org.ujar.jh.petclinic.vuemin.domain.Owners;
import org.ujar.jh.petclinic.vuemin.service.dto.OwnersDTO;

/**
 * Mapper for the entity {@link Owners} and its DTO {@link OwnersDTO}.
 */
@Mapper(componentModel = "spring")
public interface OwnersMapper extends EntityMapper<OwnersDTO, Owners> {}
