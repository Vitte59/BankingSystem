package com.bank.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductFactory {

    private static final Logger log =  LoggerFactory.getLogger(ProductFactory.class);

    public static CreditProduct createCreditProduct(String id, String name, String description, boolean isActive,
                                                    double maxSum, double minSum, String currency, LoanType loanType,
                                                    String repaymentSchedule, boolean hasCollateral) {
        log.info("Создан кредитный продукт: id={}, name={}", id, name);
        return new CreditProduct(id, name, description, isActive, maxSum, minSum, currency,
                loanType, repaymentSchedule, hasCollateral);
    }

    public static DepositProduct createDepositProduct(String id, String name, String description, boolean isActive,
                                                      double maxSum, double minSum, String currency, String earlyClosureTerms, boolean isRefillable) {
        log.info("Создан депозитный продукт: id={}, name={}", id, name);
        return new DepositProduct(id, name, description, isActive, maxSum, minSum, currency,
                earlyClosureTerms, isRefillable);

    }
}
