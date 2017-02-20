package dk.sdu.tekvideo.events

import dk.sdu.tekvideo.User
import groovy.transform.ToString

@ToString
class Event {
    Long timestamp
    User user
    String uuid

    static constraints = {
        user nullable: true
        uuid nullable: true
    }
}
