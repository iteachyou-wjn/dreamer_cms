package cc.iteachyou.cms.utils;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CipherKeyUtils {
	public static byte[] generateCipherKey() {
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance("AES");
		}catch (NoSuchAlgorithmException e) {
			String msg = "Unable to acquire AES algorithm. This is required to function.";
			throw new IllegalStateException(msg);
		}
		kg.init(128);
		SecretKey secretKey = kg.generateKey();
		byte[] cipherKey = secretKey.getEncoded();
		return cipherKey;
	}
}
