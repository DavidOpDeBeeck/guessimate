package app.dodb.guessimate.lobby.drivenadapter.lobbymetricsview;

import app.dodb.guessimate.lobby.api.LobbyMetricsTO;
import app.dodb.guessimate.lobby.api.query.FindLobbyMetricsQuery;
import app.dodb.guessimate.lobby.port.LobbyMetricsQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface LobbyMetricsViewSpringRepository extends JpaRepository<LobbyMetricsView, String>, LobbyMetricsQueryRepository {

    @Override
    @Query("""
        SELECT new app.dodb.guessimate.lobby.api.LobbyMetricsTO(
            COUNT(CASE WHEN m.connectedUserCount > 0 THEN 1 END),
            COALESCE(SUM(m.connectedUserCount), 0L),
            COALESCE(SUM(m.estimationsCompleted), 0L)
        ) FROM LobbyMetricsView m
        """)
    LobbyMetricsTO find(FindLobbyMetricsQuery query);

    @Query("SELECT COUNT(m) FROM LobbyMetricsView m WHERE m.connectedUserCount > 0")
    long countActiveLobbies();

    @Query("SELECT COALESCE(SUM(m.connectedUserCount), 0) FROM LobbyMetricsView m")
    long countTotalConnectedUsers();

    @Query("SELECT COUNT(m) FROM LobbyMetricsView m WHERE m.status = 'ESTIMATING'")
    long countActiveEstimations();

    @Query("SELECT COUNT(m) FROM LobbyMetricsView m WHERE m.connectedUserCount = 0")
    long countAbandonedLobbies();

    @Query("SELECT COUNT(m) FROM LobbyMetricsView m WHERE m.lastActivity < :threshold")
    long countIdleLobbies(@Param("threshold") Instant threshold);

    @Query("SELECT COALESCE(SUM(m.estimationsCompleted), 0) FROM LobbyMetricsView m")
    long sumEstimationsCompleted();
}
