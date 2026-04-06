package com.projeto.fintrack.mapper;

import com.projeto.fintrack.DTO.response.AccountResponse;
import com.projeto.fintrack.domain.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "balance", ignore = true)
    AccountResponse toResponse(Account account);

    default AccountResponse toResponseWithBalance(Account account, BigDecimal balance) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getType(),
                account.getDescription(),
                account.getActive(),
                balance,
                account.getCreatedAt()
        );
    }
}