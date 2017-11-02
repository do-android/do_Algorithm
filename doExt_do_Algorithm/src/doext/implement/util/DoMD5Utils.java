package doext.implement.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DoMD5Utils {
	private static MessageDigest sMd5MessageDigest;
	private static StringBuilder sStringBuilder;

	static {
		try {
			sMd5MessageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		sStringBuilder = new StringBuilder();
	}

	private DoMD5Utils() {
	}

	public static String md5(String s) {

		sMd5MessageDigest.reset();
		sMd5MessageDigest.update(s.getBytes());

		byte digest[] = sMd5MessageDigest.digest();

		sStringBuilder.setLength(0);
		for (int i = 0; i < digest.length; i++) {
			final int b = digest[i] & 255;
			if (b < 16) {
				sStringBuilder.append('0');
			}
			sStringBuilder.append(Integer.toHexString(b));
		}

		return sStringBuilder.toString();
	}

	public static String md5(byte[] b) {

		sMd5MessageDigest.reset();
		sMd5MessageDigest.update(b);

		byte digest[] = sMd5MessageDigest.digest();

		sStringBuilder.setLength(0);
		for (int i = 0; i < digest.length; i++) {
			final int len = digest[i] & 255;
			if (len < 16) {
				sStringBuilder.append('0');
			}
			sStringBuilder.append(Integer.toHexString(len));
		}

		return sStringBuilder.toString();
	}

}
