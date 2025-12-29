package app.dodb.guessimate.session.drivenadapter.sessionview;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionViewSpringRepository extends JpaRepository<SessionView, String> {
}
