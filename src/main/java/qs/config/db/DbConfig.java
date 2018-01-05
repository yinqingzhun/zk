package qs.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DbConfig {
    @Value("${app.datasource.mdb.type}")
    private Class<? extends DataSource> dataSourceType;


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "app.datasource.mdb")
    DataSourceProperties dataSourcePropertiesForTicketUser() {

        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        return dataSourceProperties;
    }

    @Bean(name = "mdb")
    @Qualifier("mdb")
    @ConfigurationProperties(prefix = "app.datasource.mdb")
    public DataSource ticketUserDataSource() {
        DataSource dataSource = dataSourcePropertiesForTicketUser().initializeDataSourceBuilder().build();
        return dataSource;
    }
}
