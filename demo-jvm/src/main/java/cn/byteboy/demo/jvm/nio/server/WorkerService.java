package cn.byteboy.demo.jvm.nio.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hongshaochuan
 * @date 2021/8/7
 */
public class WorkerService {

    private final ExecutorService workers;

    private final int numWorkerThreads;

    private final String threadNamePrefix;

    private volatile boolean stopped = true;


    public WorkerService(String name, int numWorkerThreads) {
        this.threadNamePrefix = name;
        this.numWorkerThreads = numWorkerThreads;
        this.workers = Executors.newFixedThreadPool(numWorkerThreads, new DaemonThreadFactory(threadNamePrefix));
        stopped = false;
    }

    public void schedule(WorkRequest workRequest) {
        if (stopped) {
            workRequest.cleanup();
            return;
        }
        ScheduledWorkRequest scheduledWorkRequest = new ScheduledWorkRequest(workRequest);

        try {
            workers.execute(scheduledWorkRequest);
        } catch (RejectedExecutionException e) {
            workRequest.cleanup();
            e.printStackTrace();
        }


    }

    public abstract static class WorkRequest {

        public abstract void doWork() throws Exception;

        /**
         * is will be called if the service is stopped or unable to schedule the request
         */
        public void cleanup() {

        }
    }

    private class ScheduledWorkRequest implements Runnable {

        private final WorkRequest workRequest;

        public ScheduledWorkRequest(WorkRequest workRequest) {
            this.workRequest = workRequest;
        }

        @Override
        public void run() {
            if (stopped) {
                workRequest.cleanup();
                return;
            }
            try {
                workRequest.doWork();
            } catch (Exception e) {
                workRequest.cleanup();
                e.printStackTrace();
            }

        }
    }

    private static class DaemonThreadFactory implements ThreadFactory {

        private final String namePrefix;

        private final ThreadGroup group;

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public DaemonThreadFactory(String name) {
            this.namePrefix = name + "-";
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
            t.setDaemon(true);
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
