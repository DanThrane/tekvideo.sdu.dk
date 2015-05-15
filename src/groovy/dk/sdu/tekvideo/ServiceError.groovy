package dk.sdu.tekvideo

class ServiceError {
    /**
     * Possible exception that caused this behavior
     */
    Exception exception
    /**
     * Additional information describing why the error occurred. Keys starting with "_" are considered private, and
     * are not included in JSON output.
     */
    Map<String, ?> information
    /**
     * Indicates if this error was a result of an internal error or a user error
     */
    boolean internal
}
