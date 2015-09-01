package dk.sdu.tekvideo

class Comment {
    User user
    String contents
    Date dateCreated

    static mapping = {
        contents type: "text"
    }
}
