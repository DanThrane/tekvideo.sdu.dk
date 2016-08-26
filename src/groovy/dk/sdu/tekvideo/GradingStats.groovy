package dk.sdu.tekvideo

class GradingStats {
    NodeIdentifier identifier
    Integer score
    Integer maxScore
    Boolean seen
    Map<NodeIdentifier, GradingStats> children

    boolean calculateSeenFromChildren() {
        return children.values().every { it.seen }
    }

    int calculateMaxScoreFromChildren() {
        return ((Integer) children.values().collect { it.maxScore }.sum()) ?: 0
    }
    int calculateScoreFromChildren() {
        return ((Integer) children.values().collect { it.score }.sum()) ?: 0
    }

    void updateStatsFromChildren() {
        seen = calculateSeenFromChildren()
        maxScore = calculateMaxScoreFromChildren()
        score = calculateScoreFromChildren()
    }

}
