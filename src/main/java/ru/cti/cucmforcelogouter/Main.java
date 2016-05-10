package ru.cti.cucmforcelogouter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.cti.cucmforcelogouter.controller.App;

/**
 * Main file
 */
public class Main {
    private static final Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ru.cti.cucmforcelogouter.Configuration.class);
        App app = (App) context.getBean("app");
        app.doWork();
    }
}
