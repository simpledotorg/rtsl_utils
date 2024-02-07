package org.rtsl.properties.utils.modifier.cipher;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CipherFactory {
    private final String hashPass;
    private final String hashAlgo;
    private final String encryptionAlgo;

    private final Cipher encryptingCipher;
    private final Cipher decryptingCipher;

    public CipherFactory(String hashPass, String hashAlgo, String encryptionAlgo) throws Exception {
        super();
        this.hashPass = hashPass;
        this.hashAlgo = hashAlgo;
        this.encryptionAlgo = encryptionAlgo;
        encryptingCipher = createEncryptingCipher();
        decryptingCipher = createDecryptingCipher();
    }

    private Cipher createEncryptingCipher() throws Exception {
        byte[] encryptionKey = MessageDigest.getInstance(hashAlgo).digest(hashPass.getBytes(StandardCharsets.UTF_8));
        Key key = new SecretKeySpec(encryptionKey, encryptionAlgo);
        Cipher encryptCipher = Cipher.getInstance(encryptionAlgo);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptCipher;
    }

    private Cipher createDecryptingCipher() throws Exception {
        byte[] encryptionKey = MessageDigest.getInstance(hashAlgo).digest(hashPass.getBytes(StandardCharsets.UTF_8));
        Key key = new SecretKeySpec(encryptionKey, encryptionAlgo);
        Cipher decryptCipher = Cipher.getInstance(encryptionAlgo);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        return decryptCipher;
    }

    public Cipher getEncryptingCipher() {
        return encryptingCipher;
    }

    public Cipher getDecryptingCipher() {
        return decryptingCipher;
    }

}
