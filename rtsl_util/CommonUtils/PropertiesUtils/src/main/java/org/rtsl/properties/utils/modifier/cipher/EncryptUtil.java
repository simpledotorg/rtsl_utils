package org.rtsl.properties.utils.modifier.cipher;

import java.io.UnsupportedEncodingException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public final class EncryptUtil {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static final int CONST_HEX_04 = 4;
    private static final int CONST_HEX_0F = 0x0F;
    private static final int CONST_HEX_10 = 16;
    private static final int CONST_HEX_FF = 0xFF;

    private EncryptUtil() {
    }

    public static String decrypt(Cipher decryptingCipher, String inputData)
            throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] encrypted = EncryptUtil.hexStringToByteArray(inputData);
        byte[] decrypted = decryptingCipher.doFinal(encrypted);
        String decryptedString = new String(decrypted);
        return decryptedString;
    }

    public static String encrypt(Cipher encryptingCipher, String inputData) throws Exception {
        byte[] encrypted = encryptingCipher.doFinal(inputData.getBytes());
        String encryptedString = bytesToHex(encrypted);
        return encryptedString;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), CONST_HEX_10) << CONST_HEX_04)
                    + Character.digit(s.charAt(i + 1), CONST_HEX_10));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & CONST_HEX_FF;
            hexChars[j * 2] = HEX_ARRAY[v >>> CONST_HEX_04];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & CONST_HEX_0F];
        }
        return new String(hexChars);
    }
}
