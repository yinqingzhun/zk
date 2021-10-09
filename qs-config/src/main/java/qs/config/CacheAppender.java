package qs.config;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Plugin(name = CacheAppender.APPENDER_NAME, category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE, printObject = true)
public class CacheAppender extends AbstractAppender {
    public static final String APPENDER_NAME = "Cache";
    private static Map<String, LogCounter> EVENT_MAP = new ConcurrentHashMap<>();
    public CacheAppender() {
        this(APPENDER_NAME, null, null, false, null);
    }

    public CacheAppender(String name, Filter filter) {
        this(name, filter, null, false, null);
    }

    protected CacheAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginFactory
    public static CacheAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter) {
        return new CacheAppender(name, filter);
    }

    @Override
    public void append(LogEvent event) {
        String msg=getLogMessage(event);
        String name=event.getLoggerName();

        EVENT_MAP.compute(name, (k, v) -> {
            if(v==null)
                return new LogCounter(name,msg);
            v.counter.incrementAndGet();
            return v;
        });
    }


    private String getLogMessage(LogEvent event){
        StringBuilder throwableTrace = new StringBuilder();
        if (event.getThrown() != null) {
            throwableTrace.append(event.getThrown().getClass().getName())
                    .append(":").append(event.getThrown().getMessage())
                    .append("\n").append(getStackTrace(event.getThrown(), 10));
        }
        return throwableTrace.toString();
    }

    private String getStackTrace(Throwable throwable, int maxDepth) {
        if (throwable == null || throwable.getStackTrace() == null)
            return null;
        StringBuilder stringBuilder = new StringBuilder();
        StackTraceElement[] elements = throwable.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            if (i >= maxDepth)
                break;
            stringBuilder.append(elements[i]).append("\n");
        }
        return stringBuilder.toString();
    }

    public static Map<String, LogCounter> empty(){
        Map<String, LogCounter> map=EVENT_MAP;
        EVENT_MAP=new ConcurrentHashMap<>();
        return map;
    }

    public static Map<String, LogCounter> getEventMap(){
        return Collections.unmodifiableMap(EVENT_MAP) ;
    }

    public static class LogCounter {
        public LogCounter(String name, String stackTrace){
            this.name=name;
            this.stackTrace=stackTrace;
            this.counter=new AtomicInteger();
        }

        public String name;
        public String stackTrace;
        public AtomicInteger counter;
    }
}
