package doext.implement.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DoDESUtil {

	// 向量  
	private final static String iv = "01234567";
	// 加解密统一使用的编码方式  
	private final static String encoding = "utf-8";

	/**
	 * 3DES加密
	 * 
	 * @param data
	 *            普通文本
	 * @return
	 * @throws Exception
	 */
	public String EncriptData(String data) throws Exception {
		DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		Key deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());

		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(data.getBytes(encoding));
		return DoBase64Utils.encode(encryptData);
	}

	/**
	 * 3DES解密
	 * 
	 * @param data
	 *            加密文本
	 * @return
	 * @throws Exception
	 */
	public String DecriptData(String data) throws Exception {
		DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		Key deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] decryptData = cipher.doFinal(DoBase64Utils.decode(data));
		return new String(decryptData, encoding);
	}

//	private static String ALGORITHM = "DESede";
//	private static final char[] HEX_DIGITS={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	private String key;

	public DoDESUtil(String _key) {
		this.key = _key;
	}
//
//	/**
//	 * 加密
//	 */
//	public String EncriptData(String data) throws Exception {
//		byte[] keybyte = hex2Byte(key);
//		SecretKey key = new SecretKeySpec(keybyte,ALGORITHM);
//		Cipher cipher = Cipher.getInstance(ALGORITHM);
//		cipher.init(Cipher.ENCRYPT_MODE, key);
//		byte[] bOut = cipher.doFinal(data.getBytes());
//		return getFormattedText(bOut);
//	}
//
//	/**
//	 * 解密
//	 */
//	public String DecriptData(String data) throws Exception {
//		byte[] src =hex2Byte(data);
//		byte[] keybyte = hex2Byte(key);
//		SecretKey deskey = new SecretKeySpec(keybyte,ALGORITHM);
//		Cipher cipher = Cipher.getInstance(ALGORITHM);
//		cipher.init(Cipher.DECRYPT_MODE, deskey);
//		byte[] bOut = cipher.doFinal(src);
//		return new String(bOut);
//
//	}
//	
//	private String getFormattedText(byte[] bytes){
//		int len = bytes.length;
//		StringBuilder buf = new StringBuilder(len*2);
//		//把密文转换成十六进制的字符串形式		
//		for(int j=0;j<len;j++){
//			buf.append(HEX_DIGITS[(bytes[j]>>4)&0x0f]);
//			buf.append(HEX_DIGITS[bytes[j]&0x0f]);
//		}
//		return buf.toString();
//	}
//	
//	private byte[] hex2Byte(String str){
//		byte[] bytes = new byte[str.length()/2];
//		int len = bytes.length;
//		for(int i=0;i<len;i++){
//			bytes[i]=(byte)Integer.parseInt(str.substring(2*i, 2*i+2),16);
//		}
//		return bytes;
//	}
}
