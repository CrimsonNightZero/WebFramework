package org.web.domain.core;

import java.util.ArrayList;
import java.util.List;

public class SocketAddress {
    private final int port;
    private final List<ProtocolListener> protocolListeners;

    public SocketAddress(int port) {
        this.port = port;
        this.protocolListeners = new ArrayList<>();
    }

    public int getPort() {
        return port;
    }

    public HTTPResponse request(HTTPRequest httpRequest) {
        return protocolListeners.stream().map(listener -> listener.send(port, httpRequest)).findFirst().orElseThrow();
    }

    public void subscribe(ProtocolListener protocolListener) {
        protocolListeners.add(protocolListener);
    }

    public void unsubscribe(ProtocolListener protocolListener) {
        protocolListeners.remove(protocolListener);
    }
}
