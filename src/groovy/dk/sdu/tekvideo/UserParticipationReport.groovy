package dk.sdu.tekvideo

class UserParticipationReport {
    NodeIdentifier identifier
    List<NodeIdentifier> children
    List<UserParticipation> participation
    Map<String, UserParticipationReport> details

    List<UserParticipation> findUserParticipationFromDetails() {
        Map<User, UserParticipation> participationMap = [:]
        details.values().each { report ->
            report.participation.each { participation ->
                def user = participation.user
                def current = participationMap[user]
                if (current == null) {
                    current = new UserParticipation()
                    current.stats = new GradingStats()
                    current.stats.score = 0
                    current.stats.maxScore = 0
                }

                current.user = user
                current.isStudent = participation.isStudent
                current.stats.maxScore += participation.stats.maxScore
                current.stats.score += participation.stats.score

                participationMap[user] = current
            }
        }
        return participationMap.values().toList()
    }
}
