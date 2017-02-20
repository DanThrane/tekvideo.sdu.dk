package dk.sdu.tekvideo.stats

import dk.sdu.tekvideo.User
import grails.util.Pair

class ProgressTable {
    List<ProgressHeadCell> head = []
    List<UserProgression> body = []
}

class ProgressHeadCell {
    String title
    String url
    Node node
    Long maxScore
}

class UserProgression {
    StatsUser user
    List<ProgressBodyCell> cells = []
}

interface StatsUser {}

class StatsGuestUser implements StatsUser {
    String token

    @Override
    String toString() {
        return "Gæst#${token}"
    }
}

class StatusAuthenticatedUser implements StatsUser {
    User user

    @Override
    String toString() {
        return user.username
    }
}

class ProgressBodyCell {
    Long score
    ProgressionStatus status
}

enum ProgressionStatus {
    NOT_STARTED,
    STARTED_LITTLE_PROGRESS,
    WORKING_ON_IT,
    PERFECT

    String getStyleName() {
        return toString()
    }
}
