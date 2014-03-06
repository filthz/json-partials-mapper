package de.infinity.config;

import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import de.infinity.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@EnableMongoRepositories(basePackageClasses = {FileRepository.class})
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${connection.mongodb.port}")
    private String mongoPort;

    @Value("${connection.mongodb.database}")
    private String mongoDatabasename;
    @Value("${connection.mongodb.hostname}")
    private String mongoHostname;
    private static final Logger LOG = LoggerFactory.getLogger(MongoConfig.class);

    public String getDatabaseName() {
        return mongoDatabasename;
    }

    @Override
    public Mongo mongo() throws Exception {
        int port = -1;
        try {
            port = Integer.parseInt(mongoPort.trim());
        } catch (Exception e) {
            LOG.error("Could not parse port '" + mongoPort + "' from application properties. Must be an integer", e);
        }

        return new Mongo(mongoHostname, port);
    }

    @Bean(destroyMethod = "destroy")
    public SimpleMongoDbFactory mongoDbFactory() throws Exception {

        int port = -1;
        try {
            port = Integer.parseInt(mongoPort.trim());
        } catch (Exception e) {
            LOG.error("Could not parse port '" + mongoPort + "' from application properties. Must be an integer", e);
        }

        LOG.info("INIT Mongo Database using URI: mongodb://" + mongoHostname + ":" + mongoPort + "/" + mongoDatabasename);
        return new SimpleMongoDbFactory(new MongoURI("mongodb://" + mongoHostname + ":" + mongoPort + "/" + mongoDatabasename));
    }

    @Override
    protected String getMappingBasePackage() {
        return "de.tsystems.tools.repository";
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MappingMongoConverter converter = new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
        converter.setMapKeyDotReplacement("_");

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), converter);
        LOG.info("Setting WriteResultChecking to Exception");
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return mongoTemplate;
    }
}
