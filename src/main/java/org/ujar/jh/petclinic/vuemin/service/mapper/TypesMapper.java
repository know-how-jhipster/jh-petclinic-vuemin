package org.ujar.jh.petclinic.vuemin.service.mapper;

import org.mapstruct.*;
import org.ujar.jh.petclinic.vuemin.domain.Types;
import org.ujar.jh.petclinic.vuemin.service.dto.TypesDTO;

/**
 * Mapper for the entity {@link Types} and its DTO {@link TypesDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypesMapper extends EntityMapper<TypesDTO, Types> {}
