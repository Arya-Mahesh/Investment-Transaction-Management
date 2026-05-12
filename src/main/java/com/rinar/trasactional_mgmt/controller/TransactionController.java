package com.rinar.trasactional_mgmt.controller;

import com.rinar.trasactional_mgmt.dto.TransactionRequest;
import com.rinar.trasactional_mgmt.model.Transaction;
import com.rinar.trasactional_mgmt.service.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/buy")
    public String buy(@Valid @RequestBody TransactionRequest request, Authentication authentication){
        String username = authentication.getName();
        transactionService.buy(username,request);
        return "Buy transaction successful";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/sell")
    public String sell(@Valid @RequestBody TransactionRequest request,Authentication authentication){
        String username = authentication.getName();
        transactionService.sell(username,request);
        return "Sell transaction successful";
    }

    @GetMapping("/history")
    public Page<Transaction> history(Pageable pageable,Authentication authentication){
        String username = authentication.getName();
    return transactionService.history(username,pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public Page<Transaction> allTransactions(Pageable pageable, Authentication authentication) {
        log.debug("Admin {} fetching all transactions", authentication.getName());
        return transactionService.findAll(pageable);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export")
    public void exportTransactions(HttpServletResponse response) {
        transactionService.exportAllTransactionsToExcel(response);
    }

}
