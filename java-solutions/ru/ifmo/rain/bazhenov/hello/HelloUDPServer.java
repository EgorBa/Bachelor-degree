package ru.ifmo.rain.bazhenov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Class implementing {@link HelloServer}.
 *
 * @author Egor Bazhenov
 */
public class HelloUDPServer implements HelloServer {
    private ExecutorService streams;
    private DatagramSocket socket;

    /**
     * Default constructor.
     */
    public HelloUDPServer() {
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error : wrong number of arguments");
            return;
        }
        HelloUDPServer server = new HelloUDPServer();
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
        streams = Executors.newFixedThreadPool(threads);
        final int bufferSize;
        try {
            socket = new DatagramSocket(port);
            bufferSize = socket.getSendBufferSize();
        } catch (SocketException e) {
            System.out.println("Error: Unknown port " + port);
            return;
        }
        for (int i = 0; i < threads; i++) {
            streams.submit(() -> {
                while (!socket.isClosed() && !Thread.interrupted()) {
                    try {
                        final DatagramPacket packet = new DatagramPacket(new byte[bufferSize], bufferSize);
                        socket.receive(packet);
                        String response = "Hello, " + new String(packet.getData(),
                                packet.getOffset(),
                                packet.getLength(),
                                StandardCharsets.UTF_8);
                        packet.setData(response.getBytes(StandardCharsets.UTF_8));
                        socket.send(packet);
                    } catch (IOException ignored) {
                    }
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        socket.close();
        streams.shutdown();
        try {
            streams.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
