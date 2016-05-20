package ru.cti.cucmforcelogouter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.cti.cucmforcelogouter.controller.App;
import ru.cti.cucmforcelogouter.controller.logdirectory.AbstractLogDirectoryHandler;
import ru.cti.cucmforcelogouter.controller.logdirectory.LogDirectoryHandler;
import ru.cti.cucmforcelogouter.controller.logdirectory.loghandler.AbstractFileTailer;
import ru.cti.cucmforcelogouter.controller.logdirectory.loghandler.AbstractFileListener;
import ru.cti.cucmforcelogouter.controller.cucmapi.CucmApiImplementation;
import ru.cti.cucmforcelogouter.controller.logdirectory.loghandler.FileTailer;
import ru.cti.cucmforcelogouter.controller.logdirectory.loghandler.TailerFileListener;
import ru.cti.cucmforcelogouter.model.dbmaintenance.AbstractDBMaintenance;
import ru.cti.cucmforcelogouter.model.dbmaintenance.DBMaintenance;
import ru.cti.cucmforcelogouter.model.factory.PhoneFactory;
import ru.cti.cucmforcelogouter.model.factory.PhoneListFactory;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Spring Java based configuration file
 */
@org.springframework.context.annotation.Configuration
@ComponentScan("ru.cti.cucmforcelogouter.model.repository")
@PropertySources({@PropertySource(value = "file:${app.home}/etc/config.properties")})
@EnableTransactionManagement
@EnableJpaRepositories
public class Configuration {
    public static final String APPHOME = System.getProperty("app.home");

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.sqlite.JDBC.class);
        dataSource.setUrl("jdbc:sqlite:" + APPHOME + "/db/sqlite.db");
        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.SQLiteDialect");
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        // path to domain entities
        factory.setPackagesToScan("ru.cti.cucmforcelogouter.model.domainobjects");
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public App app() {
        return new App();
    }

    @Bean
    public AbstractLogDirectoryHandler abstractLogBufferDirectoryHandler() {
        return new LogDirectoryHandler(env.getProperty("logExtension"),
                env.getProperty("sourcePath"),
                env.getProperty("soughtString"));
    }

    @Bean
    @Scope("prototype")
    public AbstractFileListener abstractLogFileHandler() {
        return new TailerFileListener(env.getProperty("deviceNameRegexp"),
                env.getProperty("messageTimeRegexp"));
    }

    @Bean
    @Scope("prototype")
    public AbstractFileTailer fileTailer() {
        return new FileTailer(Integer.parseInt(env.getProperty("timeoutUntilInterrupt")));
    }

    @Bean
    public PhoneFactory phoneFactory() {
        return new PhoneFactory();
    }

    @Bean
    public PhoneListFactory phoneListFactory() {
        return new PhoneListFactory();
    }

    @Bean
    public AbstractDBMaintenance dbMaintenance() {
        AbstractDBMaintenance dbMaintenance = new DBMaintenance();
        dbMaintenance.setMaximumPhoneAge(Integer.parseInt(env.getProperty("maximumPhoneAge")));
        dbMaintenance.setMaximumPhoneListAge(Integer.parseInt(env.getProperty("maximumPhoneListAge")));
        return dbMaintenance;
    }

    @Bean
    public CucmApiImplementation cucmApiImplementation() {
        return new CucmApiImplementation(env.getProperty("serverURL"),
                env.getProperty("login"),
                env.getProperty("password"),
                env.getProperty("techUserId"),
                env.getProperty("techDeviceProfile"),
                Integer.parseInt(env.getProperty("techExclusiveDuration")));
    }
}
