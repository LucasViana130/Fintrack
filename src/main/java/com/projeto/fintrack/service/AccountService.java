package com.projeto.fintrack.service;

import com.projeto.fintrack.DTO.request.AccountRequest;
import com.projeto.fintrack.DTO.response.AccountResponse;
import com.projeto.fintrack.domain.entity.Account;
import com.projeto.fintrack.domain.entity.User;
import com.projeto.fintrack.domain.exception.BusinessException;
import com.projeto.fintrack.domain.exception.ResourceNotFoundException;
import com.projeto.fintrack.mapper.AccountMapper;
import com.projeto.fintrack.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;

    @Transactional
    public AccountResponse create(AccountRequest request) {
        User user = userService.getAuthenticatedUser();

        if (accountRepository.existsByNameAndUserIdAndActiveTrue(request.name(), user.getId())) {
            throw new BusinessException("Você já possui uma conta ativa com este nome.");
        }

        Account account = Account.builder()
                .user(user)
                .name(request.name())
                .type(request.type())
                .description(request.description())
                .active(true)
                .build();

        Account saved = accountRepository.save(account);
        BigDecimal balance = accountRepository.calculateBalance(saved.getId());
        return accountMapper.toResponseWithBalance(saved, balance);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> listAll() {
        User user = userService.getAuthenticatedUser();

        return accountRepository.findByUserIdAndActiveTrue(user.getId())
                .stream()
                .map(account -> {
                    BigDecimal balance = accountRepository.calculateBalance(account.getId());
                    return accountMapper.toResponseWithBalance(account, balance);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountResponse findById(Long id) {
        User user = userService.getAuthenticatedUser();
        Account account = getAccountOfUser(id, user.getId());
        BigDecimal balance = accountRepository.calculateBalance(id);
        return accountMapper.toResponseWithBalance(account, balance);
    }

    @Transactional
    public AccountResponse update(Long id, AccountRequest request) {
        User user = userService.getAuthenticatedUser();
        Account account = getAccountOfUser(id, user.getId());

        boolean nameChanged = !account.getName().equalsIgnoreCase(request.name());
        if (nameChanged && accountRepository.existsByNameAndUserIdAndActiveTrue(request.name(), user.getId())) {
            throw new BusinessException("Você já possui uma conta ativa com este nome.");
        }

        account.setName(request.name());
        account.setDescription(request.description());

        Account saved = accountRepository.save(account);
        BigDecimal balance = accountRepository.calculateBalance(saved.getId());
        return accountMapper.toResponseWithBalance(saved, balance);
    }

    @Transactional
    public void deactivate(Long id) {
        User user = userService.getAuthenticatedUser();
        Account account = getAccountOfUser(id, user.getId());
        account.setActive(false);
        accountRepository.save(account);
    }

    private Account getAccountOfUser(Long accountId, Long userId) {
        return accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta", accountId));
    }
}
