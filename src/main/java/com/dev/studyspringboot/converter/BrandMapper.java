package com.dev.studyspringboot.converter;

import com.dev.studyspringboot.model.Brand;
import com.dev.studyspringboot.response.brand.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    @Mappings({
            @Mapping(target = "name", source = "name", qualifiedByName = "getNameCustom"),
//            @Mapping(target = "name", source = "name", qualifiedByName = "getNameCustom"),
            @Mapping(target = "address", source = "address"),
            @Mapping(target = "imageUrl", source = "imageUrl")
    })
    BrandResponse brandToBrandResponse(Brand brand);

    @Named("getNameCustom")
    default String getNameCustom(String name) {
        return "Custom: " + name;
    }
}
