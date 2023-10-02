package org.example.model;

import org.example.service.SchedulerService;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Job implements Runnable, Comparable<Job>{
    private int jobId;
    private Runnable task;

    private Date startTime;
    private long reschedulePeriod;

    private TimeUnit unit;
    private JobType type;

    public Job(int jobId, Runnable task, Date startTime) {
        this(jobId, task, startTime, -1, TimeUnit.SECONDS, JobType.ONCE);
    }

    public Job(int jobId, Runnable task, Date startTime, long reschedulePeriod, TimeUnit unit, JobType type) {
        this.jobId = jobId;
        this.task = task;
        this.startTime = startTime;
        this.reschedulePeriod = reschedulePeriod;
        this.unit = unit;
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void run() {
        if (JobType.REPEAT_FIXED_RATE.equals(this.type)) {
            SchedulerService.getInstance().scheduleAtFixedRate(this.task, reschedulePeriod, reschedulePeriod, unit);
        }

        task.run(); // Exception needs to handled neatly.

        // ToDo: Implement REPEAT_FIXED_DELAY
    }

    @Override
    public int compareTo(Job nextJob) {
        return this.startTime.compareTo(nextJob.getStartTime());
    }
}
