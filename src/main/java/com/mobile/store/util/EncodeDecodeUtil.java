package com.mobile.store.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EncodeDecodeUtil {

	private static final String CIPHER_INSTANCE_NAME = "AES/GCM/NoPadding";
	private static final String KEY_STR = "12fmjiojene7972322*%jlkererjeljel830#m";
	private static final String AES = "AES";
	private static final String ENCODE_TYPE = "UTF-8";
	private static final int GCM_NONCE_LENGTH = 12; // in bytes
	private static final int GCM_TAG_LENGTH = 16; // in bytes
	private Key key = null;
	private GCMParameterSpec spec = null;
	private Cipher cipher = null;
	private byte[] tag = new byte[GCM_TAG_LENGTH];
	final Logger logger = LoggerFactory.getLogger(EncodeDecodeUtil.class);

	Key getKey() {
		Key staticKey = null;
		MessageDigest sha = null;
		try {
			byte[] bytes = KEY_STR.getBytes();
			sha = MessageDigest.getInstance("SHA-1");
			bytes = sha.digest(bytes);
			bytes = Arrays.copyOf(bytes, 16);
			staticKey = new SecretKeySpec(bytes, AES);
		} catch (NoSuchAlgorithmException exception) {
		
		}
		return staticKey;
	}

	public String decodeString(String encodedStr) {
		String decodeStr = null;

		try {
			encodedStr = URLDecoder.decode(encodedStr, ENCODE_TYPE);
			Base64 decoder = new Base64();
			byte encrypted[] = decoder.decode(encodedStr.trim());
			cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
			key = getKey();
			spec = generateGcmSpec();
			cipher.init(Cipher.DECRYPT_MODE, key, spec);
			cipher.updateAAD(tag);
			byte[] outputBytes = cipher.doFinal(encrypted);
			String decodedCN = new String(outputBytes);
			decodeStr = decodedCN;
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException ex1) {
			
		} catch (NoSuchPaddingException | BadPaddingException ex2) {
			
		} catch (InvalidKeyException | InvalidAlgorithmParameterException ex3) {
			
		} catch (IllegalBlockSizeException ex4) {
			
		} catch (Exception exception) {
			
		}
		return decodeStr;
	}

	@SuppressWarnings("static-access")
	public String encodeString(String inputStr) {
		String encodedStr = null;
		try {
			cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
			key = getKey();
			spec = generateGcmSpec();
			cipher.init(Cipher.ENCRYPT_MODE, key, spec);
			cipher.updateAAD(tag);
			byte[] outputBytes = cipher.doFinal(inputStr.getBytes());
			encodedStr = Base64.encodeBase64URLSafeString(outputBytes);
			encodedStr = URLEncoder.encode(encodedStr, ENCODE_TYPE);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException ex1) {
	
		} catch (NoSuchPaddingException | BadPaddingException ex2) {
			
		} catch (InvalidKeyException | InvalidAlgorithmParameterException ex3) {
			
		} catch (IllegalBlockSizeException ex4) {
			
		} catch (Exception exception) {
			
		}
		return encodedStr;
	}

	private GCMParameterSpec generateGcmSpec() {
		final byte[] nonce = new byte[GCM_NONCE_LENGTH];
		return new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
	}
}
