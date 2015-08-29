package dk.sdu.tekvideo.events

import dk.sdu.tekvideo.Video

class VisitVideoEvent extends Event {
    Video video

    static constraints = {
    }

    @Override
    public String toString() {
        return "VisitVideoEvent{" +
                "id=" + id +
                ", video=" + video +
                ", version=" + version +
                ", super=" + super.toString() +
                '}';
    }
}
