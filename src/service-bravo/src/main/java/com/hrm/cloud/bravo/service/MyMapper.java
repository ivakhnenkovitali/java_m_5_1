package com.hrm.cloud.bravo.service;

import com.hrm.cloud.bravo.data.MyEntity;
import com.hrm.cloud.bravo.dto.MyDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MyMapper {

    MyEntity toEntity(MyDto myDto);

    MyDto toDto(MyEntity myEntity);

}
