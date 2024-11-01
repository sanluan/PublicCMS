package com.publiccms.controller.admin.oauth;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.entities.sys.SysSite;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.data.AttestationConveyancePreference;
import com.webauthn4j.data.AuthenticatorAttachment;
import com.webauthn4j.data.AuthenticatorSelectionCriteria;
import com.webauthn4j.data.PublicKeyCredentialCreationOptions;
import com.webauthn4j.data.PublicKeyCredentialDescriptor;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialRpEntity;
import com.webauthn4j.data.PublicKeyCredentialType;
import com.webauthn4j.data.PublicKeyCredentialUserEntity;
import com.webauthn4j.data.UserVerificationRequirement;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("webauthn")
public class WebAuthnController {
    private WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
    private static List<PublicKeyCredentialParameters> pubKeyCredParams = new ArrayList<>();
    static {
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES384));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES512));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS256));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS384));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS512));
    }

    @GetMapping("/register/challenge")
    public PublicKeyCredentialCreationOptions challenge(@RequestAttribute SysSite site, String username) {
        Challenge challenge = new DefaultChallenge(username.getBytes());
        PublicKeyCredentialRpEntity rp = new PublicKeyCredentialRpEntity("server", site.getDynamicPath());
        PublicKeyCredentialUserEntity user = new PublicKeyCredentialUserEntity(username.getBytes(), username, username);
        UserVerificationRequirement userVerificationRequirement = UserVerificationRequirement.PREFERRED;
        List<PublicKeyCredentialDescriptor> excludeCredentials = Collections.emptyList();
        AuthenticatorSelectionCriteria authenticatorSelectionCriteria = new AuthenticatorSelectionCriteria(
                AuthenticatorAttachment.PLATFORM, true, userVerificationRequirement);
        return new PublicKeyCredentialCreationOptions(rp, user, challenge,
                pubKeyCredParams, (long) 6000, excludeCredentials, authenticatorSelectionCriteria,
                AttestationConveyancePreference.NONE, null);
    }
}