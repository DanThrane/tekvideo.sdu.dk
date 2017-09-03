package dk.danthrane.twbs

/**
 * @author Dan Thrane
 */
enum ButtonStyle {
    DEFAULT(clazz: "default"),
    PRIMARY(clazz: "primary"),
    SUCCESS(clazz: "success"),
    INFO(clazz: "info"),
    WARNING(clazz: "warning"),
    DANGER(clazz: "danger"),
    LINK(clazz: "link")

    String clazz
}