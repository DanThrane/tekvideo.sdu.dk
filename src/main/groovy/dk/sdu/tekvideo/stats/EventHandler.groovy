package dk.sdu.tekvideo.stats

import dk.sdu.tekvideo.Event
import dk.sdu.tekvideo.ServiceResult

interface EventHandler {
    boolean canHandle(String eventKind)
    ServiceResult<Void> handle(Event event)
}
