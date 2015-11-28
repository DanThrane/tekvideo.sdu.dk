package dk.sdu.tekvideo

enum DashboardPeriod {
    TODAY(1, "Fra i dag"),
    WEEK(7, "Over den sidste uge"),
    MONTH(30, "Over den sidste måned"),
    SIX_MONTH(182, "Over de sidste 6 måneder"),
    YEAR(365, "Over det sidste år"),
    SINCE_START(0, "Siden start")

    final int id
    final String name
    DashboardPeriod(int id, String name) {
        this.id = id
        this.name = name
    }

    @Override
    String toString() { name }
}