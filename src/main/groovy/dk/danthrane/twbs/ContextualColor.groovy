package dk.danthrane.twbs

/**
 * @author Dan Thrane
 */
enum ContextualColor {
    DEFAULT(baseName: ""),
    ACTIVE(baseName: "active"),
    SUCCESS(baseName: "success"),
    WARNING(baseName: "warning"),
    DANGER(baseName: "danger"),
    INFO(baseName: "info")

    String baseName
}