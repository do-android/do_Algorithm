package doext.implement.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DoSHA1Utils {
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static final int BYTE_LEN = 20;

	public static String encode(String source) throws NoSuchAlgorithmException {
		if (source == null || "".equals(source)) {
			return null;
		}
		return getFormattedText(encodeSHA1(source.getBytes()));
	}
	
	private static String getFormattedText(byte[] bytes) {
		StringBuilder buf = new StringBuilder(BYTE_LEN * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < BYTE_LEN; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
	
	private static byte[] encodeSHA1(byte[] ming) {
		if (ming == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(ming);
			return md.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}