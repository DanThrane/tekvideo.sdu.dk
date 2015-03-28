package dk.sdu.tekvideo.events

import groovy.transform.ToString

@ToString
class ExitSiteEvent extends Event {
    String url
    String ua
    Long time

    static constraints = {
        url blank: false, maxSize: 512
        ua blank: false, maxSize: 512
        time min: 0L
    }
}
