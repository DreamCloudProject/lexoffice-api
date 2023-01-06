package io.rocketbase.lexoffice.chain;

import io.rocketbase.lexoffice.RequestContext;
import io.rocketbase.lexoffice.model.EventSubscription;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
public class EventSubscriptionChain {

    private final RequestContext context;

    public EventSubscription get(String id) {
        return new Get(context).get(id);
    }

    public Create create() {
        return new Create(context);
    }

    public Delete delete() {
        return new Delete(context);
    }

    protected static class Get extends ExecutableRequestChain {
        private static final ParameterizedTypeReference<EventSubscription> TYPE_REFERENCE = new ParameterizedTypeReference<EventSubscription>() {
        };

        public Get(RequestContext context) {
            super(context, "/event-subscriptions");
        }

        @SneakyThrows
        public EventSubscription get(String id) {
            getUriBuilder().appendPath("/" + id);
            return getContext().execute(getUriBuilder(), HttpMethod.GET, TYPE_REFERENCE);
        }
    }

    public static class Create extends ExecutableRequestChain {
        private static final ParameterizedTypeReference<EventSubscription> TYPE_REFERENCE = new ParameterizedTypeReference<EventSubscription>() {
        };

        public Create(RequestContext context) {
            super(context, "/event-subscriptions");
        }

        @SneakyThrows
        public EventSubscription submit(EventSubscription eventSubscription) {
            return getContext().execute(getUriBuilder(), HttpMethod.POST, eventSubscription, TYPE_REFERENCE);
        }
    }

    public static class Delete extends ExecutableRequestChain {
        private static final ParameterizedTypeReference<EventSubscription> TYPE_REFERENCE = new ParameterizedTypeReference<EventSubscription>() {
        };

        public Delete(RequestContext context) {
            super(context, "/event-subscriptions");
        }

        @SneakyThrows
        public EventSubscription submit(EventSubscription eventSubscription) {
            return getContext().execute(getUriBuilder(), HttpMethod.DELETE, eventSubscription, TYPE_REFERENCE);
        }
    }

}
