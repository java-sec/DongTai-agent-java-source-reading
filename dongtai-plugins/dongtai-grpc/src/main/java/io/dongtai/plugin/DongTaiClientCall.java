package io.dongtai.plugin;

import io.grpc.ClientCall;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;

public class DongTaiClientCall<REQUEST, RESPONSE> extends ForwardingClientCall.SimpleForwardingClientCall<REQUEST, RESPONSE> {
    String serviceName;
    String serviceType;
    String targetService;
    String pluginName;
    String traceKey;
    String traceId;

    protected DongTaiClientCall(ClientCall<REQUEST, RESPONSE> delegate, String serviceName, String serviceType, String targetService, String traceKey, String traceId) {
        super(delegate);
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.targetService = targetService;
        this.pluginName = "GRPC";
        this.traceKey = traceKey;
        this.traceId = traceId;
    }

    @Override
    public void start(Listener<RESPONSE> responseListener, Metadata headers) {
        try {
            Metadata.Key<String> dtTraceId = Metadata.Key.of(traceKey, Metadata.ASCII_STRING_MARSHALLER);
            headers.discardAll(dtTraceId);
            headers.put(dtTraceId, traceId);
        } catch (Exception e) {
            // fixme: solve exception
        }
        super.start(responseListener, headers);
    }
}
