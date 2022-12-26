package io.rocketbase.lexoffice.chain;

import io.rocketbase.lexoffice.RequestContext;
import io.rocketbase.lexoffice.model.Files;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
public class FilesChain {

    private final RequestContext context;

    public byte[] download(String id) {
        return new FilesChain.Download(context).get(id);
    }

    protected static class Download extends ExecutableRequestChain {
        private static final ParameterizedTypeReference<byte[]> TYPE_REFERENCE = new ParameterizedTypeReference<byte[]>() {
        };

        public Download(RequestContext context) {
            super(context, "/files");
        }

        @SneakyThrows
        public byte[] get(String id) {
            getUriBuilder().appendPath("/" + id);
            return getContext().execute(getUriBuilder(), HttpMethod.GET, TYPE_REFERENCE);
        }
    }

}