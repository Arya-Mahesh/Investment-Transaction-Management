package com.rinar.trasactional_mgmt.service;

import com.rinar.trasactional_mgmt.dto.TransactionRequest;
import com.rinar.trasactional_mgmt.exception.BadRequestException;
import com.rinar.trasactional_mgmt.model.Transaction;
import com.rinar.trasactional_mgmt.repo.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

        @Mock
        private TransactionRepository transactionRepository;

        @InjectMocks
        private TransactionService transactionService;

    @Test
    void buy_shouldSaveTransaction_whenValidRequest() {
        TransactionRequest request = new TransactionRequest("AAPL", 10, 100.0);

        transactionService.buy("arya", request);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void buy_shouldThrowException_whenQuantityIsZeroOrNegative() {
        TransactionRequest request = new TransactionRequest("AAPL", -5, 100.0);

        assertThrows(BadRequestException.class, () -> transactionService.buy("arya", request));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void buy_shouldThrowException_whenPriceIsZeroOrNegative() {
        TransactionRequest request = new TransactionRequest("AAPL", 10, -50.0);

        assertThrows(BadRequestException.class, () -> transactionService.buy("arya", request));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void buy_shouldCalculateTotalAmountCorrectly() {
        TransactionRequest request = new TransactionRequest("AAPL", 5, 200.0);

        transactionService.buy("arya", request);

        verify(transactionRepository).save(argThat(tx ->
                tx.getTotalAmount() == 1000.0 &&
                        "Buy".equals(tx.getTransactionType()) &&
                        "arya".equals(tx.getUsername())
        ));
    }

    // --- SELL TESTS ---

    @Test
    void sell_shouldSaveTransaction_whenValidRequest() {
        TransactionRequest request = new TransactionRequest("AAPL", 5, 150.0);

        transactionService.sell("arya", request);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void sell_shouldThrowException_whenQuantityIsInvalid() {
        TransactionRequest request = new TransactionRequest("AAPL", 0, 150.0);

        assertThrows(BadRequestException.class, () -> transactionService.sell("arya", request));
    }

    @Test
    void sell_shouldSetTransactionTypeToSell() {
        TransactionRequest request = new TransactionRequest("AAPL", 3, 100.0);

        transactionService.sell("arya", request);

        verify(transactionRepository).save(argThat(tx -> "Sell".equals(tx.getTransactionType())));
    }

    // --- HISTORY TESTS ---

    @Test
    void history_shouldReturnPagedTransactions_forGivenUser() {
        Transaction tx = new Transaction(1L, "arya", "AAPL", "Buy", 10, 100.0, 1000.0, LocalDateTime.now());
        PageRequest pageable = PageRequest.of(0, 10);

        when(transactionRepository.findByUsername("arya", pageable))
                .thenReturn(new PageImpl<>(List.of(tx)));

        var result = transactionService.history("arya", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("arya", result.getContent().get(0).getUsername());
    }

}


