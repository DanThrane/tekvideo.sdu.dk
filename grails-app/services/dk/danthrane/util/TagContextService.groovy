package dk.danthrane.util

/**
 * @author Dan Thrane
 */
class TagContextService {
    RequestStoreService requestStoreService

    class Context {
        int count = 0
        List<Map> attributes = []
    }

    private void enterContext(String contextName, Map attributes) {
        Map contexts = requestStoreService.contexts
        if (!contexts) {
            contexts = requestStoreService.contexts = [:]
        }

        Context context = contexts[contextName]
        if (context == null) {
            context = contexts[contextName] = new Context()
        }
        context.count++
        context.attributes += attributes
    }

    private void leaveContext(String contextName) {
        assert requestStoreService.contexts != null
        Context context = requestStoreService.contexts[contextName]

        context.count--
        context.attributes.pop()
    }

    Context getContext(String contextName) {
        Map contexts = requestStoreService.contexts
        if (contexts != null) {
            return contexts[contextName]
        }
        return null
    }

    Map getContextAttributes(String contextName) {
        Context context = getContext(contextName)
        return (context != null && context.attributes.size() > 0) ? context.attributes.last() : null
    }

    boolean isInContext(String contextName) {
        return getContext(contextName)?.count > 0
    }

    void contextWithAttributes(String ctx, Map attributes, Closure closure) {
        enterContext(ctx, attributes)
        closure()
        leaveContext(ctx)
    }

    void context(String ctx, Closure closure) {
        contextWithAttributes(ctx, [:], closure)
    }

}
