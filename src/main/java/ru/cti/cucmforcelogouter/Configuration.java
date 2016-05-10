package ru.cti.cucmforcelogouter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import ru.cti.cucmforcelogouter.controller.App;
import ru.cti.cucmforcelogouter.controller.dao.DAOFacade;
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

import javax.sql.DataSource;

/**
 * Spring Java based configuration file
 */
@org.springframework.context.annotation.Configuration
@PropertySources({@PropertySource(value = "file:${app.home}/etc/config.properties")})
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
    public DAOFacade daoFacade() {
        return new DAOFacade(dataSource());
    }

    @Bean
    public PhoneFactory phoneFactory() {
        return new PhoneFactory();
    }

    @Bean
    public AbstractDBMaintenance dbMaintenance() {
        AbstractDBMaintenance dbMaintenance = new DBMaintenance();
        dbMaintenance.setMaximumPhoneAge(Integer.parseInt(env.getProperty("maximumPhoneAge")));
        return dbMaintenance;
    }

    @Bean
    public CucmApiImplementation cucmApiImplementation() {
        return new CucmApiImplementation(env.getProperty("serverURL"),
                env.getProperty("login"),
                env.getProperty("password"));
    }
}
