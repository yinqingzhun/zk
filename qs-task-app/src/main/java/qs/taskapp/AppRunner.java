package qs.taskapp;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Component;
import qs.config.quartz.FixedDelayJob;
import qs.config.quartz.FixedDelayJobListener;
import qs.model.vo.QuartzCronTrigger;
import qs.model.vo.QuartzDelayRepeatTrigger;
import qs.model.vo.QuartzSimpleRepeatTrigger;
import qs.model.vo.Task;

import java.util.*;
import java.util.Calendar;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AppRunner implements CommandLineRunner {

    @Autowired(required = false)
    Map<String, Task> jobMap;

    /**
     * 运行任务
     *
     * @param args 启动的任务名称或包路径
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (jobMap == null || jobMap.size() == 0 || args.length == 0) {
            log.warn("no job is defined");
            return;
        }

        final List<String> taskArgs = Arrays.asList(args != null && args.length > 0 ? Arrays.copyOfRange(args, 0, args.length) : new String[0]);
        if (taskArgs.size() == 0) {
            log.warn("**specify one job or more");
            return;
        }


        List<Map.Entry<String, Task>> jobList = jobMap.entrySet().stream().filter(p ->
                taskArgs.stream().anyMatch(task -> p.getKey().equalsIgnoreCase(task) || p.getValue().getClass().getName().startsWith(task + ".")))
                .collect(Collectors.toList());

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        for (int i = 0; i < jobList.size(); i++) {
            Map.Entry<String, Task> entry = jobList.get(i);
            Task task = entry.getValue();
            MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
            methodInvokingJobDetailFactoryBean.setTargetMethod("execute");
            methodInvokingJobDetailFactoryBean.setTargetObject(task);
            methodInvokingJobDetailFactoryBean.setGroup("jobGroup");
            methodInvokingJobDetailFactoryBean.setName("job" + i);
            methodInvokingJobDetailFactoryBean.setConcurrent(false);
            methodInvokingJobDetailFactoryBean.afterPropertiesSet();

            JobDetail jobDetail = methodInvokingJobDetailFactoryBean.getObject();

            ScheduleBuilder scheduleBuilder = null;
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            QuartzSimpleRepeatTrigger quartzSimpleTrigger = task.getClass().getAnnotation(QuartzSimpleRepeatTrigger.class);
            if (quartzSimpleTrigger != null) {
                int repeatInterval = quartzSimpleTrigger.repeatInterval();
                if (repeatInterval < 0)
                    throw new Exception(String.format("repeatInterval of annotation {} for task {} is required unsigned value", QuartzSimpleRepeatTrigger.class.getSimpleName(), task.getClass().getSimpleName()));

                int initalDelay = quartzSimpleTrigger.initialDelay();
                if (initalDelay < -1)
                    initalDelay = 0;
                if (initalDelay == -1) {
                    initalDelay = new Random().nextInt(repeatInterval);
                }

                calendar.add(Calendar.SECOND, initalDelay);

                scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(quartzSimpleTrigger.repeatInterval()).withRepeatCount(quartzSimpleTrigger.repeatCount());
            }

            QuartzDelayRepeatTrigger quartzDelayRepeatTrigger = task.getClass().getAnnotation(QuartzDelayRepeatTrigger.class);
            if (quartzDelayRepeatTrigger != null) {
                int fixedDelay = quartzDelayRepeatTrigger.fixedDelay();
                if (fixedDelay < 0)
                    throw new Exception(String.format("repeatInterval of annotation {} for task {} is required unsigned value", QuartzDelayRepeatTrigger.class.getSimpleName(), task.getClass().getSimpleName()));


                scheduler.getListenerManager().addJobListener(new FixedDelayJobListener());

                jobDetail = JobBuilder.newJob(FixedDelayJob.class)
                        .usingJobData(new JobDataMap() {{
                            put(FixedDelayJob.FIXED_DELAY_JOB_TASK_INSTANCE, task);
                            put(FixedDelayJob.FIXED_DELAY_JOB_DELAY, fixedDelay);
                        }}).build();

                scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();


                int initalDelay = quartzDelayRepeatTrigger.initialDelay();
                if (initalDelay < -1)
                    initalDelay = 0;
                if (initalDelay == -1) {
                    initalDelay = new Random().nextInt(fixedDelay);
                }

                calendar.add(Calendar.SECOND, initalDelay);
            }


            QuartzCronTrigger quartzCronTrigger = task.getClass().getAnnotation(QuartzCronTrigger.class);
            if (quartzCronTrigger != null) {
                scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzCronTrigger.cron());
            }


            if (scheduleBuilder == null) {
                log.warn("找不到Task:{}的日程安排注解", task.getClass().getName());
                continue;
            }

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + i, "triggerGroup").startAt(calendar.getTime())
                    .withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("开始执行任务({}秒后)：{}", (calendar.getTime().getTime() - System.currentTimeMillis()) / 1000, task.getClass().getName());
        }
        if (scheduler.getJobGroupNames().size() > 0) {
            scheduler.start();
        }
    }
}
