package dk.sdu.tekvideo

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeFields = true)
@EqualsAndHashCode
class SimpleCrumb {
    String title
    String url
    Boolean active

    SimpleCrumb(String title, String url, Boolean active = false) {
        this.title = title
        this.url = url
        this.active = active
    }
}
