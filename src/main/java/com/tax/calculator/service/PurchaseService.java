package com.tax.calculator.service;

import com.tax.calculator.dto.PurchaseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class PurchaseService {

    private static final BigDecimal[] TAX_RATES = {BigDecimal.TEN, BigDecimal.valueOf(13), BigDecimal.valueOf(20)};
    private static final BigDecimal BIG_DECIMAL_ONE_HUNDRED = BigDecimal.valueOf(100);

    public PurchaseDTO calculatePurchaseAmounts(PurchaseDTO purchaseDTO) {
        validatePurchaseDTO(purchaseDTO);
        if (purchaseDTO.getGrossAmount() != null && purchaseDTO.getGrossAmount().compareTo(BigDecimal.ZERO) > 0) {
            return calculateValuesByGrossAmount(purchaseDTO);
        } else if (purchaseDTO.getNetAmount() != null && purchaseDTO.getNetAmount().compareTo(BigDecimal.ZERO) > 0) {
            return calculateValuesByNetAmount(purchaseDTO);
        } else {
            return calculateValuesByVatAmount(purchaseDTO);
        }
    }

    private void validatePurchaseDTO(PurchaseDTO purchaseDTO) {
        if (purchaseDTO.getVatRate() == null || Arrays.stream(TAX_RATES).noneMatch(num -> num.compareTo(purchaseDTO.getVatRate()) == 0)) {
            throw new IllegalArgumentException("Invalid vat rate");
        }
        validatePurchaseAmounts(purchaseDTO);
    }

    private PurchaseDTO calculateValuesByGrossAmount(PurchaseDTO purchaseDTO) {
        BigDecimal netValue = purchaseDTO.getGrossAmount().divide((purchaseDTO.getVatRate().divide(BIG_DECIMAL_ONE_HUNDRED).add(BigDecimal.ONE)), 2, RoundingMode.HALF_EVEN);
        purchaseDTO.setNetAmount(netValue);
        purchaseDTO.setVatAmount((purchaseDTO.getGrossAmount().subtract(purchaseDTO.getNetAmount())).setScale(2, RoundingMode.HALF_EVEN));
        return purchaseDTO;
    }

    private PurchaseDTO calculateValuesByNetAmount(PurchaseDTO purchaseDTO) {
        BigDecimal grossValue = purchaseDTO.getNetAmount().multiply(((purchaseDTO.getVatRate().divide(BIG_DECIMAL_ONE_HUNDRED).add(BigDecimal.ONE)))).setScale(2, RoundingMode.HALF_EVEN);
        purchaseDTO.setGrossAmount(grossValue);
        purchaseDTO.setVatAmount((grossValue.subtract(purchaseDTO.getNetAmount())).setScale(2, RoundingMode.HALF_EVEN));
        return purchaseDTO;
    }

    private PurchaseDTO calculateValuesByVatAmount(PurchaseDTO purchaseDTO) {
        BigDecimal netValue = (purchaseDTO.getVatAmount().divide(purchaseDTO.getVatRate(), 4, RoundingMode.HALF_EVEN).multiply(BIG_DECIMAL_ONE_HUNDRED)).setScale(2, RoundingMode.HALF_EVEN);
        purchaseDTO.setNetAmount(netValue);
        purchaseDTO.setGrossAmount((netValue.add(purchaseDTO.getVatAmount())).setScale(2, RoundingMode.HALF_EVEN));
        return purchaseDTO;
    }

    private boolean isNullOrZero(BigDecimal number) {
        return (number == null) || (number.compareTo(BigDecimal.ZERO) <= 0);
    }

    private void validatePurchaseAmounts(PurchaseDTO purchaseDTO) {
        List<BigDecimal> valuesInformed = new ArrayList<>();
        if (!isNullOrZero(purchaseDTO.getGrossAmount())) {
            valuesInformed.add(purchaseDTO.getGrossAmount());
        }
        if (!isNullOrZero(purchaseDTO.getNetAmount())) {
            valuesInformed.add(purchaseDTO.getNetAmount());
        }
        if (!isNullOrZero(purchaseDTO.getVatAmount())) {
            valuesInformed.add(purchaseDTO.getVatAmount());
        }
        if (valuesInformed.isEmpty()) {
            throw new IllegalArgumentException("At least one purchase amount should be provided");
        } else if (valuesInformed.size() > 1) {
            throw new IllegalArgumentException("More than one purchase amount provided");
        }
    }
}
