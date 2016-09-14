package bookyard.server.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PasswordHash {

    val salt : ByteArray = byteArrayOf(39, -54, 127, -75, -7, 71, -81, -52, 116, -124, -90, 97, -88, -87, 120, -26);

    public fun Compute(password : String) : String {

        val spec : KeySpec = PBEKeySpec(password.toCharArray(), salt, 65536, 128);

        val f : SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        val hash : ByteArray = f.generateSecret(spec).getEncoded();

        val enc : Base64.Encoder = Base64.getEncoder();

        System.out.printf("password: %s%n", password);
        System.out.printf("salt: %s%n", enc.encodeToString(salt));
        System.out.printf("hash: %s%n", enc.encodeToString(hash));

        val hashString : String = enc.encodeToString(hash);

        return hashString;
    }
}
