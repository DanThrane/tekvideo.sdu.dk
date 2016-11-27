package dk.sdu.tekvideo

import groovy.transform.ToString

@ToString
class ExerciseStatusChangeCommand {
    Exercise id
    String status
}
