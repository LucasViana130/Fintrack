package com.projeto.fintrack.service;

import com.projeto.fintrack.domain.entity.Account;
import com.projeto.fintrack.domain.entity.Category;
import com.projeto.fintrack.domain.entity.Transaction;
import com.projeto.fintrack.domain.entity.User;
import com.projeto.fintrack.domain.exception.BusinessException;
import com.projeto.fintrack.domain.exception.ResourceNotFoundException;
import com.projeto.fintrack.dto.request.TransactionFilterRequest;
import com.projeto.fintrack.dto.request.TransactionRequest;
import com.projeto.fintrack.dto.response.TransactionResponse;
import com.projeto.fintrack.mapper.TransactionMapper;
import com.projeto.fintrack.repository.AccountRepository;
import com.projeto.fintrack.repository.CategoryRepository;
import com.projeto.fintrack.repository.TransactionRepository;
import com.projeto.fintrack.repository.spec.TransactionSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;
    private final UserService userService;

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        User user = userService.getAuthenticatedUser();

        Account account = getActiveAccountOfUser(request.accountId(), user.getId());
        Category category = getActiveCategoryForUser(request.categoryId(), user.getId());

        validateCategoryType(request, category);

        Transaction transaction = Transaction.builder()
                .user(user)
                .account(account)
                .category(category)
                .type(request.type())
                .amount(request.amount())
                .description(request.description())
                .date(request.date())
                .build();

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> listWithFilters(TransactionFilterRequest filter) {
        User user = userService.getAuthenticatedUser();

        PageRequest pageable = PageRequest.of(
                filter.pageNumber(),
                filter.pageSize(),
                Sort.by(Sort.Direction.DESC, "date", "createdAt")
        );

        return transactionRepository.findAll(
                TransactionSpec.filter(
                        user.getId(),
                        filter.startDate(),
                        filter.endDate(),
                        filter.type(),
                        filter.categoryId(),
                        filter.accountId()
                ),
                pageable
        ).map(transactionMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TransactionResponse findById(Long id) {
        User user = userService.getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transação", id));
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request) {
        User user = userService.getAuthenticatedUser();

        Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transação", id));

        Account account = getActiveAccountOfUser(request.accountId(), user.getId());
        Category category = getActiveCategoryForUser(request.categoryId(), user.getId());

        validateCategoryType(request, category);

        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setType(request.type());
        transaction.setAmount(request.amount());
        transaction.setDescription(request.description());
        transaction.setDate(request.date());

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public void delete(Long id) {
        User user = userService.getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transação", id));
        transactionRepository.delete(transaction);
    }

    private Account getActiveAccountOfUser(Long accountId, Long userId) {
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta", accountId));
        if (!account.getActive()) {
            throw new BusinessException("A conta selecionada está inativa.");
        }
        return account;
    }

    private Category getActiveCategoryForUser(Long categoryId, Long userId) {
        return categoryRepository.findAvailableByIdForUser(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", categoryId));
    }

    private void validateCategoryType(TransactionRequest request, Category category) {
        if (!category.getType().equals(request.type())) {
            throw new BusinessException(
                    "O tipo da transação (" + request.type() + ") não é compatível com o tipo da categoria (" + category.getType() + ")."
            );
        }
    }
}