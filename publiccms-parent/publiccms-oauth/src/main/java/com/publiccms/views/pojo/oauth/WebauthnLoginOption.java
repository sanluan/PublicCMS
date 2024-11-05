package com.publiccms.views.pojo.oauth;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webauthn4j.data.PublicKeyCredentialDescriptor;
import com.webauthn4j.data.PublicKeyCredentialRequestOptions;
import com.webauthn4j.data.UserVerificationRequirement;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.extension.client.AuthenticationExtensionClientInput;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientInputs;

public class WebauthnLoginOption extends PublicKeyCredentialRequestOptions {

    private String status;
    private String errorMessage = "";

    public WebauthnLoginOption(@JsonProperty("challenge") Challenge challenge, @JsonProperty("timeout") Long timeout,
            @JsonProperty("rpId") String rpId,
            @JsonProperty("allowCredentials") List<PublicKeyCredentialDescriptor> allowCredentials,
            @JsonProperty("userVerification") UserVerificationRequirement userVerification,
            @JsonProperty("extensions") AuthenticationExtensionsClientInputs<AuthenticationExtensionClientInput> extensions) {
        super(challenge, timeout, rpId, allowCredentials, userVerification, null, extensions);
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
