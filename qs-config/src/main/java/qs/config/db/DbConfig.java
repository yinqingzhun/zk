package qs.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
@Configuration
@Qualifier("dbConfig")
public class DbConfig {
    //@Value("${spring.datasource.type}")
    //private Class<? extends DataSource> dataSourceType;


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSourceProperties dataSourcePropertiesForTicketUser() {

        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        return dataSourceProperties;
    }
    @Primary
    @Bean(name = "mydb")
    @Qualifier("mydb")
    public DataSource ticketUserDataSource() {
        DataSource dataSource = dataSourcePropertiesForTicketUser().initializeDataSourceBuilder().build();
        return dataSource;
    }


}
