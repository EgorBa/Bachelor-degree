package ru.ifmo.rain.bazhenov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class implementing {@link HelloClient}.
 *
 * @author Egor Bazhenov
 */
public class HelloUDPClient implements HelloClient {
    private static final int TIMEOUT = 500;

    /**
     * Default constructor.
     */
    public HelloUDPClient() {
    }

    /**
     * Execute program with given parameters.
     *
     * @param args host,
     *             port,
     *             prefix of requests,
     *             amount of streams,
     *             amount of requests.
     */
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Error : wrong number of arguments");
            return;
        }
        HelloUDPClient client = new HelloUDPClient();
        try {
            client.run(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        } catch (NumberFormatException e) {
            System.out.println("Error : Illegal arguments");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        ExecutorService streams = Executors.newFixedThreadPool(threads);
        final InetSocketAddress address;
        try {
            address = new InetSocketAddress(InetAddress.getByName(host), port);
        } catch (UnknownHostException e) {
            System.out.println("Error: Unknown host " + host);
            return;
        }
        for (int i = 0; i < threads; i++) {
            final int count = i;
            streams.submit(() -> {
                try (final DatagramSocket socket = new DatagramSocket()) {
                    socket.setSoTimeout(TIMEOUT);
                    final int bufferSize = socket.getReceiveBufferSize();
                    final DatagramPacket packet = new DatagramPacket(new byte[bufferSize], bufferSize,address);
                    for (int j = 0; j < requests; j++) {
                        String request = prefix + count + "_" + j;
                        while (!socket.isClosed() && !Thread.interrupted()) {
                            try {
                                packet.setData(request.getBytes(StandardCharsets.UTF_8));
                                socket.send(packet);
                                packet.setData(new byte[bufferSize], 0, bufferSize);
                                socket.receive(packet);
                                String response = new String(packet.getData(),
                                        packet.getOffset(),
                                        packet.getLength(),
                                        StandardCharsets.UTF_8);
                                if (response.contains(request)) {
                                    System.out.println(response);
                                    break;
                                }
                            } catch (IOException ignored) {
                            }
                        }
                    }
                } catch (IOException ignored) {
                }
            });
        }
        streams.shutdown();
        try {
            streams.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}