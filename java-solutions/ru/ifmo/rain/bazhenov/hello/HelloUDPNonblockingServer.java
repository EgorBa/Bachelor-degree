package ru.ifmo.rain.bazhenov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Class implementing {@link HelloServer}.
 *
 * @author Egor Bazhenov
 */
public class HelloUDPNonblockingServer implements HelloServer {
    private static final int TIMEOUT = 10;
    private static final int BUFFER_SIZE = 1024;

    private static Selector selector;
    private static ExecutorService service;
    private static DatagramChannel channel;

    /**
     * Default constructor.
     */
    public HelloUDPNonblockingServer() {
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error : wrong number of arguments");
            return;
        }
        HelloUDPNonblockingServer server = new HelloUDPNonblockingServer();
        try {
            server.start(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } catch (NumberFormatException e) {
            System.out.println("Error : Illegal arguments");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(int port, int threads) {
        try {
            selector = Selector.open();
            channel = HelloUDPNonblockingUtils.createDatagramChanel(new InetSocketAddress(port), 2);
            channel.register(selector, SelectionKey.OP_READ, new ServerAttachment());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            try {
                while (!Thread.interrupted() && selector.isOpen() && channel.isOpen()) {
                    HelloUDPNonblockingUtils.checkSelect(selector, SelectionKey.OP_READ, TIMEOUT);
                    HelloUDPNonblockingUtils.keysProcessing(selector.selectedKeys().iterator(), 2, BUFFER_SIZE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    static class ServerAttachment {
        public String request;
        public SocketAddress socketAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        service.shutdown();
        try {
            service.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
