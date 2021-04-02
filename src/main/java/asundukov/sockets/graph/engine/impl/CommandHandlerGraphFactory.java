package asundukov.sockets.graph.engine.impl;

import asundukov.sockets.graph.engine.CommandHandler;
import asundukov.sockets.graph.engine.SessionHandler;
import asundukov.sockets.graph.engine.graph.Graph;
import asundukov.sockets.graph.engine.graph.impl.GraphImpl;
import asundukov.sockets.graph.engine.graph.impl.GraphSynchronized;

public class CommandHandlerGraphFactory {

    private final Graph graph = new GraphSynchronized(new GraphImpl());

    public CommandHandler create(SessionHandler sessionHandler) {
        return new CommandHandlerGraph(sessionHandler, graph);
    }
}