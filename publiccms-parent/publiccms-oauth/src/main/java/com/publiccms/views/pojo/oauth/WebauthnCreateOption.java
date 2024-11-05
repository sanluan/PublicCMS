package com.publiccms.views.pojo.oauth;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webauthn4j.data.AttestationConveyancePreference;
import com.webauthn4j.data.AuthenticatorSelectionCriteria;
import com.webauthn4j.data.PublicKeyCredentialCreationOptions;
import com.webauthn4j.data.PublicKeyCredentialDescriptor;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialRpEntity;
import com.webauthn4j.data.PublicKeyCredentialUserEntity;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientInputs;
import com.webauthn4j.data.extension.client.RegistrationExtensionClientInput;

public class WebauthnCreateOption extends PublicKeyCredentialCreationOptions {

    private String status;
    private String errorMessage = "";

    public WebauthnCreateOption(@JsonProperty("rp") PublicKeyCredentialRpEntity rp,
            @JsonProperty("user") PublicKeyCredentialUserEntity user, @JsonProperty("challenge") Challenge challenge,
            @JsonProperty("pubKeyCredParams") List<PublicKeyCredentialParameters> pubKeyCredParams,
            @JsonProperty("timeout") Long timeout,
            @JsonProperty("excludeCredentials") List<PublicKeyCredentialDescriptor> excludeCredentials,
            @JsonProperty("authenticatorSelection") AuthenticatorSelectionCriteria authenticatorSelection,
            @JsonProperty("attestation") AttestationConveyancePreference attestation,
            @JsonProperty("extensions") AuthenticationExtensionsClientInputs<RegistrationExtensionClientInput> extensions) {
        super(rp, user, challenge, pubKeyCredParams, timeout, excludeCredentials, authenticatorSelection, null, attestation,
                extensions);
        this.status = "ok";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
