package dev.knowhowto.jh.petclinic.vuemin.service.mapper;

import org.mapstruct.*;
import dev.knowhowto.jh.petclinic.vuemin.domain.Types;
import dev.knowhowto.jh.petclinic.vuemin.service.dto.TypesDTO;

/**
 * Mapper for the entity {@link Types} and its DTO {@link TypesDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypesMapper extends EntityMapper<TypesDTO, Types> {}
