package com.bank.service;


import com.bank.application.Application;
import com.bank.exception.BusinessException;
import com.bank.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApplicationProcessor {

    public static final Logger log = LoggerFactory.getLogger(ApplicationProcessor.class);

    private final ValidationService validationService;
    public ApplicationProcessor() {
        this.validationService =  new ValidationService();
    }

    public static final ApplicationProcessor INSTANCE = new ApplicationProcessor();

    public static ApplicationProcessor getInstance() {
    return INSTANCE; }

    public void process(Application application, Product product) throws BusinessException {
        try {
            validationService.validate(application, product);

            log.info("Заявка {} успешно сохранена, валидация пройдена",application.getId());
        } catch (BusinessException e) {
            log.error("Ошибка обработки заявки {}: {}", application.getId(), e.getMessage());
            throw e;
        }
    }
}