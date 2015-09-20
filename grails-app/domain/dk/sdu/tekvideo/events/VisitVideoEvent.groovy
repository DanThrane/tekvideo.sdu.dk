package dk.sdu.tekvideo.events

class VisitVideoEvent extends Event {
    Long videoId

    @Override
    public String toString() {
        return "VisitVideoEvent{" +
                "id=" + id +
                ", video=" + videoId +
                ", version=" + version +
                ", super=" + super.toString() +
                '}';
    }
}
