package asundukov.sockets.graph.socket.impl;

import asundukov.sockets.graph.engine.Engine;
import asundukov.sockets.graph.socket.SocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SocketConnectionBlockingThreadBased implements SocketConnection {
    private static final Logger log = LoggerFactory.getLogger(SocketConnectionBlockingThreadBased.class);

    private final SocketConnection socketConnection;


    public SocketConnectionBlockingThreadBased(SocketConnection socketConnection) {
        this.socketConnection = socketConnection;
    }

    @Override
    public void start(Engine engine) throws IOException {
        Thread thread = new Thread(() -> {
            try {
                socketConnection.start(engine);
            } catch (IOException e) {
                close();
            }
        });
        thread.start();
    }

    @Override
    public void close() {
        try {
            socketConnection.close();
        } catch (Exception ignored) {
        }
    }
}

