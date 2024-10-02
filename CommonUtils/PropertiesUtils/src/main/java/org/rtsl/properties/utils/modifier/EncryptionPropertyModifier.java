package org.rtsl.properties.utils.modifier;

import org.rtsl.properties.utils.modifier.cipher.CipherFactory;
import org.rtsl.properties.utils.modifier.cipher.EncryptUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import javax.crypto.Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionPropertyModifier implements  Function<Properties, Properties> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionPropertyModifier.class);
    public static final String DEFAULT_ENCRYPTED_PREFIX = "__encrypted__.";
    private final Cipher decryptingCipher;
    private final Cipher encryptingCipher;
    private String encryptionFlag_prefix = DEFAULT_ENCRYPTED_PREFIX;
    private List< String> encryptionFlagExpectedValues = new ArrayList<>(Arrays.asList(
            "1", "true"));

    public EncryptionPropertyModifier(Cipher decryptCipher, Cipher encryptCipher) {
        this.decryptingCipher = decryptCipher;
        this.encryptingCipher = encryptCipher;
    }

    public EncryptionPropertyModifier(Cipher decryptCipher) {
        this(decryptCipher, null);
    }

    public EncryptionPropertyModifier(CipherFactory factory) throws Exception {
        this(factory.getDecryptingCipher(), factory.getEncryptingCipher());
    }

    public void setEncryptionFlag_prefix(String encryptionFlag_prefix) {
        this.encryptionFlag_prefix = encryptionFlag_prefix;
    }

    public void setEncryptionFlagExpectedValues(List<String> encryptionFlagExpectedValues) {
        this.encryptionFlagExpectedValues = encryptionFlagExpectedValues;
    }

    @Override
    public Properties apply(Properties inputProperties) {
        Properties returnProperties = new Properties();
        returnProperties.putAll(inputProperties);
        for (Object currentKey : returnProperties.keySet()) {
            Object value = returnProperties.get(currentKey);
            String isEncrytedString = inputProperties.getProperty(encryptionFlag_prefix + currentKey);
            if (encryptionFlagExpectedValues.contains(isEncrytedString) && value != null && value instanceof String) {
                LOGGER.info("Overriding value of key <{}> with decrypted value", currentKey);
                String stringValue = (String) value;
                returnProperties.put(currentKey, getRawString(stringValue));
            }
        }
        return returnProperties;
    }

    public final String getRawString(String password) {
        String decryptedPassword = password;
        try {
            decryptedPassword = EncryptUtil.decrypt(decryptingCipher, password);
        } catch (Exception ex) {
            LOGGER.warn("Impossible to decrypt password. Trying with raw encrypted Password. Reason is: "
                    + ex.getMessage());
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Impossible to decrypt password. Trying with raw password: ", ex);
            }
            if (LOGGER.isDebugEnabled() && encryptingCipher != null) {
                try {
                    LOGGER.debug("Encrypted string for provided password is <{}>",
                            EncryptUtil.encrypt(encryptingCipher, password));
                } catch (Exception ex1) {
                    LOGGER.warn("Impossible to provide default encrypted password", ex1);
                }
            }
        }
        return decryptedPassword;
    }

}
