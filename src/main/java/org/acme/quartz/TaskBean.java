package org.acme.quartz;

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.quarkus.runtime.StartupEvent;
import org.quartz.*;

@ApplicationScoped
public class TaskBean {

    @Inject
    org.quartz.Scheduler quartz;

    void onStart(@Observes StartupEvent event) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "myGroup")
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "myGroup")
                .startNow()
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(10)
                                .repeatForever())
                .build();
        quartz.scheduleJob(job, trigger);
    }

    @Transactional
    void performTask() {
        Task task = new Task();
        task.persist();
    }

    // A new instance of MyJob is created by Quartz for every job execution
    public static class MyJob implements Job {

        @Inject
        TaskBean taskBean;

        public void execute(JobExecutionContext context) throws JobExecutionException {
            taskBean.performTask();
        }

    }
}