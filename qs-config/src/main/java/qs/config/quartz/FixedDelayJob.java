package qs.config.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import qs.model.vo.Task;

import java.util.concurrent.TimeUnit;

/**
 * 执行固定延迟间隔的Job,使用FixedDelayJob需要通过JobDataMap传递key为FixedDelayJob.FIXED_DELAY_JOB_TASK_INSTANCE的{@link Task}对象，以及key为FixedDelayJob.FIXED_DELAY_JOB_DELAY的延迟间隔时长（单位：秒）。
 * FixedDelayJob需要与{@link FixedDelayJobListener}配合使用
 *
 * @see FixedDelayJobListener
 */
public class FixedDelayJob implements Job {

    /**
     * Job内部真正执行的{@link Task}实例
     */
    public static final String FIXED_DELAY_JOB_TASK_INSTANCE = "FixedDelayJob_TaskInstance";
    /**
     * 固定延迟时间间隔，单位：秒
     */
    public static final String FIXED_DELAY_JOB_DELAY = "FixedDelayJob_DelayInSecond";


    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getJobDetail().getJobDataMap();
        Object delay = map.get(FIXED_DELAY_JOB_DELAY);
        if (delay == null)
            throw new JobExecutionException("FixedDelayJob_DelayInSecond can't be found in JobDataMap");

        map.put(FixedDelayJobListener.FIXED_DELAY_JOB_DATA, new FixdedDelayJobData(Long.parseLong(delay.toString()), TimeUnit.SECONDS));
        try {
            Object taskObj = map.get(FIXED_DELAY_JOB_TASK_INSTANCE);
            if (taskObj != null || taskObj instanceof Task) {
                ((Task) taskObj).execute();
            } else {
                throw new JobExecutionException("FixedDelayJob_TaskInstance can't be found in JobDataMap");
            }

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
