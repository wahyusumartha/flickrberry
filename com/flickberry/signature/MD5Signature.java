package com.flickberry.signature;

import net.rim.device.api.crypto.MD5Digest;

public class MD5Signature {

	private String message;
	private String method;
	private String signature;

	public MD5Signature() {
		message = "";
		method = "MD5-Digest";
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSignature() {
		try {
			byte[] bytes = message.getBytes();
			MD5Digest md5 = new MD5Digest();
			md5.update(bytes, 0, bytes.length);
			int length = md5.getDigestLength();
			byte[] encrypt = new byte[length];
			md5.getDigest(encrypt, 0, true);
			signature = bytesToHex(encrypt);
			// DEBUG
			System.out.println("Message : " + getMessage());
			System.out.println("Method : " + getMethod());
			System.out.println("Signature : " + signature);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return signature;
	}

	public static String bytesToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

}
