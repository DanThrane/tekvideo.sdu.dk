package dk.sdu.tekvideo

/**
 * Supporting tag libs for basic CRUD operations
 *
 * @author Dan Thrane
 */
class CRUDTagLib {

    static namespace = "sducrud"

    def hiddenFields = { attrs, body ->
        out << render(template: "/crud/hiddenFields", model: pageScope.properties)
    }

    def saveButton = { attrs, body ->
        out << render(template: "/crud/saveButton", model: pageScope.properties)
    }

}
