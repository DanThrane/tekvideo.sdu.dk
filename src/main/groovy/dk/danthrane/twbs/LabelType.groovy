package dk.danthrane.twbs

enum LabelType {
    DEFAULT(clazz: null),
    PRIMARY(clazz: "primary"),
    SUCCESS(clazz: "success"),
    INFO(clazz: "info"),
    WARNING(clazz: "warning"),
    DANGER(clazz: "danger")

    String clazz
}
