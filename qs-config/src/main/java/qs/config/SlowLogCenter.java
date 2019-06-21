package qs.config;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import qs.util.DateHelper;
import qs.util.JsonHelper;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SlowLogCenter {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public static Disruptor slowLogCenterDisruptor;

    public static void publishEvent(LogType logType, String command, Long duration) {
        RingBuffer<LogEvent> ringBuffer = slowLogCenterDisruptor.getRingBuffer();

        ringBuffer.publishEvent((event, sequence) -> {
            event.setLogType(logType);
            event.setCommand(command);
            event.setDuration(duration);
            event.setTimestamp(Calendar.getInstance().getTime().getTime());
        });
    }


    @PostConstruct
    public void init() {

        int bufferSize = 1024;

        Disruptor<LogEvent> disruptor = new Disruptor(LogEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            long maxLen = 20;
            String key = (SlowLogCenter.class.getSimpleName()+":" + event.logType + ":" + DateHelper.serialize(new Date(), "MMdd")).toLowerCase();
            stringRedisTemplate.opsForZSet().add(key, JsonHelper.serialize(event), event.getDuration());
            long len = stringRedisTemplate.opsForZSet().size(key);
            if (len > maxLen) {
                stringRedisTemplate.opsForZSet().removeRange(key, 0, -maxLen - 1);
            }
            if (stringRedisTemplate.getExpire(key) == -1) {
                stringRedisTemplate.expire(key, 24, TimeUnit.HOURS);
            }
        });

        disruptor.start();
        SlowLogCenter.slowLogCenterDisruptor = disruptor;
    }


    public static class LogEvent {
        private LogType logType;
        private String command;
        private Long timestamp;
        private Long duration;

        public Long getDuration() {
            return duration;
        }

        public void setDuration(Long duration) {
            this.duration = duration;
        }

        public LogType getLogType() {
            return logType;
        }

        public void setLogType(LogType logType) {
            this.logType = logType;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }


    public enum LogType {
        REDIS, MSSQL, MYSQL, HTTP
    }


}