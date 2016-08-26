package dk.sdu.tekvideo

class NodeIdentifier {
    final Long id
    final String type
    final Long[] subids

    NodeIdentifier(String type, Long id, Long... subids) {
        this.type = type
        this.id = id
        this.subids = subids
    }

    NodeIdentifier child(Long... subids) {
        return new NodeIdentifier(type, id, this.subids + subids)
    }

    String getIdentifier() {
        def result = "$type/$id"
        if (subids.length > 0) {
            def joined = subids.join("/")
            result += "/" + joined
        }
        return result
    }

    @Override
    String toString() {
        getIdentifier()
    }

    static jsonMarshaller = { NodeIdentifier it ->
        return [
                id: it.id,
                type: it.type,
                identifier: it.identifier
        ]
    }
}
