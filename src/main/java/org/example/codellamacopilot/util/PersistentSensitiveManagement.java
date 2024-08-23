package org.example.codellamacopilot.util;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;

@Deprecated
public class PersistentSensitiveManagement {

    private static PersistentSensitiveManagement instance;

    private PersistentSensitiveManagement() {
    }

    public void storeAPITokenValue(String token){
        CredentialAttributes attributes = createCredentialAttributes("apiToken");
        Credentials credentials = new Credentials("", token);
        PasswordSafe.getInstance().set(attributes, credentials);
    }

    public String getAPITokenValue(){
        CredentialAttributes attributes = createCredentialAttributes("apiToken");
        Credentials credentials = PasswordSafe.getInstance().get(attributes);
        return credentials == null ? null : credentials.getPasswordAsString();
    }

    private CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(
                CredentialAttributesKt.generateServiceName("APITokenStorage", key)
        );
    }

    public static PersistentSensitiveManagement getInstance() {
        if (instance == null) {
            instance = new PersistentSensitiveManagement();
        }
        return instance;
    }

}
