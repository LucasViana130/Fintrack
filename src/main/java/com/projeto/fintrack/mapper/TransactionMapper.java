package com.projeto.fintrack.mapper;

import com.projeto.fintrack.dto.response.TransactionResponse;
import com.projeto.fintrack.domain.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, CategoryMapper.class})
public interface TransactionMapper {

    @Mapping(target = "account", source = "account")
    @Mapping(target = "category", source = "category")
    TransactionResponse toResponse(Transaction transaction);
}
