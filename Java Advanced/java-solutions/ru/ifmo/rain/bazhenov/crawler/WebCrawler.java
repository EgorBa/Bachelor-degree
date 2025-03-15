package ru.ifmo.rain.bazhenov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Class implementing {@link Crawler}.
 *
 * @author Egor Bazhenov
 */
@SuppressWarnings("unused")
public class WebCrawler implements Crawler {
    private final ExecutorService downloaders;
    private final ExecutorService extractors;
    private final Downloader downloader;

    /**
     * Default constructor.
     */
    public WebCrawler() {
        this(null, 0, 0, 0);
    }

    /**
     * Constructor with given parameters.
     *
     * @param downloader  {@link Downloader}.
     * @param downloaders maximum amount of pages to download at one time.
     * @param extractors  maximum amount of pages to extract links.
     * @param perHost     maximum amount of pages to download from 1 host.
     */
    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloaders = Executors.newFixedThreadPool(downloaders);
        this.extractors = Executors.newFixedThreadPool(extractors);
        this.downloader = downloader;
    }

    /**
     * Execute program with given parameters.
     *
     * @param args {@link java.net.URL},
     *             depth,
     *             maximum amount of pages to download at one time,
     *             maximum amount of pages to extract links,
     *             maximum amount of pages to download from 1 host.
     * @throws IOException can throw while creating {@link Downloader}.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Error: wrong amount of arguments arguments");
            return;
        }
        int depth, downloaders, extractors, perHost;
        try {
            depth = Integer.parseInt(args[1]);
            downloaders = Integer.parseInt(args[2]);
            extractors = Integer.parseInt(args[3]);
            perHost = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error: illegal arguments");
        }
        Downloader downloader = new CachingDownloader();
        WebCrawler crawler = new WebCrawler(downloader, downloaders, extractors, perHost);
        Result result = crawler.download(args[0], depth);
        System.out.println("Downloaded pages without errors:");
        for (String url : result.getDownloaded()) {
            System.out.println(url);
        }
        System.out.println("Downloaded pages with errors:");
        for (String url : result.getErrors().keySet()) {
            System.out.println(url);
            System.out.println("Error: " + result.getErrors().get(url).getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result download(String url, int depth) {
        Set<String> downloaded = ConcurrentHashMap.newKeySet();
        Map<String, IOException> errors = new ConcurrentHashMap<>();
        Phaser phaser = new Phaser(1);
        downloaded.add(url);
        downloadPage(url, depth, downloaded, errors, phaser);
        phaser.arriveAndAwaitAdvance();
        downloaded.removeAll(errors.keySet());
        return new Result(new ArrayList<>(downloaded), errors);
    }

    private void downloadPage(final String url, final int depth, final Set<String> downloaded,
                              final Map<String, IOException> errors, final Phaser phaser) {
        if (depth > 0) {
            phaser.register();
            downloaders.submit(() -> {
                try {
                    Document document = downloader.download(url);
                    if (depth > 1) {
                        phaser.register();
                        extractors.submit(() -> {
                            try {
                                for (String link : document.extractLinks()) {
                                    if (downloaded.add(link)) {
                                        downloadPage(link, depth - 1, downloaded, errors, phaser);
                                    }
                                }
                            } catch (IOException e) {
                                errors.put(url, e);
                            } finally {
                                phaser.arrive();
                            }
                        });
                    }
                } catch (IOException e) {
                    errors.put(url, e);
                } finally {
                    phaser.arrive();
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        downloaders.shutdownNow();
        extractors.shutdownNow();
    }
}
