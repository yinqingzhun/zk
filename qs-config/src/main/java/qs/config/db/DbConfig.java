package qs.config.db;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Qualifier("dbConfig")
public class DbConfig {
    //@Value("${spring.datasource.type}")
    //private Class<? extends DataSource> dataSourceType;


    @Bean
    @ConfigurationProperties(prefix = "app.datasource.mdb")
    Properties dataSourcePropertiesForMdb() {
        Properties dataSourceProperties = new Properties();
        return dataSourceProperties;
    }

    @Primary
    @Bean(name = "mydb")
    public DataSource mydb() throws Exception {
        return buildDataSource(dataSourcePropertiesForMdb());
    }

    @Bean
    @ConfigurationProperties(prefix = "app.datasource.winds")
    Properties dataSourcePropertiesForWinds() {
        Properties dataSourceProperties = new Properties();
        return dataSourceProperties;
    }

    @Bean(name = "winds")
    public DataSource winds() throws Exception {
        return buildDataSource(dataSourcePropertiesForWinds());
    }


    private DataSource buildDataSource(Properties srcProperties) throws Exception {
        if (ObjectUtils.nullSafeEquals(srcProperties.get("type"), "com.alibaba.druid.pool.DruidDataSource")) {
            Properties druidProperties = new Properties();

            srcProperties.forEach((key, value) -> {
                druidProperties.put("druid." + key, String.valueOf(value));
            });

            DataSource druidDataSource = DruidDataSourceFactory.createDataSource(druidProperties);
            return druidDataSource;
        } else {
            DataSourceProperties dataSourceProperties = new DataSourceProperties();
            DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(dataSourceProperties);
            srcProperties.forEach((key, value) -> {
                if (directFieldAccessor.getPropertyType(key.toString()) != null)
                    directFieldAccessor.setPropertyValue(key.toString(), value);
            });

            DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().build();
            return dataSource;
        }
    }


}
