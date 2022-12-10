package qs.model.vo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface QuartzDelayRepeatTrigger {
    
    /*
    首次延迟执行时间，单位秒。-1 代表使用不超过运行间隔的随机延迟时间
     */
    int initialDelay() default 0;

    /**
     * 上一次调用结束和下一次调用开始之间的间隔时长，单位秒
     * @return
     */
    int fixedDelay();
    
    

}
