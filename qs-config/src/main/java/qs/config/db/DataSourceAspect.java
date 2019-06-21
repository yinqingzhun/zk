package qs.config.db;

import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerStatement;
import com.google.common.base.Stopwatch;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.StatementImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.TypeUtils;
import qs.config.SlowLogCenter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@Aspect
@DeclarePrecedence("DataSourceAspect,*")
public class DataSourceAspect {
    Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);


    private DbChoosing getDbChoosing(JoinPoint point) {
        DbChoosing dbChoosing = point.getTarget().getClass().getAnnotation(DbChoosing.class);
        if (dbChoosing == null) {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            try {

                method = Optional.ofNullable(point.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes())).orElse(method);

            } catch (Exception e) {
                logger.debug(e.getMessage());
            }

            if (method != null) {
                dbChoosing = method.getAnnotation(DbChoosing.class);
            }
        }
        return dbChoosing;
    }

    @Around("execution(public * com.mysql.jdbc.PreparedStatement.execute*(..))" +
            "||execution(public * com.mysql.jdbc.StatementImpl.execute*(..))")
    public Object statementMysqlExecute(ProceedingJoinPoint joinpoint) throws Throwable {
        Object target = joinpoint.getTarget();
        String sql = "";
        try {
            if (target instanceof PreparedStatement || target instanceof PreparedStatement) {
                PreparedStatement preparedStatement = (PreparedStatement) target;
                sql = preparedStatement.asSql();
            } else if (target instanceof StatementImpl) {
                if (joinpoint.getArgs() != null && joinpoint.getArgs().length > 0)
                    sql = joinpoint.getArgs()[0].toString();
            }
        } catch (SQLException e) {
            log.error("failed to get the sql of the statementExecute", e);
        }
        sql = sql.replaceAll("\\s{2,}", " ");

        Stopwatch stopwatch = Stopwatch.createStarted();
        Object object = joinpoint.proceed(joinpoint.getArgs());

        stopwatch.stop();
        long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        log.info("****StatementExecute(MYSQL) costs {} ms \r\n{}\r\n{}", duration, sql, joinpoint.getSignature());
        if (!sql.startsWith("/*"))
            SlowLogCenter.publishEvent(SlowLogCenter.LogType.MYSQL, sql, duration);
        return object;
    }

    @Around("execution(public * com.microsoft.sqlserver.jdbc.SQLServerStatement.execute*(..))" +
            "||execution(public * com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement.execute*(..))")
    public Object statementMsSqlExecute(ProceedingJoinPoint joinpoint) throws Throwable {
        Object target = joinpoint.getTarget();
        String sql = "";
        //首次检查method是否携带sql
        if (joinpoint.getArgs() != null && joinpoint.getArgs().length > 0 && joinpoint.getArgs()[0] instanceof String) {
            sql = joinpoint.getArgs()[0].toString();
        }

        //inOutParam[0].inputDTV.getSetterValue()
        //检查获取SQLServerPreparedStatement中的userSql
        if (StringUtils.isEmpty(sql)) {
            if (target instanceof SQLServerPreparedStatement) {
                Field field = ReflectionUtils.findField(target.getClass(), "userSQL");
                ReflectionUtils.makeAccessible(field);
                if (null != field) {
                    Object fieldValue = ReflectionUtils.getField(field, target);
                    if (null != fieldValue) {
                        sql = fieldValue.toString();
                    }
                }
            }
        }

        //检查获取SQLServerStatement中的batchStatementBuffer (batch sql)
        if (StringUtils.isEmpty(sql)) {
            if (target instanceof SQLServerStatement) {
                Field field = ReflectionUtils.findField(target.getClass(), "batchStatementBuffer");
                ReflectionUtils.makeAccessible(field);
                if (null != field && field.getType() == ArrayList.class) {
                    Object fieldValue = ReflectionUtils.getField(field, target);
                    if (null != fieldValue) {
                        sql = ((ArrayList<Object>) fieldValue).stream().filter(p -> p != null).map(p -> p.toString()).collect(Collectors.joining(";"));
                    }
                }
            }
        }


        sql = sql.replaceAll("\\s{2,}", " ");

        Stopwatch stopwatch = Stopwatch.createStarted();
        Object object = joinpoint.proceed(joinpoint.getArgs());

        stopwatch.stop();
        long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        log.info("****StatementExecute(MS_SQLSERVER) costs {} ms \r\n{}\r\n{}", duration, sql, joinpoint.getSignature());
         
        SlowLogCenter.publishEvent(SlowLogCenter.LogType.MSSQL, sql, duration);
        return object;
    }

    @Around("execution(* qs.service..*(..))")
    public Object printAnnotationJointPointAround(ProceedingJoinPoint joinpoint) throws Throwable {
        DbChoosing dbChoosing = getDbChoosing(joinpoint);
        if (dbChoosing == null) {
            return joinpoint.proceed(joinpoint.getArgs());
        } else {
            DataSourceContextHolder.push(dbChoosing.value());
            Object object = joinpoint.proceed(joinpoint.getArgs());
            log.info("dbChoosing: {}, db: {} ", joinpoint.getSignature(), dbChoosing.value());
            DataSourceContextHolder.pop();
            return object;
        }
    }


}
