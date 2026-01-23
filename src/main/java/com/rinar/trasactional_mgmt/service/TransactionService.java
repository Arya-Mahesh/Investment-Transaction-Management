package com.rinar.trasactional_mgmt.service;

import com.rinar.trasactional_mgmt.dto.TransactionRequest;
import com.rinar.trasactional_mgmt.exception.BadRequestException;
import com.rinar.trasactional_mgmt.model.Transaction;
import com.rinar.trasactional_mgmt.repo.TransactionRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void buy(String username, TransactionRequest request){

        if(request.getQuantity() <= 0){
            throw new BadRequestException("Quantity must be greater than zero");

        }
        if (request.getPrice() <= 0) {
            throw new BadRequestException("Price must be greater than zero");
        }

        Transaction tx = new Transaction();
        tx.setUsername(username);
        tx.setAssetName(request.getAssetName());
        tx.setTransactionType("Buy");
        tx.setQuantity(request.getQuantity());
        tx.setPrice(request.getPrice());
        tx.setTotalAmount(request.getQuantity() * request.getPrice());
        tx.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(tx);
    }

    public void sell(String username,TransactionRequest request){
        if(request.getQuantity() <= 0){
            throw new BadRequestException("Quantity must be greater than zero");

        }
        if (request.getPrice() <= 0) {
            throw new BadRequestException("Price must be greater than zero");
        }
        Transaction tx = new Transaction();
        tx.setUsername(username);
        tx.setAssetName(request.getAssetName());
        tx.setTransactionType("Sell");
        tx.setQuantity(request.getQuantity());
        tx.setPrice(request.getPrice());
        tx.setTotalAmount(request.getQuantity() * request.getPrice());
        tx.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(tx);
    }

    public Page<Transaction> history(String username, Pageable pageable){
        return transactionRepository.findByUsername(username, pageable);
    }

    public Page<Transaction> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public void exportAllTransactionsToExcel(HttpServletResponse response) {

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {

            Sheet sheet = workbook.createSheet("Transactions");

            // Header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Username");
            header.createCell(2).setCellValue("Asset");
            header.createCell(3).setCellValue("Type");
            header.createCell(4).setCellValue("Quantity");
            header.createCell(5).setCellValue("Price");
            header.createCell(6).setCellValue("Total");
            header.createCell(7).setCellValue("Created At");

            int rowIndex = 1;

            // Stream data page by page
            int page = 0;
            int size = 1000;

            Page<Transaction> transactionPage;

            do {
                Pageable pageable = PageRequest.of(page, size);
                transactionPage = transactionRepository.findAll(pageable);

                for (Transaction tx : transactionPage.getContent()) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(tx.getId());
                    row.createCell(1).setCellValue(tx.getUsername());
                    row.createCell(2).setCellValue(tx.getAssetName());
                    row.createCell(3).setCellValue(tx.getTransactionType());
                    row.createCell(4).setCellValue(tx.getQuantity());
                    row.createCell(5).setCellValue(tx.getPrice());
                    row.createCell(6).setCellValue(tx.getTotalAmount());
                    row.createCell(7).setCellValue(tx.getCreatedAt().toString());
                }

                page++;

            } while (transactionPage.hasNext());

            // Response config
            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=transactions.xlsx"
            );

            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();

            workbook.dispose(); // VERY IMPORTANT (frees temp files)

        } catch (Exception e) {
            throw new RuntimeException("Failed to export Excel file", e);
        }
    }
}
