package org.example.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.object.OriginObject;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;

@Configuration
public class RdbConfig extends OriginObject {

    @Value("${spring.datasource.hikari.pool-name}")
    public String poolName;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    public Integer maximumPoolSize;

    @Value("${spring.datasource.hikari.connection-timeout}")
    public Integer connectionTimeout;

    @Value("${spring.datasource.hikari.idle-timeout}")
    public Integer idleTimeout;

    @Value("${spring.datasource.hikari.max-lifetime}")
    public Integer maxLifetime;

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public HikariDataSource masterDataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        if(bePresent(this.poolName)) hikariDataSource.setPoolName(this.poolName);
        if(bePresent(this.maximumPoolSize)) hikariDataSource.setMaximumPoolSize(this.maximumPoolSize);
        if(bePresent(this.connectionTimeout)) hikariDataSource.setConnectionTimeout(this.connectionTimeout);
        if(bePresent(this.idleTimeout)) hikariDataSource.setIdleTimeout(this.idleTimeout);
        if(bePresent(this.maxLifetime)) hikariDataSource.setMaxLifetime(this.maxLifetime);
        return hikariDataSource;
    }

    @Bean
    public SqlSessionFactory masterSqlSessionFactory(DataSource masterDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(masterDataSource);
        sqlSessionFactoryBean
            .setConfigLocation(applicationContext.getResource("classpath:mapper/mybatis-context.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate masterSqlSessionTemplate(SqlSessionFactory masterSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(masterSqlSessionFactory);
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        HikariDataSource dataSource = masterDataSource();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
