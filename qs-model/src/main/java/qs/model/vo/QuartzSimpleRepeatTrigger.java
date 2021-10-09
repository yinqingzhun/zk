package qs.model.vo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface QuartzSimpleRepeatTrigger {
    /**
     *重复运行次数。默认-1无限次
     **/
    int repeatCount() default -1;

    /**
    *运行间隔，单位秒
     **/
    int repeatInterval();

    /**
     * 首次延迟执行时间，单位秒。-1 代表使用不超过运行间隔的随机延迟时间
     * @return
     */
    int initialDelay() default 0;
    
}
