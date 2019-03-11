package de.itemis.jmo.dodo.tests.util;

import com.github.tomakehurst.wiremock.http.trafficlistener.WiremockNetworkTrafficListener;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import de.itemis.jmo.dodo.util.DodoStallCallback;

public class DownloadTrafficListener implements WiremockNetworkTrafficListener {

    private final List<DodoStallCallback> callbacks = new CopyOnWriteArrayList<>();
    private final AtomicInteger outgoingCallCount = new AtomicInteger(0);

    @Override
    public void opened(Socket socket) {
        System.err.println("opened");
    }

    @Override
    public void incoming(Socket socket, ByteBuffer bytes) {
        System.err.println("incoming");
    }

    @Override
    public void outgoing(Socket socket, ByteBuffer bytes) {
        System.err.println("outgoing " + bytes.limit());
        if (outgoingCallCount.incrementAndGet() == 2) {
            System.err.println("############### " + bytes.limit());
            callbacks.forEach(callback -> callback.stallPointReached());
            outgoingCallCount.set(0);
            callbacks.clear();
        }
    }

    @Override
    public void closed(Socket socket) {

    }

    /**
     * Adds the provided {@code callback}.
     */
    public void addCallback(DodoStallCallback callback) {
        callbacks.add(callback);
    }
}
