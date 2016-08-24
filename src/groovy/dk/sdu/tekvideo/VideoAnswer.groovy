package dk.sdu.tekvideo

class VideoAnswer {
    String type
    String value

    static VideoAnswer fromMap(Map<String, Object> map) {
        return new VideoAnswer(
                type: map.type,
                value: map.value
        )
    }

    @Override
    String toString() {
        return "VideoAnswer{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
