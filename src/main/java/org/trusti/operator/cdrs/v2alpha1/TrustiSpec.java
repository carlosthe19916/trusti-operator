package org.trusti.operator.cdrs.v2alpha1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.fabric8.kubernetes.api.model.LocalObjectReference;
import io.fabric8.kubernetes.api.model.SecretKeySelector;

import java.util.List;

public record TrustiSpec(
        @JsonPropertyDescription("Secret(s) that might be used when pulling an image from a private container image registry or repository.")
        List<LocalObjectReference> imagePullSecrets,

        @JsonProperty("db")
        @JsonPropertyDescription("In this section you can find all properties related to connect to a database.")
        DatabaseSpec databaseSpec,

        @JsonProperty("hostname")
        @JsonPropertyDescription("In this section you can configure hostname and related properties.")
        HostnameSpec hostnameSpec,

        @JsonProperty("http")
        @JsonPropertyDescription("In this section you can configure Keycloak features related to HTTP and HTTPS")
        HttpSpec httpSpec,

        @JsonProperty("apiResourceLimits")
        @JsonPropertyDescription("In this section you can configure resource limits settings for the Web Console.")
        ResourcesLimitSpec apiResourceLimitSpec
) {

    public TrustiSpec() {
        this(
                null,
                null,
                null,
                null,
                null
        );
    }

    public record DatabaseSpec(
            @JsonPropertyDescription("Size of the PVC to create.")
            String size,

            @JsonProperty("resourceLimits")
            @JsonPropertyDescription("In this section you can configure resource limits settings.")
            ResourcesLimitSpec resourceLimitSpec
    ) {
    }

    public record HostnameSpec(
            @JsonPropertyDescription("Hostname for the server.")
            String hostname
    ) {
    }

    public record HttpSpec(
            @JsonPropertyDescription("A secret containing the TLS configuration for HTTPS. Reference: https://kubernetes.io/docs/concepts/configuration/secret/#tls-secrets.")
            String tlsSecret
    ) {
    }

    public record ResourcesLimitSpec(
            @JsonPropertyDescription("Requested CPU.")
            String cpuRequest,

            @JsonPropertyDescription("Limit CPU.")
            String cpuLimit,

            @JsonPropertyDescription("Requested memory.")
            String memoryRequest,

            @JsonPropertyDescription("Limit Memory.")
            String memoryLimit
    ) {
    }

}