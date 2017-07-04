package dk.sdu.tekvideo.stats

interface ViewingStatisticsConfiguration {}

class StandardViewingStatisticsConfiguration implements ViewingStatisticsConfiguration {
    Boolean cumulative
}

class WeeklyViewingStatisticsConfiguration implements ViewingStatisticsConfiguration {}
