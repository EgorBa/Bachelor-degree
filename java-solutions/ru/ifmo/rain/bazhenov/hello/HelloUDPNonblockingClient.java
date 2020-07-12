package ru.ifmo.rain.bazhenov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Class implementing {@link HelloClient}.
 *
 * @author Egor Bazhenov
 */
public class HelloUDPNonblockingClient implements HelloClient {
    private static final int TIMEOUT = 10;
    private static final int BUFFER_SIZE = 1024;

    /**
     * Default constructor.
     */
    public HelloUDPNonblockingClient() {
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
        HelloUDPNonblockingClient client = new HelloUDPNonblockingClient();
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
        final InetSocketAddress address;
        try {
            address = new InetSocketAddress(InetAddress.getByName(host), port);
        } catch (UnknownHostException e) {
            System.out.println("Error: Unknown host " + host);
            return;
        }
        try (Selector selector = Selector.open()) {
            for (int i = 0; i < threads; i++) {
                final DatagramChannel datagramChannel = HelloUDPNonblockingUtils.createDatagramChanel(address, 1);
                datagramChannel.register(selector, SelectionKey.OP_WRITE, new ClientAttachment(i, requests, prefix));
            }
            while (selector.keys().size() > 0) {
                HelloUDPNonblockingUtils.checkSelect(selector, SelectionKey.OP_WRITE, TIMEOUT);
                HelloUDPNonblockingUtils.keysProcessing(selector.selectedKeys().iterator(), 1, BUFFER_SIZE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientAttachment {
        public int thread;
        public int countOfRequests = 0;
        public int limit;
        public String prefix;

        public ClientAttachment(int i, int limit, String prefix) {
            thread = i;
            this.limit = limit;
            this.prefix = prefix;
        }
    }

}