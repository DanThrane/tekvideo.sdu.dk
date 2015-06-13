package dk.sdu.tekvideo

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Semester {
    Boolean spring
    Integer year

    static constraints = {
        year min: 1900, max: 9999 // Lets assume we don't have anything outside of this range
    }
}
