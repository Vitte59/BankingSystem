package com.bank.service;

import com.bank.application.Application;
import com.bank.exception.BusinessException;
import com.bank.exception.InvalidAmountException;
import com.bank.exception.ProductInactiveException;
import com.bank.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationService {

    private static final Logger log = LoggerFactory.getLogger(ValidationService.class.getName());
    public void validate(Application application, Product product) throws BusinessException {

        if (!product.isActive()) {
            log.error("Ошибка валидации: продукт {} неактивен", product.getId());
            throw new ProductInactiveException("Продукт должен быть активен");
        }

        if (application.getAmount() < product.getMinSum() || application.getAmount() > product.getMaxSum()) {
            log.error("Ошибка валидации: amount вне допустимых пределов", product.getId());
            throw new InvalidAmountException("amount должен быть между " + product.getMinSum() + " и " +  product.getMaxSum());
        }


    }
}
