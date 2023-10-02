package org.example.service;

import org.example.executor.JobExecutor;
import org.example.model.Job;
import org.example.model.JobType;

import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SchedulerService implements ISchedulerService{
    private static final SchedulerService INSTANCE = new SchedulerService();
    private final PriorityQueue<Job> jobPriorityQueue;
    private final Lock queueLock;
    private final Condition entryAdded;
    private int threadCount;

    public SchedulerService() {
        this.threadCount = 6;
        this.jobPriorityQueue = new PriorityQueue<>();
        this.queueLock = new ReentrantLock();
        this.entryAdded = queueLock.newCondition();

        Thread executor = new Thread(new JobExecutor(jobPriorityQueue, queueLock, entryAdded, threadCount));
        executor.start();
    }

    public static SchedulerService getInstance() {
        return INSTANCE;
    }

    private void addToJobQueue(Job job) {
        queueLock.lock();
        try {
            jobPriorityQueue.offer(job);
            entryAdded.signal();
        } finally {
            queueLock.unlock();
        }
    }


    @Override
    public void schedule(Runnable task, long initialDelay, TimeUnit unit) {
        Date startTime = new Date(Calendar.getInstance().getTime().getTime() + unit.toMillis(initialDelay));
        Job job = new Job(UUID.randomUUID().hashCode(), task, startTime);

        addToJobQueue(job);
    }

    @Override
    public void scheduleAtFixedRate(Runnable task, long initialDelay, long recurringDelay, TimeUnit unit) {
        Date startTime = new Date(Calendar.getInstance().getTime().getTime() + unit.toMillis(initialDelay));
        Job job = new Job(UUID.randomUUID().hashCode(), task, startTime, recurringDelay, unit, JobType.REPEAT_FIXED_RATE);

        addToJobQueue(job);
    }
}
