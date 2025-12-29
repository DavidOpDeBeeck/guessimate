package app.dodb.guessimate.session.domain.insight;

import app.dodb.guessimate.session.api.EstimationInsight;
import app.dodb.guessimate.session.domain.Estimate;
import app.dodb.guessimate.session.domain.Estimation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static app.dodb.guessimate.session.api.EstimationInsight.SPLIT_DECISION;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingLong;

public class SplitDecisionEstimationInsightResolver implements EstimationInsightResolver {

    private final int minimumParticipants;
    private final int maxVoteDifference;
    private final double minimumTopTwoShare;

    public SplitDecisionEstimationInsightResolver(int minimumParticipants, int maxVoteDifference, double minimumTopTwoShare) {
        this.minimumParticipants = minimumParticipants;
        this.maxVoteDifference = maxVoteDifference;
        this.minimumTopTwoShare = minimumTopTwoShare;
    }

    @Override
    public List<EstimationInsight> resolve(Estimation current, Set<Estimation> previous) {
        if (current.getEstimates().size() < minimumParticipants) {
            return emptyList();
        }

        var votesByEstimate = current.votesByEstimate();
        var topTwoEstimates = votesByEstimate.entrySet().stream()
            .sorted(comparingLong(Map.Entry<Estimate, Long>::getValue).reversed())
            .map(Map.Entry::getKey)
            .limit(2)
            .toList();

        if (topTwoEstimates.size() < 2) {
            return emptyList();
        }

        var firstEstimate = topTwoEstimates.getFirst();
        var secondEstimate = topTwoEstimates.getLast();

        var firstPlaceVotes = votesByEstimate.get(firstEstimate);
        var secondPlaceVotes = votesByEstimate.get(secondEstimate);

        var votesAreClose = Math.abs(firstPlaceVotes - secondPlaceVotes) <= maxVoteDifference;
        var topTwoDominate = (firstPlaceVotes + secondPlaceVotes) / (double) current.getEstimates().size() >= minimumTopTwoShare;
        return votesAreClose && topTwoDominate ? List.of(SPLIT_DECISION) : emptyList();
    }

}

