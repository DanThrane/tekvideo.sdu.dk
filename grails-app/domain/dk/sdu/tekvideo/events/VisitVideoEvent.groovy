package dk.sdu.tekvideo.events

class VisitVideoEvent extends Event {
    Long video

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
