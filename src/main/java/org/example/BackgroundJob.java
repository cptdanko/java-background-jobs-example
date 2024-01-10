package org.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class BackgroundJob {
    private static final Logger LOG = Logger.getLogger(BackgroundJob.class.toString());
    private static final AtomicInteger job1Counter = new AtomicInteger(0);
    private static final AtomicInteger job2Counter = new AtomicInteger(0);
    static AtomicReference<Queue<String>> runningJobs = new AtomicReference<>(new LinkedList<>());
    public static void startJob() {
        CompletableFuture<String> longRunningJob = CompletableFuture.supplyAsync(() -> {
            String jobName = "JOB_ONE_INSTANCE_"+ job1Counter.incrementAndGet() + "_"+ System.currentTimeMillis();
            runningJobs.get().offer(jobName);
            LOG.info("Running job "+ jobName);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return jobName;
        });

        longRunningJob.thenApply(result -> {
            LOG.info("Finished "+ runningJobs.get().poll() +" " );

            LOG.info("---------------__________------------");
            String jobName = "JOB_TWO_INSTANCE_"+ job2Counter.incrementAndGet() + "_"+ System.currentTimeMillis();
            runningJobs.get().offer(jobName);
            LOG.info("Running job "+ jobName);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return jobName;
        }).thenRun(() -> {
            LOG.info("Finished "+ runningJobs.get().poll() +" ");
            LOG.info("---------------__________------------");
        });
        LOG.info("=============== CURRENT JOBS QUEUE ===============");
        runningJobs.get().forEach(s -> {
            LOG.info(s);
        });
        LOG.info("=============== END OF JOBS QUEUE ===============");
    }
}
