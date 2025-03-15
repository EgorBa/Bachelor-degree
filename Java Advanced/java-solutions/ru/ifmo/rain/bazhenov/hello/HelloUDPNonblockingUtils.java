package ru.ifmo.rain.bazhenov.hello;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class HelloUDPNonblockingUtils {

    public static void checkSelect(Selector selector, int mode, int timeout) throws IOException {
        if (selector.select(timeout) == 0) {
            selector.keys().forEach(selectionKey -> selectionKey.interestOps(mode));
        }
    }

    public static DatagramChannel createDatagramChanel(InetSocketAddress address, int mode) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        switch (mode) {
            case 1: {
                channel.connect(address);
                break;
            }
            case 2: {
                channel.bind(address);
                break;
            }
        }
        return channel;
    }

    public static HashSet<SelectionKey> validateSelectedKeys(Iterator<SelectionKey> iterator) {
        HashSet<SelectionKey> set = new HashSet<>();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isValid()) {
                set.add(key);
            }
        }
        return set;
    }

    public static void keysProcessing(Iterator<SelectionKey> iterator, int mode, int bufferSize) throws IOException {
        Set<SelectionKey> set = validateSelectedKeys(iterator);
        for (SelectionKey key : set) {
            DatagramChannel channel = (DatagramChannel) key.channel();
            HelloUDPNonblockingClient.ClientAttachment clientAttachment = null;
            HelloUDPNonblockingServer.ServerAttachment serverAttachment = null;
            String request = null;
            switch (mode) {
                case 1: {
                    clientAttachment = (HelloUDPNonblockingClient.ClientAttachment) key.attachment();
                    request = clientAttachment.prefix + clientAttachment.thread + "_" + clientAttachment.countOfRequests;
                    break;
                }
                case 2: {
                    serverAttachment = (HelloUDPNonblockingServer.ServerAttachment) key.attachment();
                    request = serverAttachment.request;
                    break;
                }
            }
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            if (key.isReadable()) {
                switch (mode) {
                    case 1: {
                        channel.read(buffer);
                        String receive = new String(buffer.array(), StandardCharsets.UTF_8).trim();
                        if (receive.contains(request)) {
                            System.out.println(receive);
                            clientAttachment.countOfRequests++;
                            if (clientAttachment.countOfRequests == clientAttachment.limit) {
                                channel.close();
                            }
                        }
                        break;
                    }
                    case 2: {
                        serverAttachment.socketAddress = channel.receive(buffer);
                        serverAttachment.request = "Hello, " + new String(buffer.array(), StandardCharsets.UTF_8).trim();
                        break;
                    }
                }
                if (channel.isOpen()) {
                    key.interestOps(SelectionKey.OP_WRITE);
                }
            } else {
                if (key.isWritable()) {
                    buffer = ByteBuffer.wrap(Objects.requireNonNull(request).getBytes(StandardCharsets.UTF_8));
                    switch (mode) {
                        case 1: {
                            channel.write(buffer);
                            break;
                        }
                        case 2: {
                            channel.send(buffer, serverAttachment.socketAddress);
                            break;
                        }
                    }
                    key.interestOps(SelectionKey.OP_READ);
                }
            }
        }
    }

}
