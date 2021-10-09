package qs.taskapp.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import qs.model.vo.QuartzSimpleRepeatTrigger;
import qs.model.vo.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@QuartzSimpleRepeatTrigger(repeatInterval = 1)
@Component
public class HelloTask implements Task {

    @Override
    public void execute() {
        log.info("**hello task at {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
