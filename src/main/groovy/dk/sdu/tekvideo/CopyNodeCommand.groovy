package dk.sdu.tekvideo

import grails.validation.Validateable

class CopyNodeCommand implements Validateable {
    Long element
    Long destination
}
