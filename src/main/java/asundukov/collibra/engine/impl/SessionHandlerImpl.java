package asundukov.collibra.engine.impl;

import asundukov.collibra.engine.CommandHandler;
import asundukov.collibra.engine.MessageSender;
import asundukov.collibra.engine.SessionHandler;
import asundukov.collibra.engine.TimeoutDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionHandlerImpl implements SessionHandler {
    private static final Logger log = LoggerFactory.getLogger(SessionHandlerImpl.class);

    private final String sessionId;
    private final MessageSender messageSender;
    private final TimeoutDetector timeoutHandler;
    private long sessionStartedTime;

    private CommandHandler commandHandler = new DefaultCommandHandlerState();
    private String clientName = "";

    public SessionHandlerImpl(String sessionId, MessageSender messageSender, TimeoutDetector timeoutDetector) {
        this.sessionId = sessionId;
        this.messageSender = messageSender;
        this.timeoutHandler = timeoutDetector;
    }

    @Override
    public void start() {
        sessionStartedTime = System.currentTimeMillis();
        timeoutHandler.renew(this);
        CommandHandlerGraphFactory commandHandlerGraphFactory = new CommandHandlerGraphFactory();
        CommandHandler initState = new CommandHandlerGreeting(this, commandHandlerGraphFactory);
        setCommandHandler(initState);
    }

    @Override
    public void fromClient(String text) {
        log.info("{} -> {}", getSessionId(), text);
        timeoutHandler.renew(this);
        commandHandler = commandHandler.handle(text);
    }

    @Override
    public void toClient(String text) {
        log.info("{} <- {}", getSessionId(), text);
        messageSender.send(text);
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String getClientName() {
        return clientName;
    }

    @Override
    public void setClientName(String name) {
        clientName = name;
    }

    @Override
    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void closeSession() {
        timeoutHandler.cancel(this);
        messageSender.close();
    }

    @Override
    public long getSessionStartTime() {
        return sessionStartedTime;
    }

}
