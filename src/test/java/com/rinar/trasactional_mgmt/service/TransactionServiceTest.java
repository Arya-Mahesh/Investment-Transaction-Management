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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

        @Mock
        private TransactionRepository transactionRepository;

        @InjectMocks
        private TransactionService transactionService;

        @Test
        void buy_shouldSaveTransaction_whenValidRequest() {

            TransactionRequest request = new TransactionRequest();
            request.setAssetName("AAPL");
            request.setQuantity(10);
            request.setPrice(100.0);

            transactionService.buy("arya", request);

            verify(transactionRepository, times(1)).save(any(Transaction.class));
        }

        @Test
        void buy_shouldThrowException_whenQuantityIsInvalid() {

            TransactionRequest request = new TransactionRequest();
            request.setAssetName("AAPL");
            request.setQuantity(-5);
            request.setPrice(100.0);

            assertThrows(
                    BadRequestException.class,
                    () -> transactionService.buy("arya", request)
            );
        }

}


