package com.rinar.trasactional_mgmt.dto;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @NotBlank(message= "Asset name cannot be empty")
    private String assetName;
    @Positive(message = "Quantity must be greater than zero")
    private int quantity;
    @Positive(message = "Price must be greater than zero")
    private Double price;


}
