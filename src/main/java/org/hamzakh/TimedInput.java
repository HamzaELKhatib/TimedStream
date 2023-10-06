package org.hamzakh;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TimedInput {
    private final List<String> names = new ArrayList<>();

    private static final Integer DURATION_IN_SECONDS = 5;


    public void start() {
        long startTime;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        startTime = System.currentTimeMillis();
        System.out.println("Started at: " + startTime + " ms");

        Future<?> future = executor.submit(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Enter a name: ");
                    String name = reader.readLine();
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    names.add(name + " -> " + elapsedTime + " ms");
                    System.out.println("Name entered: " + name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.interruptAfter(future, executor, startTime);
    }

    private void interruptAfter(Future<?> future, ExecutorService executor, long startTime) {

        try {
            future.get(DURATION_IN_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("Terminated at: " + endTime + " ms");
            System.out.println("Names entered: " + names);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();
    }
}