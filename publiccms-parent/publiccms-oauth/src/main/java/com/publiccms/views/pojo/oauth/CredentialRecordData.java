package com.publiccms.views.pojo.oauth;

import static com.webauthn4j.data.attestation.authenticator.AuthenticatorData.BIT_BE;
import static com.webauthn4j.data.attestation.authenticator.AuthenticatorData.BIT_BS;
import static com.webauthn4j.data.attestation.authenticator.AuthenticatorData.BIT_UV;

import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.webauthn4j.converter.AttestedCredentialDataConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.AuthenticatorTransport;
import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.data.attestation.statement.AttestationStatement;
import com.webauthn4j.data.client.CollectedClientData;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientOutputs;
import com.webauthn4j.data.extension.client.RegistrationExtensionClientOutput;

public class CredentialRecordData {
    public CredentialRecordData() {
        super();
    }

    public CredentialRecordData(AttestationObject attestationObject, CollectedClientData collectedClientData,
            AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput> clientExtensions,
            Set<AuthenticatorTransport> transports, ObjectConverter objectConverter) {
        AttestedCredentialDataConverter attestedCredentialDataConverter = new AttestedCredentialDataConverter(objectConverter);
        this.attestedCredentialData = attestedCredentialDataConverter
                .convert(attestationObject.getAuthenticatorData().getAttestedCredentialData());
        this.attestationStatement = objectConverter.getJsonConverter()
                .writeValueAsString(attestationObject.getAttestationStatement());
        this.counter = attestationObject.getAuthenticatorData().getSignCount();
        this.authenticatorExtensions = objectConverter.getJsonConverter()
                .writeValueAsString(attestationObject.getAuthenticatorData().getExtensions());
        this.clientData = collectedClientData;
        this.clientExtensions = objectConverter.getJsonConverter().writeValueAsString(clientExtensions);
        this.transports = objectConverter.getJsonConverter().writeValueAsString(transports);
        this.uvInitialized = (attestationObject.getAuthenticatorData().getFlags() & BIT_UV) != 0;
        this.backupEligible = (attestationObject.getAuthenticatorData().getFlags() & BIT_BE) != 0;
        this.backupState = (attestationObject.getAuthenticatorData().getFlags() & BIT_BS) != 0;
    }

    public CredentialRecord toRecord(ObjectConverter objectConverter) {
        AttestedCredentialDataConverter attestedCredentialDataConverter = new AttestedCredentialDataConverter(objectConverter);
        return new CredentialRecordImpl(
                objectConverter.getJsonConverter().readValue(attestationStatement, AttestationStatementBox.class)
                        .getAttestationStatement(),
                uvInitialized, backupEligible, backupState, counter,
                attestedCredentialDataConverter.convert(attestedCredentialData),
                objectConverter.getJsonConverter().readValue(authenticatorExtensions, new TypeReference<>() {
                }), clientData, objectConverter.getJsonConverter().readValue(clientExtensions, new TypeReference<>() {
                }), objectConverter.getJsonConverter().readValue(transports, new TypeReference<>() {
                }));
    }

    private CollectedClientData clientData;
    private byte[] attestedCredentialData;
    private String attestationStatement;
    private long counter;
    private String authenticatorExtensions;
    private Boolean uvInitialized;
    private Boolean backupEligible;
    private Boolean backupState;
    private String clientExtensions;
    private String transports;

    public CollectedClientData getClientData() {
        return clientData;
    }

    public void setClientData(CollectedClientData clientData) {
        this.clientData = clientData;
    }

    public byte[] getAttestedCredentialData() {
        return attestedCredentialData;
    }

    public void setAttestedCredentialData(byte[] attestedCredentialData) {
        this.attestedCredentialData = attestedCredentialData;
    }

    public String getAttestationStatement() {
        return attestationStatement;
    }

    public void setAttestationStatement(String attestationStatement) {
        this.attestationStatement = attestationStatement;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public String getAuthenticatorExtensions() {
        return authenticatorExtensions;
    }

    public void setAuthenticatorExtensions(String authenticatorExtensions) {
        this.authenticatorExtensions = authenticatorExtensions;
    }

    public Boolean getUvInitialized() {
        return uvInitialized;
    }

    public void setUvInitialized(Boolean uvInitialized) {
        this.uvInitialized = uvInitialized;
    }

    public Boolean getBackupEligible() {
        return backupEligible;
    }

    public void setBackupEligible(Boolean backupEligible) {
        this.backupEligible = backupEligible;
    }

    public Boolean getBackupState() {
        return backupState;
    }

    public void setBackupState(Boolean backupState) {
        this.backupState = backupState;
    }

    public String getClientExtensions() {
        return clientExtensions;
    }

    public void setClientExtensions(String clientExtensions) {
        this.clientExtensions = clientExtensions;
    }

    public String getTransports() {
        return transports;
    }

    public void setTransports(String transports) {
        this.transports = transports;
    }
}

class AttestationStatementBox {
    public AttestationStatementBox() {
    }

    public AttestationStatementBox(AttestationStatement attestationStatement) {
        this.attestationStatement = attestationStatement;
    }

    private AttestationStatement attestationStatement;

    public AttestationStatement getAttestationStatement() {
        return attestationStatement;
    }
}
