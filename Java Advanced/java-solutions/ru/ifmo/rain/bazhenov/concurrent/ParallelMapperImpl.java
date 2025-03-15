package ru.ifmo.rain.bazhenov.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

/**
 * Class implementing {@link ParallelMapper}.
 *
 * @author Egor Bazhenov
 */
@SuppressWarnings("unused")
public class ParallelMapperImpl implements ParallelMapper {
    private final Queue<Runnable> queueTasks;
    private final List<Thread> threadList;

    /**
     * Default constructor
     */
    public ParallelMapperImpl() {
        queueTasks = new ArrayDeque<>();
        threadList = new ArrayList<>();
    }

    /**
     * Constructor with given count of {@link Thread threads}
     * @param countOfThreads count of {@link Thread threads}
     */
    public ParallelMapperImpl(int countOfThreads) {
        if (countOfThreads <= 0) {
            throw new IllegalArgumentException("Can not calculate on 0 or less threads");
        }
        queueTasks = new ArrayDeque<>();
        threadList = new ArrayList<>();
        for (int i = 0; i < countOfThreads; i++) {
            threadList.add(new Thread(() -> {
                while (!Thread.interrupted()) {
                    try {
                        pollTask();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }));
        }
        for (Thread thread : threadList) {
            thread.start();
        }
    }

    private void pollTask() throws InterruptedException {
        Runnable task;
        synchronized (queueTasks) {
            while (queueTasks.isEmpty()) {
                queueTasks.wait();
            }
            task = queueTasks.poll();
        }
        task.run();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> function, List<? extends T> list) throws InterruptedException {
        ResultList<R> results = new ResultList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            int j = i;
            addTask(() -> results.set(j, function.apply(list.get(j))));
        }
        return results.getList();
    }

    private void addTask(Runnable task) {
        synchronized (queueTasks) {
            queueTasks.add(task);
            queueTasks.notify();
        }
    }

    private static class ResultList<R> {
        private List<R> list;
        private int readyCount;

        private ResultList(int size) {
            list = new ArrayList<>(Collections.nCopies(size, null));
            readyCount = 0;
        }

        private void set(int i, R result) {
            synchronized (this) {
                list.set(i, result);
                readyCount++;
                if (readyCount == list.size()) {
                    this.notify();
                }
            }
        }

        private List<R> getList() throws InterruptedException {
            synchronized (this) {
                while (readyCount != list.size()) {
                    this.wait();
                }
            }
            return list;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        for (Thread thread : threadList) {
            thread.interrupt();
        }
        for (Thread thread : threadList){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}