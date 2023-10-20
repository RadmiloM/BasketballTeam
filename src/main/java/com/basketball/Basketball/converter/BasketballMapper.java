package com.basketball.Basketball.converter;

import com.basketball.Basketball.dto.BasketballDTO;
import com.basketball.Basketball.entity.Basketball;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BasketballMapper {
    Basketball mapToEntity(BasketballDTO basketballDTO);

    BasketballDTO mapToDTO(Basketball basketball);
}
