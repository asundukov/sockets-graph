package asundukov.collibra.engine.impl.graph;

import asundukov.collibra.engine.graph.Graph;
import asundukov.collibra.engine.graph.exception.NodeAlreadyExistsException;
import asundukov.collibra.engine.graph.exception.NodeDoesNotExistException;

import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static asundukov.collibra.engine.CommandHandler.DONT_KNOW_ANSWER;
import static java.math.BigInteger.ZERO;
import static java.util.regex.Pattern.compile;

enum GraphCommand {
    ADD_NODE("^ADD NODE ([\\w\\d-]+)$", (Graph g, Matcher m) -> {
        try {
            g.add(m.group(1));
            return "NODE ADDED";
        } catch (NodeAlreadyExistsException e) {
            return "ERROR: NODE ALREADY EXISTS";
        }
    }),
    DEL_NODE("^REMOVE NODE ([\\w\\d-]+)$", (Graph g, Matcher m) -> {
        try {
            g.remove(m.group(1));
            return "NODE REMOVED";
        } catch (NodeDoesNotExistException e) {
            return "ERROR: NODE NOT FOUND";
        }
    }),
    ADD_EDGE("^ADD EDGE ([\\w\\d-]+) ([\\w\\d-]+) ([\\d]+)$", (Graph g, Matcher m) -> {
        try {
            BigInteger value = new BigInteger(m.group(3));
            if (value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                return DONT_KNOW_ANSWER;
            }
            if (value.compareTo(ZERO) <= 0) {
                return DONT_KNOW_ANSWER;
            }
            g.addEdge(m.group(1), m.group(2), value.intValue());
            return "EDGE ADDED";
        } catch (NodeDoesNotExistException e) {
            return "ERROR: NODE NOT FOUND";
        }
    }),
    DEL_EDGE("^REMOVE EDGE ([\\w\\d-]+) ([\\w\\d-]+)$", (Graph g, Matcher m) -> {
        try {
            g.removeEdge(m.group(1), m.group(2));
            return "EDGE REMOVED";
        } catch (NodeDoesNotExistException e) {
            return "ERROR: NODE NOT FOUND";
        }
    });

    private final Pattern pattern;
    private final BiFunction<Graph, Matcher, String> handler;

    GraphCommand(String regExp, BiFunction<Graph, Matcher, String> handler) {
        this.pattern = compile(regExp);
        this.handler = handler;
    }

    public Matcher match(String command) {
        return pattern.matcher(command);
    }

    public String execute(Graph graph, Matcher matcher) {
        return handler.apply(graph, matcher);
    }
}