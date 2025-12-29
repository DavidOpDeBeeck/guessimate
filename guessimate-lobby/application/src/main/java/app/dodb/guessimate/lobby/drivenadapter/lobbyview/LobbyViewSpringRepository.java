package app.dodb.guessimate.lobby.drivenadapter.lobbyview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LobbyViewSpringRepository extends JpaRepository<LobbyView, String> {

    @Query(value = "SELECT session_id FROM lobby_view WHERE jsonb_array_length(data::jsonb -> 'users') > 0", nativeQuery = true)
    List<String> findActiveSessionIds();
}
