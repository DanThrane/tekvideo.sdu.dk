package dk.sdu.tekvideo

class DashboardHeroInformation {
    String title
    String image
    String mainStatAmount
    String mainStatUnit
    List<DashboardHeroStat> stats
}

class DashboardHeroStat {
    String icon
    String content
}
