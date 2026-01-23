package com.rinar.trasactional_mgmt.controller;

import com.rinar.trasactional_mgmt.dto.TransactionRequest;
import com.rinar.trasactional_mgmt.model.Transaction;
import com.rinar.trasactional_mgmt.service.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    @Autowired
    private TransactionService transactionService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/buy")
    public String buy(@Valid @RequestBody TransactionRequest request, Authentication authentication){
        String username = authentication.getName();
        transactionService.buy(username,request);
        return "Buy transaction successful";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/sell")
    public String sell(@RequestBody TransactionRequest request,Authentication authentication){
        String username = authentication.getName();
        transactionService.sell(username,request);
        return "Sell transaction successful";
    }

    @GetMapping
    public Page<Transaction> history(Pageable pageable,Authentication authentication){
        String username = authentication.getName();
    return transactionService.history(username,pageable);
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<Transaction> allTransactions(Pageable pageable) {
        System.out.println("AUTH = " + SecurityContextHolder.getContext().getAuthentication());
        return transactionService.findAll(pageable);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/export")
    public void exportTransactions(HttpServletResponse response) {
        transactionService.exportAllTransactionsToExcel(response);
    }

}
