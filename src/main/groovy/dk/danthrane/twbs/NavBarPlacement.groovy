package dk.danthrane.twbs

/**
 * @author Dan Thrane
 */
enum NavBarPlacement {
    DEFAULT(className: ""),
    FIXED_TO_TOP(className: "navbar-fixed-top"),
    FIXED_TO_BOTTOM(className: "navbar-fixed-bottom"),
    STATIC_TOP(className: "navbar-static-top")

    String className
}
