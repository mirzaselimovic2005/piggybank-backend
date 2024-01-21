package com.testing.piggybank;

import com.testing.piggybank.account.AccountRepository;
import com.testing.piggybank.account.AccountService;
import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // GETACCOUNT TEST METHODES
    @Test
    void testGetAccountSuccess() {
        long accountId = 1;
        Account mockAccount = new Account();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        Optional<Account> result = accountService.getAccount(accountId);

        assertTrue(result.isPresent());
        assertEquals(mockAccount, result.get());
        verify(accountRepository).findById(accountId);
    }

    @Test
    void testGetAccountNotFound() {
        long accountId = 1;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Optional<Account> result = accountService.getAccount(accountId);

        assertFalse(result.isPresent());
        verify(accountRepository).findById(accountId);
    }

    // GETACCOUNTBYUSERID TEST METHODES

    @Test
    void testGetAccountsByUserIdSuccess() {
        long userId = 1;
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account());
        mockAccounts.add(new Account());

        when(accountRepository.findAllByUserId(userId)).thenReturn(mockAccounts);

        List<Account> result = accountService.getAccountsByUserId(userId);

        assertEquals(mockAccounts.size(), result.size());
        assertEquals(mockAccounts, result);
        verify(accountRepository).findAllByUserId(userId);
    }

    @Test
    void testGetAccountsByUserIdNotFound() {
        long userId = 1;
        when(accountRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>());

        List<Account> result = accountService.getAccountsByUserId(userId);

        assertTrue(result.isEmpty());
        verify(accountRepository).findAllByUserId(userId);
    }

    // UPDATEBALANCE TEST METHODES
    @Test
    void testUpdateBalanceCredit() {
        long accountId = 1;
        BigDecimal initialBalance = new BigDecimal("100.00");
        Account mockAccount = new Account();
        mockAccount.setBalance(initialBalance);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        BigDecimal amountToAdd = new BigDecimal("50.00");
        accountService.updateBalance(accountId, amountToAdd, Direction.CREDIT);

        BigDecimal expectedBalance = initialBalance.subtract(amountToAdd);
        assertEquals(expectedBalance, mockAccount.getBalance());
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void testUpdateBalanceDebit() {
        long accountId = 1;
        BigDecimal initialBalance = new BigDecimal("100.00");
        Account mockAccount = new Account();
        mockAccount.setBalance(initialBalance);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        BigDecimal amountToAdd = new BigDecimal("30.00");
        accountService.updateBalance(accountId, amountToAdd, Direction.DEBIT);

        BigDecimal expectedBalance = initialBalance.add(amountToAdd);
        assertEquals(expectedBalance, mockAccount.getBalance());
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void testUpdateBalanceAccountNotFound() {
        long accountId = 1;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            accountService.updateBalance(accountId, new BigDecimal("50.00"), Direction.CREDIT);
        });

        verify(accountRepository, never()).save(any());
    }


}
