package com.bank.product;

public class DepositProduct extends Product{


    private String earlyClosureTerms;
    private boolean isRefillable;

    public DepositProduct(String id, String name, String description, boolean isActive,
                          double maxSum, double minSum, String currency,  String earlyClosureTerms, boolean isRefillable) {
        super(id, name, description, isActive, maxSum, minSum, currency);
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
