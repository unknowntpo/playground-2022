package org.example.concurrency;

import org.example.rate_limit.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class Csp {
    private final int numJobs;
    private final int numWorkers = 8;
    private BlockingQueue<Job> jobQueue;
    private BlockingQueue<Result> resultQueue;
    private Logger logger = LoggerFactory.getLogger(Csp.class);
    private CountDownLatch done;
    private ExecutorService executor;

    public Csp(int numJobs) {
        this.numJobs = numJobs;
        this.jobQueue = new LinkedBlockingQueue<>();
        this.resultQueue = new LinkedBlockingQueue<>();
        this.done = new CountDownLatch(1);
    }

    public void start() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
        // spawn workers
        for (int i = 0; i < numWorkers; i++) {
            int workerId = i;
            executor.submit(() -> {
                while (true) {
                    if (isDone()) {
                        logger.info("worker {} done its job.", workerId);
                        return;
                    }

                        try {
                            var job = jobQueue.take();
                            logger.info("worker {}: executing job {}", workerId, job);
                            
                            // Sleep for 100ms as requested
                            Thread.sleep(100);
                            
                            resultQueue.put(new Result(job.id, String.format("job %d completed", job.id)));

                            logger.info("len of resultQueue: {}", resultQueue.size());

                            logger.info("worker {}: job {} completed", workerId, job.id);
                        } catch (InterruptedException e) {
                            logger.info("worker {} interrupted, exiting.", workerId);
                            return;
                        }
                    }
                });
            }

            // spawn jobs
            executor.submit(() -> {
                IntStream.range(0, numJobs).forEach((id) -> {
                    try {
                        jobQueue.put(new Job(id));
                        logger.info("put job with id: {} into queue", id);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        }

        private boolean isDone() {
            return done.getCount() == 0;
        }

        public record Job(int id) {
        }

        public record Result(int id, String message) {
        }

        public List<Result> getResults() {
            // Consumer (fan-in)
            List<Result> results = new ArrayList<>(numJobs);
            try {
                for (int i = 0; i < numJobs; i++) {
                    Result r = resultQueue.take();
                    logger.info("getResults: {} → {}", r.id(), r.message());
                    results.add(r);
                }

                done.countDown();

                // shutdownNow() sends interrupts to workers blocked on take()
                this.executor.shutdownNow();

                return results;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
