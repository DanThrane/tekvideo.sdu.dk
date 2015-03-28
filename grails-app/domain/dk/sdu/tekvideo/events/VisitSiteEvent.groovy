package dk.sdu.tekvideo.events

import groovy.transform.ToString

@ToString
class VisitSiteEvent extends Event {
    String url
    String ua

    static constraints = {
        url blank: false, maxSize: 512
        ua blank: false, maxSize: 512
    }
}
