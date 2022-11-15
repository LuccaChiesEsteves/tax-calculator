package com.tax.calculator.service;

import com.tax.calculator.dto.PurchaseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PurchaseServiceTest {

    private final PurchaseService purchaseService = new PurchaseService();

    @Test
    public void calculatePurchaseAmountsInvalidTaxRateIllegalArgumentExceptionThrown() {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setVatRate(BigDecimal.ONE);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseService.calculatePurchaseAmounts(purchaseDTO));
        assertEquals("Invalid vat rate", exception.getMessage());
    }

    @Test
    public void calculatePurchaseAmountsNoAmountsInformedIllegalArgumentExceptionThrown() {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setVatRate(BigDecimal.valueOf(13));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseService.calculatePurchaseAmounts(purchaseDTO));
        assertEquals("At least one purchase amount should be provided", exception.getMessage());
    }

    @Test
    public void calculatePurchaseAmountsTwoAmountsInformedIllegalArgumentExceptionThrown() {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setVatRate(BigDecimal.valueOf(13));
        purchaseDTO.setNetAmount(BigDecimal.TEN);
        purchaseDTO.setGrossAmount(BigDecimal.TEN);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseService.calculatePurchaseAmounts(purchaseDTO));
        assertEquals("More than one purchase amount provided", exception.getMessage());
    }

    @Test
    public void calculatePurchaseAmountsGrossAmountInformedPurchaseAmountsReturned() {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setVatRate(BigDecimal.valueOf(13));
        purchaseDTO.setGrossAmount(BigDecimal.valueOf(23.50));
        PurchaseDTO result = purchaseService.calculatePurchaseAmounts(purchaseDTO);
        assertEquals(0, BigDecimal.valueOf(20.80).compareTo(result.getNetAmount()));
        assertEquals(0, BigDecimal.valueOf(2.70).compareTo(result.getVatAmount()));
        assertEquals(0, BigDecimal.valueOf(23.50).compareTo(result.getGrossAmount()));
    }

    @Test
    public void calculatePurchaseAmountsNetAmountInformedPurchaseAmountsReturned() {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setVatRate(BigDecimal.valueOf(10));
        purchaseDTO.setNetAmount(BigDecimal.valueOf(47.23));
        PurchaseDTO result = purchaseService.calculatePurchaseAmounts(purchaseDTO);
        assertEquals(0, BigDecimal.valueOf(47.23).compareTo(result.getNetAmount()));
        assertEquals(0, BigDecimal.valueOf(4.72).compareTo(result.getVatAmount()));
        assertEquals(0, BigDecimal.valueOf(51.95).compareTo(result.getGrossAmount()));
    }

    @Test
    public void calculatePurchaseAmountsVatAmountInformedPurchaseAmountsReturned() {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setVatRate(BigDecimal.valueOf(20));
        purchaseDTO.setVatAmount(BigDecimal.valueOf(105.67));
        PurchaseDTO result = purchaseService.calculatePurchaseAmounts(purchaseDTO);
        assertEquals(0, BigDecimal.valueOf(528.35).compareTo(result.getNetAmount()));
        assertEquals(0, BigDecimal.valueOf(105.67).compareTo(result.getVatAmount()));
        assertEquals(0, BigDecimal.valueOf(634.02).compareTo(result.getGrossAmount()));
    }
}
