package com.bank.product;

public class ProductFactory {

    public static CreditProduct createCreditProduct(String id, String name, String description, boolean isActive,
                                                    double maxSum, double minSum, String currency, LoanType loanType,
                                                    String repaymentSchedule, boolean hasCollateral) {
        return new CreditProduct(id, name, description, isActive, maxSum, minSum, currency,
                loanType, repaymentSchedule, hasCollateral);
    }

    public static DepositProduct createDepositProduct(String id, String name, String description, boolean isActive,
                                                      double maxSum, double minSum, String currency, String earlyClosureTerms, boolean isRefillable) {
        return new DepositProduct(id, name, description, isActive, maxSum, minSum, currency,
                earlyClosureTerms, isRefillable);

    }
}
