package com.bank.product;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductFactory {

    private static final Logger log =  LoggerFactory.getLogger(ProductFactory.class);

    public static Product createProduct(String type, String id, String name, String description,
                                        boolean isActive, double minSum, double maxSum, String currency, double interestRate,
                                        LoanType loanType, String repaymentSchedule, boolean hasCollateral,
                                        String earlyClosureTerms, boolean isRefillable) {
        if (type==null) {
            throw new IllegalArgumentException("Укажите тип продукта");
        }

        return switch (type.toUpperCase()) {
            case "CREDIT" -> createCreditProduct(id, name, description, isActive, minSum, maxSum, currency, interestRate,
                    loanType, repaymentSchedule, hasCollateral);
            case "DEPOSIT" -> createDepositProduct(id, name, description, isActive, minSum, maxSum, currency, interestRate,
                    earlyClosureTerms, isRefillable);
            default -> throw new IllegalArgumentException("Неизвестный тип продукта " + type);
        };
    }

    public static CreditProduct createCreditProduct(String id, String name, String description, boolean isActive,
                                                    double minSum, double maxSum, String currency, double interestRate, LoanType loanType,
                                                    String repaymentSchedule, boolean hasCollateral) {
        log.info("Создан кредитный продукт: id={}, name={}", id, name);
        return new CreditProduct(id, name, description, isActive, minSum, maxSum, currency, interestRate,
                loanType, repaymentSchedule, hasCollateral);
    }

    public static DepositProduct createDepositProduct(String id, String name, String description, boolean isActive,
                                                      double minSum, double maxSum, String currency, double interestRate, String earlyClosureTerms, boolean isRefillable) {
        log.info("Создан депозитный продукт: id={}, name={}", id, name);
        return new DepositProduct(id, name, description, isActive, minSum, maxSum, currency, interestRate,
                earlyClosureTerms, isRefillable);

    }
}
