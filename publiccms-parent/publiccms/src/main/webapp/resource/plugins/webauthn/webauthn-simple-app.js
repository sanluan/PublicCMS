/* globals
   WebAuthnHelpers, Msg, ServerResponse,
   CreateOptionsRequest, CreateOptions,
   CredentialAttestation,
   GetOptionsRequest, GetOptions,
   CredentialAssertion,
   WebAuthnOptions
 */

"use strict";

// messages for sending / receiving
// registration:
// 1a. client >>> CreateOptionsRequest >>> server
// 1b. client <<< CreateOptions <<< server
// 2a. client >>> CredentialAttestation >>> server
// 2b. client <<< ServerResponse <<< server
// authentication:
// 1a. client >>> GetOptionsRequest >>> server
// 1b. client <<< GetOptions <<< server
// 2a. client >>> CredentialAssertion >>> server
// 2b. client <<< ServerResponse <<< server
(function() {
    /**
     * Virtual class for messages that serves as the base
     * for all other messages.
     */
    class Msg {
        constructor() {
            /** @type {Array} The list of "official" properties that are managed for this object and sent over the wire. */
            this.propList = [];
        }

        /**
         * Converts the `Msg` to an `Object` containing all the properties in `propList` that have been defined on the `Msg`
         * @return {Object} An `Object` that contains all the properties to be sent over the wire.
         */
        toObject() {
            var obj = {};
            copyPropList(this, obj, this.propList);
            return obj;
        }

        /**
         * Converts the `Msg` to a JSON string containing all the properties in `propList` that have been defined on the `Msg`
         * @return {String} A JSON `String` that contains all the properties to be sent over the wire.
         */
        toString() {
            return JSON.stringify(this.toObject());
        }

        /**
         * Converts the `Msg` to a human-readable string. Useful for debugging messages as they are being sent / received.
         * @return {String} The human-readable message, probably multiple lines.
         */
        toHumanString() {
            var constructMe = Object.getPrototypeOf(this).constructor;
            var retObj = constructMe.from(this);
            retObj.decodeBinaryProperties();
            retObj = retObj.toObject();
            var ret = `[${constructMe.name}] ` + stringifyObj(retObj, 0);
            return ret;
        }

        /**
         * Converts the provided `obj` to this class and then returns a human
         * readable form of the object as interpreted by that class.
         * @param  {Object} obj Any object
         * @return {String}     A human-readable string as interpreteed by this class.
         */
        static toHumanString(obj) {
            var retObj = this.from(obj);
            retObj.decodeBinaryProperties();
            retObj = retObj.toObject();
            var ret = `[${this.name}] ` + stringifyObj(retObj, 0);
            return ret;
        }

        /**
         * Converts the `Msg` to a human-readable string (via {@link toHumanString}) and then replaces whitespace (" " and "\n") with
         * HTML compatible interpetations of whitespace ("&nbsp;" and "<br>").
         * @return {String} The HTML compatible representation of this Msg that should be easy for people to read
         */
        toHumanHtml() {
            return this.toHumanString().replace(/ /g, "&nbsp;").replace(/\n/g, "<br>");
        }

        /**
         * Ensures that all the required properties in the object are defined, and all defined properties are of the correct format.
         * @throws {Error} If any required field is undefined, or any defined field is of the wrong format.
         */
        validate() {
            throw new Error("not implemented");
        }

        /**
         * Any fields that are known to be encoded as `base64url` are decoded to an `ArrayBuffer`
         */
        decodeBinaryProperties() {
            // throw new Error("not implemented");
        }

        /**
         * Any fields that are known to be encoded as an `ArrayBuffer` are encoded as `base64url`
         */
        encodeBinaryProperties() {
            // throw new Error("not implemented");
        }

        /**
         * Creates a new `Msg` object from the specified parameter. Note that the resulting `Msg` is not validated
         * and all fields are their original values (call {@link decodeBinaryProperties} to convert fields to ArrayBuffers)
         * if needed.
         * @param  {String|Object} json The JSON encoded string, or already parsed JSON message in an `Object`
         * @return {Msg}      The newly created message from the Object.
         */
        static from(json) {
            var obj;
            if (typeof json === "string") {
                try {
                    obj = JSON.parse(json);
                } catch (err) {
                    throw new TypeError("error parsing JSON string");
                }
            }

            if (typeof json === "object") {
                obj = json;
            }

            if (typeof obj !== "object") {
                throw new TypeError("could not coerce 'json' argument to an object: '" + json + "'");
            }

            var msg = new this.prototype.constructor();
            copyPropList(obj, msg, msg.propList);

            if (obj.preferences) {
                msg.preferences = WebAuthnOptions.from(obj.preferences);
            }

            return msg;
        }
    }

    /**
     * Generic {@link Msg} from server to indicate success or failure. Used by
     * itself for simple responses, or extended for more complex responses.
     * @extends {Msg}
     */
    class ServerResponse extends Msg {
        constructor() {
            super();

            this.propList = [
                "status",
                "errorMessage",
                "debugInfo"
            ];
        }

        validate() {
            switch (this.status) {
                case "ok":
                    if (this.errorMessage === undefined) {
                        this.errorMessage = "";
                    }

                    // if status is "ok", errorMessage must be ""
                    checkTrue(this.errorMessage === "", "errorMessage must be empty string when status is 'ok'");
                    checkOptionalType(this, "debugInfo", "object");
                    break;

                case "failed":
                    // if status is "failed", errorMessage must be non-zero-length string
                    checkType(this, "errorMessage", "string");
                    checkTrue(
                        this.errorMessage.length > 0,
                        "errorMessage must be non-zero length when status is 'failed'"
                    );
                    checkOptionalType(this, "debugInfo", "object");
                    break;

                // status is string, either "ok" or "failed"
                default:
                    throw new Error("'expected 'status' to be 'string', got: " + this.status);
            }
        }

        decodeBinaryProperties() {
            function decodeAb(obj, key) {
                obj[key] = coerceToArrayBuffer(obj[key], key);
            }

            function decodeOptionalAb(obj, key) {
                if (obj[key] !== undefined) decodeAb(obj, key);
            }

            function objToMap(o) {
                var m = new Map();
                Object.keys(o).forEach((k) => {
                    m.set(k, o[k]);
                });
                return m;
            }

            if (typeof this.debugInfo === "object") {
                decodeAb(this.debugInfo.clientData, "rawId");
                decodeAb(this.debugInfo.authnrData, "rawAuthnrData");
                decodeAb(this.debugInfo.authnrData, "rpIdHash");
                decodeOptionalAb(this.debugInfo.authnrData, "aaguid");
                decodeOptionalAb(this.debugInfo.authnrData, "credId");
                decodeOptionalAb(this.debugInfo.authnrData, "credentialPublicKeyCose");
                decodeOptionalAb(this.debugInfo.authnrData, "sig");
                decodeOptionalAb(this.debugInfo.authnrData, "attCert");

                this.debugInfo.clientData.rawClientDataJson = str2ab(this.debugInfo.clientData.rawClientDataJson);
                this.debugInfo.authnrData.flags = new Set([...this.debugInfo.authnrData.flags]);
                this.debugInfo.audit.warning = objToMap(this.debugInfo.audit.warning);
                this.debugInfo.audit.info = objToMap(this.debugInfo.audit.info);
            }
        }

        encodeBinaryProperties() {
            function encodeAb(obj, key) {
                obj[key] = coerceToBase64Url(obj[key], key);
            }

            function encodeOptionalAb(obj, key) {
                if (obj[key] !== undefined) encodeAb(obj, key);
            }

            if (typeof this.debugInfo === "object") {
                encodeAb(this.debugInfo.clientData, "rawId");
                encodeAb(this.debugInfo.authnrData, "rawAuthnrData");
                encodeAb(this.debugInfo.authnrData, "rpIdHash");
                encodeOptionalAb(this.debugInfo.authnrData, "aaguid");
                encodeOptionalAb(this.debugInfo.authnrData, "credId");
                encodeOptionalAb(this.debugInfo.authnrData, "credentialPublicKeyCose");
                encodeOptionalAb(this.debugInfo.authnrData, "sig");
                encodeOptionalAb(this.debugInfo.authnrData, "attCert");

                this.debugInfo.clientData.rawClientDataJson = ab2str(this.debugInfo.clientData.rawClientDataJson, "clientData.rawClientDataJson");
                this.debugInfo.authnrData.flags = [...this.debugInfo.authnrData.flags];
                this.debugInfo.audit.warning = mapToObj(this.debugInfo.audit.warning);
                this.debugInfo.audit.info = mapToObj(this.debugInfo.audit.info);
            }
        }
    }

    /**
     * A {@link Msg} object that the browser sends to the server to request
     * the options to be used for the WebAuthn `create()` call.
     * @extends {Msg}
     */
    class CreateOptionsRequest extends Msg {
        constructor() {
            super();

            this.propList = [
                "username",
                "displayName",
                "authenticatorSelection",
                "attestation"
            ];
        }

        validate() {
            checkFormat(this, "username", "non-empty-string");
            checkFormat(this, "displayName", "non-empty-string");
            checkAuthenticatorSelection(this);
            checkAttestation(this);
        }

        decodeBinaryProperties() {}

        encodeBinaryProperties() {}
    }

    /**
     * The options to be used for WebAuthn `create()`
     * @extends {ServerResponse}
     */
    class CreateOptions extends ServerResponse {
        constructor() {
            super();

            this.propList = this.propList.concat([
                "rp",
                "user",
                "challenge",
                "pubKeyCredParams",
                "timeout",
                "excludeCredentials",
                "authenticatorSelection",
                "attestation",
                "extensions"
            ]);
        }

        validate() {
            super.validate();

            // check types
            checkType(this, "rp", Object);
            checkFormat(this.rp, "name", "non-empty-string");
            checkOptionalFormat(this.rp, "id", "non-empty-string");
            checkOptionalFormat(this.rp, "icon", "non-empty-string");

            checkType(this, "user", Object);
            checkFormat(this.user, "name", "non-empty-string");
            checkFormat(this.user, "id", "base64url");
            checkFormat(this.user, "displayName", "non-empty-string");
            checkOptionalFormat(this.user, "icon", "non-empty-string");

            checkFormat(this, "challenge", "base64url");
            checkType(this, "pubKeyCredParams", Array);
            this.pubKeyCredParams.forEach((cred) => {
                checkType(cred, "alg", "number");
                checkTrue(cred.type === "public-key", "credential type must be 'public-key'");
            });
            checkOptionalFormat(this, "timeout", "positive-integer");
            checkOptionalType(this, "excludeCredentials", Array);
            if (this.excludeCredentials) checkCredentialDescriptorList(this.excludeCredentials);

            checkAuthenticatorSelection(this);
            checkAttestation(this);

            checkOptionalType(this, "extensions", Object);
        }

        decodeBinaryProperties() {
            if (this.user && this.user.id) {
                this.user.id = coerceToArrayBuffer(this.user.id, "user.id");
            }

            this.challenge = coerceToArrayBuffer(this.challenge, "challenge");

            if (this.excludeCredentials) {
                this.excludeCredentials.forEach((cred, idx) => {
                    cred.id = coerceToArrayBuffer(cred.id, "excludeCredentials[" + idx + "].id");
                });
            }
        }

        encodeBinaryProperties() {
            if (this.user && this.user.id) {
                this.user.id = coerceToBase64Url(this.user.id, "user.id");
            }

            this.challenge = coerceToBase64Url(this.challenge, "challenge");

            if (this.excludeCredentials) {
                this.excludeCredentials.forEach((cred, idx) => {
                    cred.id = coerceToBase64Url(cred.id, "excludeCredentials[" + idx + "].id");
                });
            }
        }
    }

    /**
     * This is the `PublicKeyCredential` that was the result of the `create()` call.
     * @extends {Msg}
     */
    class CredentialAttestation extends Msg {
        constructor() {
            super();

            this.propList = [
                "rawId",
                "id",
                "response",
                "getClientExtensionResults"
            ];
        }

        static from(obj) {
            obj = super.from(obj);

            // original response object is probably read-only
            if (typeof obj.response === "object") {
                var origResponse = obj.response;

                obj.response = {
                    clientDataJSON: origResponse.clientDataJSON,
                    attestationObject: origResponse.attestationObject,
                };
            }

            return obj;
        }

        validate() {
            checkFormat(this, "rawId", "base64url");
            checkOptionalFormat(this, "id", "base64url");
            checkType(this, "response", Object);
            checkFormat(this.response, "attestationObject", "base64url");
            checkFormat(this.response, "clientDataJSON", "base64url");
            checkOptionalType(this, "getClientExtensionResults", Object);
        }

        decodeBinaryProperties() {
            this.rawId = coerceToArrayBuffer(this.rawId, "rawId");
            if (this.id) this.id = coerceToArrayBuffer(this.id, "id");
            this.response.attestationObject = coerceToArrayBuffer(this.response.attestationObject, "response.attestationObject");
            this.response.clientDataJSON = coerceToArrayBuffer(this.response.clientDataJSON, "response.clientDataJSON");
        }

        encodeBinaryProperties() {
            this.rawId = coerceToBase64Url(this.rawId, "rawId");
            if (this.id) this.id = coerceToBase64Url(this.id, "id");
            this.response.attestationObject = coerceToBase64Url(this.response.attestationObject, "response.attestationObject");
            this.response.clientDataJSON = coerceToBase64Url(this.response.clientDataJSON, "response.clientDataJSON");
        }
    }

    function stringifyObj(obj, depth) {
        var str = "";

        // opening bracket
        str += "{\n";
        depth++;

        // print all properties
        for (let key of Object.keys(obj)) {
            // add key
            str += indent(depth) + key + ": ";
            // add value
            str += stringifyType(obj, key, depth) + ",\n";
        }

        // closing bracket
        depth--;
        str += indent(depth) + "}";

        return str;
    }

    function stringifyArr(arr, depth) {
        var str = "";

        // opening brace
        str += "[\n";
        depth++;

        // print all properties
        for (let i = 0; i < arr.length; i++) {
            // add value
            str += indent(depth) + stringifyType(arr, i, depth) + ",\n";
        }

        // closing brace
        depth--;
        str += indent(depth) + "]";

        return str;
    }

    function stringifyType(obj, key, depth) {
        // handle native types
        switch (typeof obj[key]) {
            case "object": break;
            case "undefined": return "undefined";
            // case "string": return "\"" + obj[key].replace(/\n/g, "\\n\"\n" + indent(depth + 1) + "\"") + "\"";
            case "string": return "\"" + obj[key].replace(/\n/g, "\n" + indent(depth + 1)) + "\"";
            case "number": return obj[key].toString();
            case "boolean": return obj[key].toString();
            case "symbol": return obj[key].toString();
            default:
                throw new TypeError("unknown type in stringifyType: " + typeof obj[key]);
        }

        // handle objects
        switch (true) {
            case obj[key] instanceof ArrayBuffer:
                return abToHumanStr(obj[key], (depth + 1));
            case obj[key] instanceof Array:
                return stringifyArr(obj[key], depth);
            case obj[key] instanceof Set:
                return stringifyArr([...obj[key]], depth);
            case obj[key] instanceof Map:
                return stringifyObj(mapToObj(obj[key]), depth);
            default:
                return stringifyObj(obj[key], depth);
        }
    }

    function indent(depth) {
        var ret = "";

        for (let i = 0; i < depth * 4; i++) {
            ret += " ";
        }

        return ret;
    }

    // printHex
    function abToHumanStr(buf, depth) {
        var ret = "";

        // if the buffer was a TypedArray (e.g. Uint8Array), grab its buffer and use that
        if (ArrayBuffer.isView(buf) && buf.buffer instanceof ArrayBuffer) {
            buf = buf.buffer;
        }

        // check the arguments
        if ((typeof depth != "number") ||
        (typeof buf != "object")) {
            throw new TypeError("Bad args to abToHumanStr");
        }
        if (!(buf instanceof ArrayBuffer)) {
            throw new TypeError("Attempted abToHumanStr with non-ArrayBuffer:", buf);
        }
        // print the buffer as a 16 byte long hex string
        var arr = new Uint8Array(buf);
        var len = buf.byteLength;
        var i, str = "";
        ret += `[ArrayBuffer] (${buf.byteLength} bytes)\n`;
        for (i = 0; i < len; i++) {
            var hexch = arr[i].toString(16);
            hexch = (hexch.length == 1) ? ("0" + hexch) : hexch;
            str += hexch.toUpperCase() + " ";
            if (i && !((i + 1) % 16)) {
                ret += indent(depth) + str.replace(/.$/, "\n");
                str = "";
            }
        }
        // print the remaining bytes
        if ((i) % 16) {
            ret += indent(depth) + str.replace(/.$/, "\n");
        }

        // remove final newline
        ret = ret.replace(/\n$/, "");

        return ret;
    }
    /**
     * A {@link Msg} object that the browser sends to the server to request
     * the options to be used for the WebAuthn `get()` call.
     * @extends {Msg}
     */
    class GetOptionsRequest extends Msg {
        constructor() {
            super();

            this.propList = [
                "username",
                "displayName"
            ];
        }

        validate() {
            checkFormat(this, "username", "non-empty-string");
            checkFormat(this, "displayName", "non-empty-string");
        }

        decodeBinaryProperties() {}

        encodeBinaryProperties() {}
    }

    /**
     * The options to be used for WebAuthn `get()`
     * @extends {ServerResponse}
     */
    class GetOptions extends ServerResponse {
        constructor() {
            super();

            this.propList = this.propList.concat([
                "challenge",
                "timeout",
                "rpId",
                "allowCredentials",
                "userVerification",
                "extensions"
            ]);
        }

        validate() {
            super.validate();
            checkFormat(this, "challenge", "base64url");
            checkOptionalFormat(this, "timeout", "positive-integer");
            checkOptionalFormat(this, "rpId", "non-empty-string");
            checkOptionalType(this, "allowCredentials", Array);
            if (this.allowCredentials) checkCredentialDescriptorList(this.allowCredentials);
            if (this.userVerification) checkUserVerification(this.userVerification);
            checkOptionalType(this, "extensions", Object);
        }

        decodeBinaryProperties() {
            this.challenge = coerceToArrayBuffer(this.challenge, "challenge");
            if (this.allowCredentials) {
                this.allowCredentials.forEach((cred) => {
                    cred.id = coerceToArrayBuffer(cred.id, "cred.id");
                });
            }
        }

        encodeBinaryProperties() {
            this.challenge = coerceToBase64Url(this.challenge, "challenge");
            if (this.allowCredentials) {
                this.allowCredentials.forEach((cred, idx) => {
                    cred.id = coerceToBase64Url(cred.id, "allowCredentials[" + idx + "].id");
                });
            }
        }
    }

    /**
     * This is the `PublicKeyCredential` that was the result of the `get()` call.
     * @extends {Msg}
     */
    class CredentialAssertion extends Msg {
        constructor() {
            super();

            this.propList = [
                "rawId",
                "id",
                "response",
                "getClientExtensionResults"
            ];
        }

        static from(obj) {
            obj = super.from(obj);

            // original response object is probably read-only
            if (typeof obj.response === "object") {
                var origResponse = obj.response;

                obj.response = {
                    clientDataJSON: origResponse.clientDataJSON,
                    authenticatorData: origResponse.authenticatorData,
                    signature: origResponse.signature,
                    userHandle: origResponse.userHandle,
                };
            }

            return obj;
        }

        validate() {
            checkFormat(this, "rawId", "base64url");
            checkOptionalFormat(this, "id", "base64url");
            checkType(this, "response", Object);
            checkFormat(this.response, "authenticatorData", "base64url");
            checkFormat(this.response, "clientDataJSON", "base64url");
            checkFormat(this.response, "signature", "base64url");
            checkOptionalFormat(this.response, "userHandle", "nullable-base64");
            checkOptionalType(this, "getClientExtensionResults", Object);
        }

        decodeBinaryProperties() {
            this.rawId = coerceToArrayBuffer(this.rawId, "rawId");
            if (this.id) this.id = coerceToArrayBuffer(this.id, "id");
            this.response.clientDataJSON = coerceToArrayBuffer(this.response.clientDataJSON, "response.clientDataJSON");
            this.response.signature = coerceToArrayBuffer(this.response.signature, "response.signature");
            this.response.authenticatorData = coerceToArrayBuffer(this.response.authenticatorData, "response.authenticatorData");
            if (this.response.userHandle) {
                this.response.userHandle = coerceToArrayBuffer(this.response.userHandle, "response.authenticatorData");
            }
            if (this.response.userHandle === null || this.response.userHandle === "") {
                this.response.userHandle = new ArrayBuffer();
            }
        }

        encodeBinaryProperties() {
            this.rawId = coerceToBase64Url(this.rawId, "rawId");
            if (this.id) this.id = coerceToBase64Url(this.id, "id");
            this.response.clientDataJSON = coerceToBase64Url(this.response.clientDataJSON, "response.clientDataJSON");
            this.response.signature = coerceToBase64Url(this.response.signature, "response.signature");
            this.response.authenticatorData = coerceToBase64Url(this.response.authenticatorData, "response.authenticatorData");
            if (this.response.userHandle) {
                if (this.response.userHandle.byteLength > 0) this.response.userHandle = coerceToBase64Url(this.response.userHandle, "response.authenticatorData");
                else this.response.userHandle = null;
            }
        }
    }

    class WebAuthnOptions extends Msg {
        constructor() {
            super();

            this.propList = [
                "timeout"
            ];
        }

        merge(dst, preferDst) {
            var i;
            for (i = 0; i < this.propList.length; i++) {
                var prop = this.propList[i];
                // copy property if it isn't set
                if (this[prop] === undefined) this[prop] = dst[prop];
                // if the destination is set and we prefer that, copy it over
                if (preferDst && dst[prop] !== undefined) this[prop] = dst[prop];
            }
        }
    }

    // these get defined different depending on whether we're running in a browser or node.js
    var exp, coerceToBase64Url, coerceToArrayBuffer;

    // running in node.js
    if (typeof module === "object" && module.exports) {
        exp = module.exports;
        coerceToBase64Url = function(thing, name) {
            name = name || "''";

            // Array to Uint8Array
            if (Array.isArray(thing)) {
                thing = Uint8Array.from(thing);
            }

            // Uint8Array, etc. to ArrayBuffer
            if (typeof thing === "object" &&
                thing.buffer instanceof ArrayBuffer &&
                !(thing instanceof Buffer)) {
                thing = thing.buffer;
            }

            // ArrayBuffer to Buffer
            if (thing instanceof ArrayBuffer && !(thing instanceof Buffer)) {
                thing = new Buffer(thing);
            }

            // Buffer to base64 string
            if (thing instanceof Buffer) {
                thing = thing.toString("base64");
            }

            if (typeof thing !== "string") {
                throw new Error(`could not coerce '${name}' to string`);
            }

            // base64 to base64url
            // NOTE: "=" at the end of challenge is optional, strip it off here so that it's compatible with client
            thing = thing.replace(/\+/g, "-").replace(/\//g, "_").replace(/=*$/g, "");

            return thing;
        };

        coerceToArrayBuffer = function(buf, name) {
            name = name || "''";

            if (typeof buf === "string") {
                // base64url to base64
                buf = buf.replace(/-/g, "+").replace(/_/g, "/");
                // base64 to Buffer
                buf = Buffer.from(buf, "base64");
            }

            // Buffer or Array to Uint8Array
            if (buf instanceof Buffer || Array.isArray(buf)) {
                buf = new Uint8Array(buf);
            }

            // Uint8Array to ArrayBuffer
            if (buf instanceof Uint8Array) {
                buf = buf.buffer;
            }

            // error if none of the above worked
            if (!(buf instanceof ArrayBuffer)) {
                throw new TypeError(`could not coerce '${name}' to ArrayBuffer`);
            }

            return buf;
        };
    }

    // running in browser
    if (isBrowser()) {
        exp = window;
        coerceToBase64Url = function(thing, name) {
            // Array or ArrayBuffer to Uint8Array
            if (Array.isArray(thing)) {
                thing = Uint8Array.from(thing);
            }

            if (thing instanceof ArrayBuffer) {
                thing = new Uint8Array(thing);
            }

            // Uint8Array to base64
            if (thing instanceof Uint8Array) {
                var str = "";
                var len = thing.byteLength;

                for (var i = 0; i < len; i++) {
                    str += String.fromCharCode(thing[i]);
                }
                thing = window.btoa(str);
            }

            if (typeof thing !== "string") {
                throw new Error("could not coerce '" + name + "' to string");
            }

            // base64 to base64url
            // NOTE: "=" at the end of challenge is optional, strip it off here
            thing = thing.replace(/\+/g, "-").replace(/\//g, "_").replace(/=*$/g, "");

            return thing;
        };

        coerceToArrayBuffer = function(thing, name) {
            if (typeof thing === "string") {
                // base64url to base64
                thing = thing.replace(/-/g, "+").replace(/_/g, "/");

                // base64 to Uint8Array
                var str = window.atob(thing);
                var bytes = new Uint8Array(str.length);
                for (var i = 0; i < str.length; i++) {
                    bytes[i] = str.charCodeAt(i);
                }
                thing = bytes;
            }

            // Array to Uint8Array
            if (Array.isArray(thing)) {
                thing = new Uint8Array(thing);
            }

            // Uint8Array to ArrayBuffer
            if (thing instanceof Uint8Array) {
                thing = thing.buffer;
            }

            // error if none of the above worked
            if (!(thing instanceof ArrayBuffer)) {
                throw new TypeError("could not coerce '" + name + "' to ArrayBuffer");
            }

            return thing;
        };
    }

    function ab2str(buf) {
        return String.fromCharCode.apply(null, new Uint8Array(buf));
    }

    function str2ab(str) {
        var buf = new ArrayBuffer(str.length);
        var bufView = new Uint8Array(buf);
        for (var i = 0, strLen = str.length; i < strLen; i++) {
            bufView[i] = str.charCodeAt(i);
        }
        return buf;
    }

    function copyProp(src, dst, prop) {
        if (src[prop] !== undefined) dst[prop] = src[prop];
    }

    function copyPropList(src, dst, propList) {
        var i;
        for (i = 0; i < propList.length; i++) {
            copyProp(src, dst, propList[i]);
        }
    }

    function checkType(obj, prop, type) {
        switch (typeof type) {
            case "string":
                if (typeof obj[prop] !== type) {
                    throw new Error("expected '" + prop + "' to be '" + type + "', got: " + typeof obj[prop]);
                }
                break;

            case "function":
                if (!(obj[prop] instanceof type)) {
                    throw new Error("expected '" + prop + "' to be '" + type.name + "', got: " + obj[prop]);
                }
                break;

            default:
                throw new Error("internal error: checkType received invalid type");
        }
    }

    function checkOptionalType(obj, prop, type) {
        if (obj === undefined || obj[prop] === undefined) return;

        checkType(obj, prop, type);
    }

    function checkFormat(obj, prop, format) {
        switch (format) {
            case "non-empty-string":
                checkType(obj, prop, "string");
                checkTrue(
                    obj[prop].length > 0,
                    "expected '" + prop + "' to be non-empty string"
                );
                break;
            case "base64url":
                checkType(obj, prop, "string");
                checkTrue(
                    isBase64Url(obj[prop]),
                    "expected '" + prop + "' to be base64url format, got: " + obj[prop]
                );
                break;
            case "positive-integer":
                checkType(obj, prop, "number");
                var n = obj[prop];
                checkTrue(
                    n >>> 0 === parseFloat(n),
                    "expected '" + prop + "' to be positive integer"
                );
                break;
            case "nullable-base64":
                var t = typeof obj[prop];
                if (obj[prop] === null) t = "null";
                checkTrue(
                    ["null", "string", "undefined"].includes(t),
                    "expected '" + prop + "' to be null or string"
                );
                if (!obj[prop]) return;
                checkTrue(
                    isBase64Url(obj[prop]),
                    "expected '" + prop + "' to be base64url format, got: " + obj[prop]
                );
                break;
            default:
                throw new Error("internal error: unknown format");
        }
    }

    function checkOptionalFormat(obj, prop, format) {
        if (obj === undefined || obj[prop] === undefined) return;

        checkFormat(obj, prop, format);
    }

    function isBase64Url(str) {
        return !!str.match(/^[A-Za-z0-9\-_]+={0,2}$/);
    }

    function checkTrue(truthy, msg) {
        if (!truthy) {
            throw Error(msg);
        }
    }

    function checkUserVerification(val) {
        checkTrue(
            ["required", "preferred", "discouraged"].includes(val),
            "userVerification must be 'required', 'preferred' or 'discouraged'"
        );
    }

    function checkAuthenticatorSelection(obj) {
        checkOptionalType(obj, "authenticatorSelection", Object);
        if (obj.authenticatorSelection && obj.authenticatorSelection.authenticatorAttachment) {
            checkTrue(
                ["platform", "cross-platform"].includes(obj.authenticatorSelection.authenticatorAttachment),
                "authenticatorAttachment must be either 'platform' or 'cross-platform'"
            );
        }
        if (obj.authenticatorSelection && obj.authenticatorSelection.userVerification) {
            checkUserVerification(obj.authenticatorSelection.userVerification);

        }
        checkOptionalType(obj.authenticatorSelection, "requireResidentKey", "boolean");
    }

    function checkCredentialDescriptorList(arr) {
        arr.forEach((cred) => {
            checkFormat(cred, "id", "base64url");
            checkTrue(cred.type === "public-key", "credential type must be 'public-key'");
            checkOptionalType(cred, "transports", Array);
            if (cred.transports) cred.transports.forEach((trans) => {
                checkTrue(
                    ["usb", "nfc", "ble"].includes(trans),
                    "expected transport to be 'usb', 'nfc', or 'ble', got: " + trans
                );
            });
        });
    }

    function checkAttestation(obj) {
        if (obj.attestation) checkTrue(
            ["direct", "none", "indirect"].includes(obj.attestation),
            "expected attestation to be 'direct', 'none', or 'indirect'"
        );
    }

    function mapToObj(mapObj) {
        var m = {};
        mapObj.forEach((v, k) => {
            m[k] = v;
        });
        return m;
    }

    function isBrowser() {
        try {
            if (!window) return false;
        } catch (err) {
            return false;
        }
        return true;
    }

    /**
     * The main class for registering and logging in via WebAuthn. This class wraps all server communication,
     * as well as calls to `credentials.navigator.create()` (registration) and `credentials.navigator.get()` (login)
     *
     * @param {Object} config The configuration object for WebAuthnApp
     */
    class WebAuthnApp {
        constructor(config) {
            // check for browser; throw error and fail if not browser
            if (!WebAuthnHelpers.utils.isBrowser()) throw new Error("WebAuthnApp must be run from a browser");

            // check for secure context
            if (!window.isSecureContext) {
                fireNotSupported("This web page was not loaded in a secure context (https). Please try loading the page again using https or make sure you are using a browser with secure context support.");
                return null;
            }

            // check for WebAuthn CR features
            if (window.PublicKeyCredential === undefined ||
            typeof window.PublicKeyCredential !== "function" ||
            typeof window.PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable !== "function") {
                console.log("PublicKeyCredential not found");
                fireNotSupported("WebAuthn is not currently supported by this browser. See this webpage for a list of supported browsers: <a href=https://developer.mozilla.org/en-US/docs/Web/API/Web_Authentication_API#Browser_compatibility>Web Authentication: Browser Compatibility</a>");
                return null;
            }

            // Useful constants for working with COSE key objects
            const coseAlgECDSAWithSHA256 = -7;

            // configure or defaults
            config = config || {};
            this.registerChallengeEndpoint = config.registerChallengeEndpoint || WebAuthnHelpers.defaultRoutes.attestationOptions;
            this.registerResponseEndpoint = config.registerResponseEndpoint || WebAuthnHelpers.defaultRoutes.attestationResult;
            this.loginChallengeEndpoint = config.loginChallengeEndpoint || WebAuthnHelpers.defaultRoutes.assertionOptions;
            this.loginResponseEndpoint = config.loginResponseEndpoint || WebAuthnHelpers.defaultRoutes.assertionResult;
            this.registerChallengeMethod = config.registerChallengeMethod || "POST";
            this.registerResponseMethod = config.registerResponseMethod || "POST";
            this.loginChallengeMethod = config.loginChallengeMethod || "POST";
            this.loginResponseMethod = config.loginResponseMethod || "POST";
            this.timeout = config.timeout || 60000; // one minute
            this.alg = config.alg || coseAlgECDSAWithSHA256;
            this.binaryEncoding = config.binaryEncoding;
            // TODO: relying party name
            this.appName = config.appName || window.location.hostname;
            this.username = config.username;
        }

        /**
         * Perform WebAuthn registration, including getting options from the server
         * calling `navigator.credentials.create()`, sending the result to the server,
         * and validating the end result. Note that this is a convenience wrapper around
         * {@link requestRegisterOptions}, {@link create}, and {@link sendRegisterResult}.
         * Each of those classes fires events for various state changes or errors that
         * can be captured for more advanced applications.
         *
         * @return {Promise.<ServerResponse|Error>} Returns a promise that resolves to
         * a {@link ServerResponse} on success, or rejects with an `Error` on failure.
         */
        register() {
            fireRegister("start");
            // get challenge
            return this.requestRegisterOptions()
                .then((serverMsg) => this.create(serverMsg))
                .then((newCred) => this.sendRegisterResult(newCred))
                .then((msg) => {
                    fireRegister("success");
                    return msg;
                })
                .catch((err) => {
                    fireRegister("error", err);
                    return Promise.reject(err);
                });
        }

        /**
         * Perform WebAuthn authentication, including getting options from the server
         * calling `navigator.credentials.get()`, sending the result to the server,
         * and validating the end result. Note that this is a convenience wrapper around
         * {@link requestLoginOptions}, {@link get}, and {@link sendLoginResult}.
         * Each of those classes fires events for various state changes or errors that
         * can be captured for more advanced applications.
         *
         * @return {Promise.<ServerResponse|Error>} Returns a promise that resolves to
         * a {@link ServerResponse} on success, or rejects with an `Error` on failure.
         */
        login() {
            fireLogin("start");
            var self = this;
            // get challenge
            return this.requestLoginOptions()
                .then((serverMsg) => self.get(serverMsg))
                .then((assn) => self.sendLoginResult(assn))
                .then((msg) => {
                    fireLogin("success");
                    return msg;
                })
                .catch((err) => {
                    fireLogin("error", err);
                    return Promise.reject(err);
                });
        }

        /**
         * A wrapper around a call to `navigator.credentials.create()`,
         * which is WebAuthn's way of registering a new device with a service.
         *
         * @param  {CreateOptions} options The desired options for the `navigator.credentials.create()`
         * call. May be the return value from {@link requestRegisterOptions} or a modified version thereof.
         * Note that this object contains a `challenge` property which MUST come from the server and that
         * the server will use to make sure that the credential isn't part of a replay attack.
         * @return {Promise.<PublicKeyCredentialAttestation|Error>}         Returns a Promise that resolves to a
         * {@link PublicKeyCredentialAttestation} on success (i.e. - the actual return value from `navigator.credentials.create()`),
         * or rejects with an Error on failure.
         * @fires WebAuthnApp#userPresenceEvent
         */
        create(options) {
            if (!(options instanceof CreateOptions)) {
                throw new Error("expected 'options' to be instance of CreateOptions");
            }
            options.decodeBinaryProperties();

            var args = {
                publicKey: options.toObject()
            };
            args.publicKey.attestation = args.publicKey.attestation || "direct";
            delete args.publicKey.status;
            delete args.publicKey.errorMessage;

            fireDebug("create-options", args);
            fireUserPresence("start");

            return navigator.credentials.create(args)
                .then((res) => {
                    // save client extensions
                    if (typeof res.getClientExtensionResults === "function") {
                        let exts = res.getClientExtensionResults();
                        if (typeof exts === "object") res.getClientExtensionResults = exts;
                    }

                    fireUserPresence("done");
                    fireDebug("create-result", res);
                    return res;
                })
                .catch((err) => {
                    fireUserPresence("done");
                    fireDebug("create-error", err);
                    return Promise.reject(err);
                });
        }

        /**
         * A wrapper around a call to `navigator.credentials.get()`,
         * which is WebAuthn's way of authenticating a user to a service.
         *
         * @param  {GetOptions} options The desired options for the `navigator.credentials.get()`
         * call. May be the return value from {@link requestLoginOptions} or a modified version thereof.
         * Note that this object contains a `challenge` property which MUST come from the server and that
         * the server will use to make sure that the credential isn't part of a replay attack.
         * @return {Promise.<PublicKeyCredentialAssertion|Error>}         Returns a Promise that resolves to a
         * {@link PublicKeyCredentialAssertion} on success (i.e. - the actual return value from `navigator.credentials.get()`),
         * or rejects with an Error on failure.
         * @fires WebAuthnApp#userPresenceEvent
         */
        get(options) {
            if (!(options instanceof GetOptions)) {
                throw new Error("expected 'options' to be instance of GetOptions");
            }
            options.decodeBinaryProperties();

            var args = {
                publicKey: options.toObject()
            };
            delete args.publicKey.status;
            delete args.publicKey.errorMessage;

            fireDebug("get-options", args);
            fireUserPresence("start");

            return navigator.credentials.get(args)
                .then((res) => {
                    // save client extensions
                    if (typeof res.getClientExtensionResults === "function") {
                        let exts = res.getClientExtensionResults();
                        if (typeof exts === "object") res.getClientExtensionResults = exts;
                    }

                    fireUserPresence("done");
                    fireDebug("get-result", res);
                    return res;
                })
                .catch((err) => {
                    fireUserPresence("done");
                    fireDebug("get-error", err);
                    return Promise.reject(err);
                });
        }

        /**
         * Requests the registration options to be used from the server, including the random
         * challenge to be used for this registration request.
         *
         * @return {CreateOptions} The options to be used for creating the new
         * credential to be registered with the server. The options returned will
         * have been validated.
         */
        requestRegisterOptions() {
            var sendData = CreateOptionsRequest.from({
                username: this.username,
                displayName: this.displayName || this.username
            });

            return this.send(
                this.registerChallengeMethod,
                this.registerChallengeEndpoint,
                sendData,
                CreateOptions
            );
        }

        /**
         * Sends the {@link WebAuthn#AuthenticatorAttestationResponse}
         * to the server.
         *
         * @param  {WebAuthn#AuthenticatorAttestationResponse} pkCred The public key credential (containing an attesation) returned from `navigator.credentials.get()`
         * @return {Promise.<ServerResponse|Error>} Resolves to the {@link ServerResponse} from the server on success, or rejects with Error on failure
         */
        sendRegisterResult(pkCred) {
            if (!(pkCred instanceof window.PublicKeyCredential)) {
                throw new Error("expected 'pkCred' to be instance of PublicKeyCredential");
            }

            var sendData = CredentialAttestation.from({
                username: this.username,
                rawId: pkCred.rawId,
                id: pkCred.rawId,
                response: {
                    attestationObject: pkCred.response.attestationObject,
                    clientDataJSON: pkCred.response.clientDataJSON
                }
            });

            return this.send(
                this.registerResponseMethod,
                this.registerResponseEndpoint,
                sendData,
                ServerResponse
            );
        }

        /**
         * Requests the login options to be used from the server, including the random
         * challenge to be used for this registration request.
         *
         * @return {GetOptions} The options to be used for creating the new
         * credential to be registered with the server. The options returned will
         * have been validated.
         */
        requestLoginOptions() {
            var sendData = GetOptionsRequest.from({
                username: this.username,
                displayName: this.displayname || this.username
            });

            return this.send(
                this.loginChallengeMethod,
                this.loginChallengeEndpoint,
                sendData,
                GetOptions
            );
        }

        /**
         * This class refers to the dictionaries and interfaces defined in the
         * {@link https://www.w3.org/TR/webauthn/ WebAuthn specification} that are
         * used by the {@link WebAuthnApp} class. They are included here for reference.
         *
         * @class WebAuthn
         */

        /**
         * A {@link https://www.w3.org/TR/webauthn/#iface-pkcredential PublicKeyCredential}
         * that has been created by an authenticator, where the `response` field contains a
         * {@link https://www.w3.org/TR/webauthn/#authenticatorattestationresponse AuthenticatorAttesationResponse}.
         *
         * @typedef {Object} WebAuthn#AuthenticatorAttesationResponse
         */

        /**
         * A {@link https://www.w3.org/TR/webauthn/#iface-pkcredential PublicKeyCredential}
         * that has been created by an authenticator, where the `response` field contains a
         * {@link https://www.w3.org/TR/webauthn/#authenticatorassertionresponse AuthenticatorAssertionResponse}.
         *
         * @typedef {Object} WebAuthn#AuthenticatorAssertionResponse
         */

        /**
         * Sends the {@link WebAuthn#AuthenticatorAssertionResponse}
         * to the server.
         *
         * @param  {WebAuthn#AuthenticatorAssertionResponse} assn The assertion returned from `navigator.credentials.get()`
         * @return {Promise.<ServerResponse|Error>} Resolves to the {@link ServerResponse} from the server on success, or rejects with Error on failure
         */
        sendLoginResult(assn) {
            if (!(assn instanceof window.PublicKeyCredential)) {
                throw new Error("expected 'assn' to be instance of PublicKeyCredential");
            }

            var msg = CredentialAssertion.from(assn);

            return this.send(
                this.loginResponseMethod,
                this.loginResponseEndpoint,
                msg,
                ServerResponse
            );
        }

        /**
         * The lowest-level message sending. Transmits a response over the wire.
         *
         * @param  {String} method              "POST", currently throws if non-POST, but this may be changed in the future.
         * @param  {String} url                 The REST path to send the data to
         * @param  {Msg} data                The data to be sent, in the form of a {@link Msg} object. This method will convert binary fields to their transmittable form and will validate the data being sent.
         * @param  {Function} responseConstructor The constructor of the data to be received, which must inherit from {@link ServerResponse}. The data returned from this function will be of this type, as created by {@link Msg.from} and will be validated by {@link Msg.validate}.
         * @return {Promise.<Msg|Error>}                     Returns a Promise that resolves to a {@link Msg} of the type specified by the `responseConstructor` parameter, or rejects with an Error on failure.
         * @fires WebAuthnApp#debugEvent
         */
        send(method, url, data, responseConstructor) {
        // check args
            if (method !== "POST") {
                return Promise.reject(new Error("why not POST your data?"));
            }

            if (typeof url !== "string") {
                return Promise.reject(new Error("expected 'url' to be 'string', got: " + typeof url));
            }

            if (!(data instanceof Msg)) {
                return Promise.reject(new Error("expected 'data' to be instance of 'Msg'"));
            }

            if (typeof responseConstructor !== "function") {
                return Promise.reject(new Error("expected 'responseConstructor' to be 'function', got: " + typeof responseConstructor));
            }

            // convert binary properties (if any) to strings
            data.encodeBinaryProperties();

            // validate the data we're sending
            try {
                data.validate();
            } catch (err) {
            // console.log("validation error", err);
                return Promise.reject(err);
            }

            // TODO: maybe some day upgrade to fetch(); have to change the mock in the tests too
            return new Promise(function(resolve, reject) {
                var xhr = new XMLHttpRequest();
                function rejectWithFailed(errorMessage) {
                    fireDebug("send-error", new Error(errorMessage));
                    return reject(new Error(errorMessage));
                }

                xhr.open(method, url, true);
                xhr.setRequestHeader("Content-type", "application/json; charset=utf-8");
                xhr.onload = function() {
                    fireDebug("response-raw", {
                        status: xhr.status,
                        body: xhr.responseText
                    });

                    if (xhr.readyState !== 4) {
                        return rejectWithFailed("server returned ready state: " + xhr.readyState);
                    }

                    var response;
                    try {
                        response = JSON.parse(xhr.responseText);
                    } catch (err) {
                        if (xhr.status === 200) {
                            return rejectWithFailed("error parsing JSON response: '" + xhr.responseText + "'");
                        }
                        return rejectWithFailed("server returned status: " + xhr.status);
                    }

                    if (Array.isArray(response)) {
                        response = response[0];
                    }

                    var msg = responseConstructor.from(response);

                    if (msg.status === "failed") {
                        return rejectWithFailed(msg.errorMessage);
                    }


                    try {
                        msg.validate();
                    } catch (err) {
                        return rejectWithFailed(err.message);
                    }

                    fireDebug("response", {
                        status: xhr.status,
                        body: msg
                    });
                    return resolve(msg);
                };
                xhr.onerror = function() {
                    return rejectWithFailed("POST to URL failed: " + url);
                };
                fireDebug("send", data);

                data = data.toString();
                fireDebug("send-raw", data);
                xhr.send(data);
            });
        }
    }

    function fireEvent(type, data) {
        // console.log("firing event", type);
        var e = new CustomEvent(type, { detail: data || null });
        document.dispatchEvent(e);
    }

    /**
     * Event fired to signal that WebAuthn is not supported in the current context.
     *
     * @event WebAuthnApp#notSupportedEvent
     *
     * @property {String} type "webauthn-not-supported"
     * @property {String} detail A human-readable reason for why WebAuthn is currently not supported.
     */
    function fireNotSupported(reason) {
        fireEvent("webauthn-not-supported", reason);
        // fireDebug("not-supported", reason);
    }

    /**
     * Debug event, for tracking the internal status of login() and register()
     *
     * @event WebAuthnApp#debugEvent
     * @type {CustomEvent}
     * @property {String} type "webauthn-debug"
     * @property {Object} detail The details of the event
     * @property {String} detail.subtype The sub-type of the "webauth-debug" event.
     * Options include: "create-options", "create-result", "create-error", "get-options",
     * "get-result", "get-error", "send-error", "send-raw", "send", "response-raw", "response"
     * @property {Any} detail.data The data of the event. Varies based on the `subtype` of the event.
     */
    function fireDebug(subtype, data) {
        fireEvent("webauthn-debug", {
            subtype: subtype,
            data: data
        });
    }

    /**
     * Event that signals state changes for "User Presence" or "User Verification" testing.
     * User Presence involves a user simply touching a device (or perhaps a button) to signal
     * that the user is present and approves of a registration or log in action. On traditional
     * Security Key devices, such as USB Security Keys, this may be signaled to the user by a
     * flashing LED light on the device. User Verification is similar to User Presence, but
     * involves a user performing biometric authentication (fingerprint, face, etc.) or entering
     * a PIN. This event can be caught and a message can be displayed to the user reminding them
     * to perform the approperiate action to continue the registration or log in process.
     *
     * @event WebAuthnApp#userPresenceEvent
     * @type {CustomEvent}
     * @property {String} type "webauthn-user-presence-start" when the User Presence or User Verification is beginning and waiting for the user.
     * @property {String} type "webauthn-user-presence-done" when the User Presence or User Verification has completed (successfully or unsuccessfully)
     * @property {null} detail (there are no details for this event)
     */
    function fireUserPresence(state) {
        switch (state) {
            case "start":
                return fireEvent("webauthn-user-presence-start");
            case "done":
                return fireEvent("webauthn-user-presence-done");
            default:
                throw new Error("unknown 'state' in fireUserPresence");
        }
    }

    /**
     * Event that signals the state changes for registration.
     *
     * @event WebAuthnApp#registerEvent
     * @type {CustomEvent}
     * @property {String} type "webauthn-register-start"
     * @property {String} type "webauthn-register-done"
     * @property {String} type "webauthn-register-error"
     * @property {String} type "webauthn-register-success"
     * @property {null|Error} detail There are no details for these events, except "webauthn-register-error"
     * which will have the Error in detail.
     */
    function fireRegister(state, data) {
        switch (state) {
            case "start":
                return fireEvent("webauthn-register-start");
            case "done":
                return fireEvent("webauthn-register-done");
            case "error":
                fireEvent("webauthn-register-error", data);
                return fireEvent("webauthn-register-done");
            case "success":
                fireEvent("webauthn-register-success", data);
                return fireEvent("webauthn-register-done");
            default:
                throw new Error("unknown 'state' in fireRegister");
        }
    }

    /**
     * Event that signals the state changes for log in.
     *
     * @event WebAuthnApp#loginEvent
     * @type {CustomEvent}
     * @property {String} type "webauthn-login-start"
     * @property {String} type "webauthn-login-done"
     * @property {String} type "webauthn-login-error"
     * @property {String} type "webauthn-login-success"
     * @property {null|Error} detail There are no details for these events, except "webauthn-login-error"
     * which will have the Error in detail.
     */
    function fireLogin(state, data) {
        switch (state) {
            case "start":
                return fireEvent("webauthn-login-start");
            case "done":
                return fireEvent("webauthn-login-done");
            case "error":
                fireEvent("webauthn-login-error", data);
                return fireEvent("webauthn-login-done");
            case "success":
                fireEvent("webauthn-login-success", data);
                return fireEvent("webauthn-login-done");
            default:
                throw new Error("unknown 'state' in fireLogin");
        }
    }

    // exports, regardless of whether we're in browser or node.js
    // note that browser is using global namespace (i.e. - "window");
    exp.WebAuthnHelpers = {
        defaultRoutes: {
            attestationOptions: "/attestation/options",
            attestationResult: "/attestation/result",
            assertionOptions: "/assertion/options",
            assertionResult: "/assertion/result"
        },
        utils: {
            coerceToBase64Url,
            coerceToArrayBuffer,
            isBrowser
        }
    };
    exp.Msg = Msg;
    exp.ServerResponse = ServerResponse;
    exp.CreateOptionsRequest = CreateOptionsRequest;
    exp.CreateOptions = CreateOptions;
    exp.CredentialAttestation = CredentialAttestation;
    exp.GetOptionsRequest = GetOptionsRequest;
    exp.GetOptions = GetOptions;
    exp.CredentialAssertion = CredentialAssertion;
    exp.WebAuthnOptions = WebAuthnOptions;
    exp.WebAuthnApp = WebAuthnApp;
}());
