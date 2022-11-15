package com.tax.calculator.controller;

import com.tax.calculator.dto.PurchaseDTO;
import com.tax.calculator.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchases")
@AllArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping("/calculate")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseDTO calculatePurchaseValues(@RequestBody PurchaseDTO purchaseDTO) {
        return purchaseService.calculatePurchaseAmounts(purchaseDTO);
    }
}
