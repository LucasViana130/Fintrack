package com.projeto.fintrack.mapper;

import com.projeto.fintrack.DTO.response.CategoryResponse;
import com.projeto.fintrack.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "isDefault", expression = "java(category.isDefault())")
    CategoryResponse toResponse(Category category);
}
