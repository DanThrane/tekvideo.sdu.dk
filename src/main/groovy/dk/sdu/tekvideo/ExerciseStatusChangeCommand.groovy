package dk.sdu.tekvideo

import grails.validation.Validateable
import groovy.transform.ToString

@ToString
class ExerciseStatusChangeCommand implements Validateable {
    Exercise id
    String status
}
