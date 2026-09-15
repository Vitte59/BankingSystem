package com.bank.product;

public class DepositProduct extends Product{


    private String earlyClosureTerms;
    private boolean isRefillable;

    public DepositProduct(String id, String name, String description, boolean isActive,
                          double minSum, double maxSum, String currency, double interestRate, String earlyClosureTerms, boolean isRefillable) {
        super(id, name, description, isActive, minSum, maxSum, currency, interestRate);
        this.earlyClosureTerms = earlyClosureTerms;
        this.isRefillable = isRefillable;
    }

    public String getEarlyClosureTerms() {
        return earlyClosureTerms;
    }

    public boolean isRefillable() {
        return isRefillable;
    }





}
