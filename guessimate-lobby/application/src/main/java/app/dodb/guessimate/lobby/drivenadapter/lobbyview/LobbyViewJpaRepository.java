package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import app.dodb.guessimate.lobby.api.event.LobbyConfiguration;
import app.dodb.guessimate.lobby.api.event.LobbyInfo;
import app.dodb.guessimate.lobby.api.event.UserInfo;
import app.dodb.guessimate.lobby.api.query.FindActiveLobbiesQuery;
import app.dodb.guessimate.lobby.api.query.FindLobbyInfoForUserQuery;
import app.dodb.guessimate.lobby.domain.EstimationId;
import app.dodb.guessimate.lobby.domain.Lobby;
import app.dodb.guessimate.lobby.domain.LobbyState;
import app.dodb.guessimate.lobby.domain.SessionId;
import app.dodb.guessimate.lobby.domain.User;
import app.dodb.guessimate.lobby.domain.UserId;
import app.dodb.guessimate.lobby.port.LobbyQueryRepository;
import app.dodb.guessimate.lobby.port.LobbyRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
public class LobbyViewJpaRepository implements LobbyRepository, LobbyQueryRepository {

    private final LobbyViewSpringRepository lobbyRepository;

    public LobbyViewJpaRepository(LobbyViewSpringRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    @Override
    public List<String> find(FindActiveLobbiesQuery query) {
        return lobbyRepository.findActiveSessionIds();
    }

    @Override
    public Optional<LobbyInfo> find(FindLobbyInfoForUserQuery query) {
        return lobbyRepository.findById(query.sessionId())
            .map(view -> new LobbyInfo(
                view.getSessionId(),
                new LobbyConfiguration(
                    view.getDeck(),
                    view.isAutoReveal(),
                    view.getAutoJoinRole().orElse(null),
                    view.getTimerDuration(),
                    view.isReactionsEnabled()
                ),
                view.getPreviousEstimationId().orElse(null),
                view.getStatus(),
                view.getUsers().stream()
                    .map(user -> new UserInfo(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEstimate().orElse(null),
                        user.getReaction().orElse(null),
                        user.getRole().orElse(null),
                        user.getUserId().equals(query.userId())
                    ))
                    .toList(),
                view.getTimerExpiresAt().map(Instant::toString).orElse(null)
            ));
    }

    @Override
    public Optional<Lobby> findBySessionId(SessionId sessionId) {
        return lobbyRepository.findById(sessionId.value())
            .map(this::toLobby);
    }

    private Lobby toLobby(LobbyView view) {
        return new Lobby(new LobbyState(
            new SessionId(view.getSessionId()),
            view.getDeck(),
            view.isAutoReveal(),
            view.getAutoJoinRole().orElse(null),
            view.getTimerDuration(),
            view.isReactionsEnabled(),
            view.getPreviousEstimationId()
                .map(EstimationId::new)
                .orElse(null),
            view.getTimerExpiresAt()
                .orElse(null),
            view.getStatus(),
            view.getUsers().stream()
                .map(user -> new User(
                    new UserId(user.getUserId()),
                    user.getUsername(),
                    user.getEstimate().orElse(null),
                    user.getReaction().orElse(null),
                    user.getRole().orElse(null)
                ))
                .collect(toList())
        ));
    }

}
