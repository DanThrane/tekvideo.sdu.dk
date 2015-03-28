package dk.sdu.tekvideo

import groovy.transform.ToString

@ToString
class Event {
    Long timestamp
    User user

    static constraints = {
    }
}
