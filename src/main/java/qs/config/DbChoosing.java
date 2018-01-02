package qs.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@Inherited
public @interface DbChoosing {
    /**
     * 数据库名称
     *
     * @return
     */
    EnumDataSourceName value();
}