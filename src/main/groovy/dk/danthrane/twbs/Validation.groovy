package dk.danthrane.twbs

enum Validation {
    DEFAULT(icon: null, baseName: ""),
    SUCCESS(icon: Icon.OK, baseName: "success"),
    WARNING(icon: Icon.ALERT, baseName: "warning"),
    ERROR(icon: Icon.REMOVE, baseName: "error")

    Icon icon
    String baseName
}