package ru.ifmo.rain.bazhenov.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class implementing {@link ScalarIP}. Processes lists in multiple threads.
 *
 * @author Egor Bazhenov
 */
@SuppressWarnings("unused")
public class IterativeParallelism implements ScalarIP {
    private final ParallelMapper mapper;

    /**
     * Default constructor.
     */
    public IterativeParallelism() {
        this(null);
    }

    /**
     * Constructor with given {@link ParallelMapper}.
     * @param mapper given {@link ParallelMapper}.
     */
    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        if (values.isEmpty()) {
            throw new NoSuchElementException("Can not find element in empty list");
        }
        Function<List<? extends T>, ? extends T> getMax = (args) -> Collections.max(args, comparator);
        return getMax.apply(calculate(threads, values, getMax));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, comparator.reversed());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        Function<List<? extends T>, Boolean> allMatch = (list) -> list.stream().allMatch(predicate);
        return calculate(threads, values, allMatch).stream().allMatch(Boolean::booleanValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, Predicate.not(predicate));
    }

    private <T, R> List<R> calculate(int countOfThreads, List<? extends T> list, Function<List<? extends T>, R> function)
            throws InterruptedException, IllegalArgumentException {
        if (countOfThreads <= 0) {
            throw new IllegalArgumentException("Can not calculate on 0 or less threads");
        }
        int shift = 0;
        int usedThreads = Math.min(countOfThreads, list.size());
        int sublistSize = list.size() / usedThreads;
        int mod = list.size() - sublistSize * usedThreads;
        List<List<? extends T>> sublists = new ArrayList<>();
        for (int i = 0; i < usedThreads; i++) {
            int leftBoard = shift + i * sublistSize;
            int rightBoard = shift + (i + 1) * sublistSize + (i < mod ? 1 : 0);
            if (i < mod) {
                shift++;
            }
            sublists.add(list.subList(leftBoard, rightBoard));
        }
        if (mapper != null) {
            return mapper.map(function, sublists);
        } else {
            return map(usedThreads, sublists, function);
        }
    }

    private <T, R> List<R> map(int usedThreads, List<List<? extends T>> list, Function<List<? extends T>, ? extends R> function) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        List<R> result = new ArrayList<>(Collections.nCopies(usedThreads, null));
        for (int i = 0; i < usedThreads; i++) {
            int j = i;
            threads.add(new Thread(() -> result.set(j, function.apply(list.get(j)))));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return result;
    }
}