package pl.com.company.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.company.scheduler.SchedulerConfig;

import java.text.ParseException;


@Service
public class SalaryJobService {

    @Autowired
    private Scheduler scheduler;


    public void schedule(final Class jobClass) throws SchedulerException, ParseException {
        JobDetail jobDetail = SchedulerConfig.buildJobDetail(jobClass);
        Trigger trigger = SchedulerConfig.buildTrigger(jobClass);

        scheduler.scheduleJob(jobDetail, trigger);

    }
}
