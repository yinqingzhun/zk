package qs.config.db;

import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

//@Configuration
public class DataSourceTransactionManagerAutoConfigurer extends DataSourceTransactionManagerAutoConfiguration {
    /** * 自定义事务 * MyBatis自动参与到spring事务管理中，无需额外配置，只要org.mybatis.spring.SqlSessionFactoryBean引用的数据源与DataSourceTransactionManager引用的数据源一致即可，否则事务管理会不起作用。 * @return */
    @Bean
    @Primary
    public DataSourceTransactionManager multiTransactionManager(ApplicationContext ctx) {
        return new DataSourceTransactionManager((AbstractRoutingDataSource)ctx.getBean("roundRobinDataSourceProxy"));
    }
}