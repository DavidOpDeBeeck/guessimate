package app.dodb.guessimate.session.domain;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.api.event.EstimationAddedEvent;
import app.dodb.guessimate.session.api.event.EstimationRemovedEvent;
import app.dodb.guessimate.session.api.event.SessionCreatedEvent;
import app.dodb.guessimate.session.api.event.SessionEvent;
import app.dodb.guessimate.session.domain.deck.Deck;
import app.dodb.guessimate.session.domain.insight.AllUniqueEstimationInsightResolver;
import app.dodb.guessimate.session.domain.insight.ConsensusEstimationInsightResolver;
import app.dodb.guessimate.session.domain.insight.EstimationInsightResolver;
import app.dodb.guessimate.session.domain.insight.FrequentRevoteEstimationInsightResolver;
import app.dodb.guessimate.session.domain.insight.HighDisagreementEstimationInsightResolver;
import app.dodb.guessimate.session.domain.insight.NearConsensusEstimationInsightResolver;
import app.dodb.guessimate.session.domain.insight.OutlierPresentEstimationInsightResolver;
import app.dodb.guessimate.session.domain.insight.SoloEstimationInsightResolver;
import app.dodb.guessimate.session.domain.insight.SplitDecisionEstimationInsightResolver;
import app.dodb.smd.api.event.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public class Session {

    private static final int MAX_ESTIMATIONS = 10;
    private static final List<EstimationInsightResolver> ESTIMATION_INSIGHT_RESOLVERS = List.of(
        new SoloEstimationInsightResolver(),
        new ConsensusEstimationInsightResolver(),
        new NearConsensusEstimationInsightResolver(0.75),
        new HighDisagreementEstimationInsightResolver(4, 2),
        new SplitDecisionEstimationInsightResolver(4, 1, 0.7),
        new OutlierPresentEstimationInsightResolver(4, 2),
        new FrequentRevoteEstimationInsightResolver(),
        new AllUniqueEstimationInsightResolver(3)
    );

    private final SessionState state;
    private final List<Event> events = new ArrayList<>();

    public Session(SessionState state) {
        this.state = requireNonNull(state);
    }

    public Session(SessionId sessionId) {
        this(new SessionState(sessionId, new HashSet<>()));
        record(new SessionCreatedEvent(sessionId.value()));
    }

    public EstimationId addEstimation(Deck deck, List<Estimate> estimates, LocalDateTime timestamp) {
        var validEstimates = estimates.stream()
            .filter(deck::exists)
            .sorted(deck.comparator())
            .toList();

        if (validEstimates.isEmpty()) {
            throw new IllegalArgumentException("No valid estimates provided");
        }

        if (state.getEstimations().size() == MAX_ESTIMATIONS) {
            var oldestEstimation = state.findOldestEstimation();
            record(new EstimationRemovedEvent(sessionId(), oldestEstimation.getEstimationId().value()));
        }

        var estimationId = EstimationId.generate();
        var estimation = new Estimation(estimationId, timestamp, validEstimates, deck);
        var insights = determineInsights(estimation);

        record(new EstimationAddedEvent(
            sessionId(),
            estimationId.value(),
            timestamp,
            deck.asDeckTO(),
            validEstimates.stream().map(Estimate::value).toList(),
            insights
        ));

        return estimationId;
    }

    public List<Event> consumeEvents() {
        List<Event> copy = new ArrayList<>(events);
        events.clear();
        return copy;
    }

    private void record(SessionEvent event) {
        switch (event) {
            case SessionCreatedEvent e -> state.apply(e);
            case EstimationAddedEvent e -> state.apply(e);
            case EstimationRemovedEvent e -> state.apply(e);
        }
        events.add(event);
    }

    private Set<EstimationInsight> determineInsights(Estimation estimation) {
        return ESTIMATION_INSIGHT_RESOLVERS.stream()
            .flatMap(resolver -> resolver.resolve(estimation, state.getEstimations()).stream())
            .collect(toSet());
    }

    private String sessionId() {
        return state.getSessionId().value();
    }
}