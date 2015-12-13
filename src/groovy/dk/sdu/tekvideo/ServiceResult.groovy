package dk.sdu.tekvideo

/**
 * A standardized way for a service to report back a result.
 *
 * @author Dan Thrane
 */
class ServiceResult<E> {
    // TODO Complete documentation
    E result
    String message
    boolean success
    ServiceError error
    int suggestedHttpStatus

    static jsonMarshaller = { ServiceResult it ->
        def map = [result: it.result, message: it.message, success: it.success]
        if (!it.success) {
            map.error = filter(it.error.information)
        }
        return map
    }

    private ServiceResult() {}

    static def <E> ServiceResult<E> ok() {
        ok(null, "service_result.success")
    }

    static def <E> ServiceResult<E> ok(Map<String, Object> args) {
        E item = args.containsKey("item") ? args.item : null
        String message = args.message ?: "service_result.success"
        ok(item, message)
    }

    static def <E> ServiceResult<E> ok(E item, String message = "service_result.success") {
        ServiceResult<E> result = new ServiceResult<E>()
        result.result = item
        result.message = message
        result.success = true
        result.suggestedHttpStatus = 200
        return result
    }

    static def <E> ServiceResult<E> fail(Map<String, Object> args) {
        String message = args.message
        boolean internal = args.internal != null ? args.internal : false
        Map<String, ?> information = args.information ?: [:]
        int suggestedHttpStatus = args.suggestedHttpStatus ?: -1
        Exception exception = args.exception ?: null
        fail(message, internal, information, suggestedHttpStatus, exception)
    }

    static def <E> ServiceResult<E> fail(String message, boolean internal = false, Map<String, ?> information = [:],
                                         int suggestedHttpStatus = -1, Exception exception = null) {
        int actualHttpStatus = suggestedHttpStatus
        if (suggestedHttpStatus == -1) {
            actualHttpStatus = (internal) ? 500 : 400
        }
        ServiceResult result = new ServiceResult<E>()
        result.message = message
        result.success = false
        result.suggestedHttpStatus = actualHttpStatus

        ServiceError error = new ServiceError()
        result.error = error
        error.internal = internal
        error.information = information
        error.exception = exception
        return result
    }

    private static Map<String, ?> filter(Map<String, ?> map) {
        Map<String, ?> result = [:]
        map.keySet().findAll { !it.startsWith("_") }.each {
            result[it] = map[it]
        }
        return result
    }

    void updateFlashMessage(Map flash) {
        if (success) {
            flash.success = message
        } else {
            flash.error = message
        }
    }

}
