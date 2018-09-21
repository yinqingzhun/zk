package qs.config.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import sun.rmi.runtime.Log;

import javax.sql.DataSource;
import java.util.Map;
import java.util.stream.Collectors;

@DependsOn(value = "dbConfig")
@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@MapperScan("qs.persist")
@Import(DbConfig.class)
@Slf4j
public class MybatisConfig implements TransactionManagementConfigurer, ApplicationContextAware {

    ApplicationContext applicationContext;
    @Autowired
    DbConfig dbConfig;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Bean
    public SqlSessionFactory multiSqlSessionFactory(ApplicationContext ctx) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(roundRobinDataSourceProxy(ctx));
        factory.setVfs(SpringBootVFS.class);
        factory.setTypeAliasesPackage("qs.model.po");



        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            factory.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
            return factory.getObject();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean
    public AbstractRoutingDataSource roundRobinDataSourceProxy(ApplicationContext ctx) {
        MybatisRoutingDataSource proxy = new MybatisRoutingDataSource();
        Map<Object, Object> targetDataSources = applicationContext.getBeansOfType(DataSource.class).entrySet().stream
                ().collect(Collectors.toMap(a -> a.getKey(), b -> b.getValue()));
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }


    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return null;
    }

    public class MybatisRoutingDataSource extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            return DataSourceContextHolder.get();
        }
    }


}
