package practice.bookyard.server.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHash {
	
	private static final byte[] salt = new byte[] { 
			39, -54, 127, -75, -7, 71, -81, -52, 116, -124, -90, 97, -88, -87, 120, -26	
	};
	
	public static String Compute(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		
		byte[] hash = f.generateSecret(spec).getEncoded();
		
		Base64.Encoder enc = Base64.getEncoder();
		
		System.out.printf("password: %s%n", password);
		System.out.printf("salt: %s%n", enc.encodeToString(salt));
		System.out.printf("hash: %s%n", enc.encodeToString(hash));
		
		String hashString = enc.encodeToString(hash);
		
		return hashString;
	}
}
