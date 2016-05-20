package ru.cti.cucmforcelogouter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.cti.cucmforcelogouter.controller.App;

/**
 * Main file
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger("Mine");

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ru.cti.cucmforcelogouter.Configuration.class);
        App app = (App) context.getBean("app");
        app.doWork();
    }
}
