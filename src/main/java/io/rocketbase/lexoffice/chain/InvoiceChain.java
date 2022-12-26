package io.rocketbase.lexoffice.chain;

import io.rocketbase.lexoffice.RequestContext;
import io.rocketbase.lexoffice.model.Files;
import io.rocketbase.lexoffice.model.Invoice;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
public class InvoiceChain {

    private final RequestContext context;

    public Invoice get(String id) {
        return new Get(context).get(id);
    }

    public Create create() {
        return new Create(context);
    }

    public Files render(String id) {
        return new Render(context).render(id);
    }

    protected static class Get extends ExecutableRequestChain {
        private static final ParameterizedTypeReference<Invoice> TYPE_REFERENCE = new ParameterizedTypeReference<Invoice>() {
        };

        public Get(RequestContext context) {
            super(context, "/invoices");
        }

        @SneakyThrows
        public Invoice get(String id) {
            getUriBuilder().appendPath("/" + id);
            return getContext().execute(getUriBuilder(), HttpMethod.GET, TYPE_REFERENCE);
        }
    }

    public static class Create extends ExecutableRequestChain {
        private static final ParameterizedTypeReference<Invoice> TYPE_REFERENCE = new ParameterizedTypeReference<Invoice>() {
        };

        public Create(RequestContext context) {
            super(context, "/invoices");
        }

        public Create finalize(Boolean finalize) {
            super.getUriBuilder()
                    .addParameter("finalize", finalize);
            return this;
        }

        @SneakyThrows
        public Invoice submit(Invoice invoice) {
            return getContext().execute(getUriBuilder(), HttpMethod.POST, invoice, TYPE_REFERENCE);
        }
    }

    public static class Render extends ExecutableRequestChain {
        private static final ParameterizedTypeReference<Files> TYPE_REFERENCE = new ParameterizedTypeReference<Files>() {
        };

        public Render(RequestContext context) {
            super(context, "/invoices");
        }

        @SneakyThrows
        public Files render(String id) {
            super.getUriBuilder().appendPath("/" + id).appendPath("/document");
            return getContext().execute(getUriBuilder(), HttpMethod.GET, TYPE_REFERENCE);
        }
    }

}
