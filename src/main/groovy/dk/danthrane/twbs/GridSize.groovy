package dk.danthrane.twbs

enum GridSize {
    XS("xs"),
    SM("sm"),
    MD("md"),
    LG("lg"),
    EXTRA_SMALL("xs"),
    SMALL("sm"),
    MEDIUM("md"),
    LARGE("lg")

    String columnName

    private GridSize(String columnName) {
        this.columnName = columnName
    }

    String getClassName(int columns) {
        return "col-$columnName-$columns"
    }

    String getOffsetName(int columns) {
        return "col-$columnName-offset-$columns"
    }
}
