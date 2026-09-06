package com.bank.service;

import com.bank.application.Application;
import com.bank.exception.BusinessException;
import com.bank.exception.InvalidAmountException;
import com.bank.exception.ProductInactiveException;
import com.bank.product.Product;

public class ValidationService {
    public void validate(Application application, Product product) throws BusinessException {

        if (!product.isActive()) {
            throw new ProductInactiveException("Продукт должен быть активен");
        }

        if (application.getAmount() < product.getMinSum() || application.getAmount() > product.getMaxSum()) {
            throw new InvalidAmountException("amount должен быть между " + product.getMinSum() + " и " +  product.getMaxSum());
        }


    }
}
