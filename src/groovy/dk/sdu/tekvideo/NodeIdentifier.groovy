package dk.sdu.tekvideo

class NodeIdentifier {
    final Long id
    final String type
    final String name
    final List<Long> subids

    NodeIdentifier(String type, Long id, String name = null, List<Long> subids = Collections.emptyList()) {
        this.type = type
        this.id = id
        this.subids = subids
        this.name = name
    }

    NodeIdentifier child(String type, Long... subids) {
        def subidsList = subids.length > 0 ? Arrays.asList(subids) : []
        return new NodeIdentifier(type, id, name, subidsList + this.subids)
    }

    String getIdentifier() {
        def result = "$type/$id"
        if (subids != null && subids.size() > 0) {
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
                identifier: it.identifier,
                name: it.name ?: it.identifier,
        ]
    }
}
