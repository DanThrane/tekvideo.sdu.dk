package dk.sdu.tekvideo

class Student {
    User user

    static constraints = {
    }

    static jsonMarshaller = { Student it ->
        [
                id      : it.id,
                username: it.user.username
        ]
    }
}
