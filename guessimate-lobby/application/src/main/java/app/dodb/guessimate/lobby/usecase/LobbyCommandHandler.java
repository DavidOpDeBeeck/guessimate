package app.dodb.guessimate.lobby.usecase;

import app.dodb.guessimate.lobby.api.command.CheckLobbyActivityCommand;
import app.dodb.guessimate.lobby.api.command.ClearEstimateCommand;
import app.dodb.guessimate.lobby.api.command.CompleteEstimationCommand;
import app.dodb.guessimate.lobby.api.command.ConnectUserToLobbyCommand;
import app.dodb.guessimate.lobby.api.command.DisconnectUserFromLobbyCommand;
import app.dodb.guessimate.lobby.api.command.SetAutoJoinCommand;
import app.dodb.guessimate.lobby.api.command.SetAutoRevealCommand;
import app.dodb.guessimate.lobby.api.command.SetDeckCommand;
import app.dodb.guessimate.lobby.api.command.SetEstimateCommand;
import app.dodb.guessimate.lobby.api.command.SetReactionCommand;
import app.dodb.guessimate.lobby.api.command.SetReactionsEnabledCommand;
import app.dodb.guessimate.lobby.api.command.SetTimerDurationCommand;
import app.dodb.guessimate.lobby.api.command.SetUserRoleCommand;
import app.dodb.guessimate.lobby.api.command.SetUsernameCommand;
import app.dodb.guessimate.lobby.api.command.StartEstimationCommand;
import app.dodb.guessimate.lobby.api.command.TryCompleteEstimationCommand;
import app.dodb.guessimate.lobby.api.command.UserActionCommand;
import app.dodb.guessimate.lobby.domain.Lobby;
import app.dodb.guessimate.lobby.domain.SessionId;
import app.dodb.guessimate.lobby.domain.UserId;
import app.dodb.guessimate.lobby.port.LobbyRepository;
import app.dodb.guessimate.lobby.port.UserConnectivityChecker;
import app.dodb.guessimate.lobby.port.UsernameGenerator;
import app.dodb.guessimate.session.api.query.FindDeckByNameQuery;
import app.dodb.smd.api.command.CommandGateway;
import app.dodb.smd.api.command.CommandHandler;
import app.dodb.smd.api.event.EventPublisher;
import app.dodb.smd.api.metadata.datetime.DatetimeProvider;
import app.dodb.smd.api.query.QueryGateway;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.function.Consumer;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Component
public class LobbyCommandHandler {

    private static final int MAX_USERNAME_LENGTH = 16;

    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;
    private final EventPublisher eventPublisher;
    private final LobbyRepository lobbyRepository;
    private final UsernameGenerator usernameGenerator;
    private final DatetimeProvider datetimeProvider;
    private final UserConnectivityChecker userConnectivityChecker;

    public LobbyCommandHandler(QueryGateway queryGateway,
                               CommandGateway commandGateway,
                               EventPublisher eventPublisher,
                               LobbyRepository lobbyRepository,
                               UsernameGenerator usernameGenerator,
                               DatetimeProvider datetimeProvider,
                               UserConnectivityChecker userConnectivityChecker) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
        this.eventPublisher = eventPublisher;
        this.lobbyRepository = lobbyRepository;
        this.usernameGenerator = usernameGenerator;
        this.datetimeProvider = datetimeProvider;
        this.userConnectivityChecker = userConnectivityChecker;
    }

    @CommandHandler
    public void handle(ConnectUserToLobbyCommand command) {
        var userId = new UserId(command.userId());
        var sessionId = new SessionId(command.sessionId());
        doWithLobby(sessionId, lobby -> lobby.connect(userId, determineUsername(command)));
    }

    @CommandHandler
    public void handle(DisconnectUserFromLobbyCommand command) {
        var userId = new UserId(command.userId());
        var sessionId = new SessionId(command.sessionId());
        doWithLobby(sessionId, lobby -> lobby.disconnect(userId));
    }

    @CommandHandler
    public void handle(UserActionCommand<?> command) {
        var userId = new UserId(command.userId());
        var sessionId = new SessionId(command.sessionId());
        doWithLobby(sessionId, lobby -> {
            Instant now = datetimeProvider.now().atZone(ZoneId.systemDefault()).toInstant();
            switch (command.command()) {
                case SetUserRoleCommand(var role) -> lobby.setUserRole(userId, role);
                case SetUsernameCommand(var username) -> lobby.setUserName(userId, username);
                case SetEstimateCommand(var estimate) -> lobby.setEstimate(userId, estimate);
                case ClearEstimateCommand() -> lobby.clearEstimate(userId);
                case SetDeckCommand(var deckName) -> queryGateway.send(new FindDeckByNameQuery(deckName)).ifPresent(lobby::setDeck);
                case SetAutoRevealCommand(var enabled) -> lobby.setAutoReveal(enabled);
                case SetAutoJoinCommand(var role) -> lobby.setAutoJoin(role);
                case SetTimerDurationCommand(var timerDuration) -> lobby.setTimerDuration(timerDuration, now);
                case SetReactionsEnabledCommand(var enabled) -> lobby.setReactionsEnabled(enabled);
                case SetReactionCommand(var reaction) -> lobby.setReaction(userId, reaction);
                case StartEstimationCommand() -> lobby.startEstimation(now);
                case CompleteEstimationCommand() -> lobby.completeEstimation(commandGateway);
            }
        });
    }

    @CommandHandler
    public void handle(TryCompleteEstimationCommand command) {
        var sessionId = new SessionId(command.sessionId());
        doWithLobby(sessionId, lobby -> lobby.tryAutoCompleteEstimation(commandGateway));
    }

    @CommandHandler
    public void handle(CheckLobbyActivityCommand command) {
        var sessionId = new SessionId(command.sessionId());
        doWithLobby(sessionId, lobby -> lobby.checkActivity(userConnectivityChecker));
    }

    private void doWithLobby(SessionId sessionId, Consumer<Lobby> consumer) {
        var lobby = lobbyRepository.findBySessionId(sessionId).orElseThrow();
        consumer.accept(lobby);
        lobby.tryAutoCompleteEstimation(commandGateway);
        lobby.consumeEvents().forEach(eventPublisher::publish);
    }

    private String determineUsername(ConnectUserToLobbyCommand command) {
        var usernameFromCommand = command.username().orElse(null);
        if (isBlank(usernameFromCommand)) {
            return usernameGenerator.generate();
        }
        return usernameFromCommand.substring(0, usernameFromCommand.length() % MAX_USERNAME_LENGTH);
    }
}
