package dk.sdu.tekvideo.stats

import dk.sdu.tekvideo.User
import dk.sdu.tekvideo.Node

class ProgressTable {
    List<ProgressHeadCell> head = []
    List<UserProgression> body = []
}

class ProgressHeadCell {
    String title
    String url
    Node node
    Long maxScore

    @Override
    String toString() {
        return "$title [Node=$node, url=$url, maxScore=$maxScore]"
    }
}

class UserProgression {
    StatsUser user
    List<ProgressBodyCell> cells = []
}

interface StatsUser {}

class StatsGuestUser implements StatsUser {
    String token

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        StatsGuestUser that = (StatsGuestUser) o

        if (token != that.token) return false

        return true
    }

    int hashCode() {
        return token.hashCode()
    }

    @Override
    String toString() {
        return token
    }
}

class StatsAuthenticatedUser implements StatsUser {
    User user

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        StatsAuthenticatedUser that = (StatsAuthenticatedUser) o

        if (user != that.user) return false

        return true
    }

    int hashCode() {
        return user.hashCode()
    }

    @Override
    String toString() {
        return user.username
    }
}

class ProgressBodyCell {
    Long score
    ProgressionStatus status

    @Override
    String toString() {
        return "$score [$status]"
    }
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
