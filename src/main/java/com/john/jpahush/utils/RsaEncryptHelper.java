package com.john.jpahush.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 암호화 헬퍼 클래스
 */
@SuppressWarnings("unused")
@Slf4j
@Component
public class RsaEncryptHelper {

	/**
	 * RSA공개키 사이즈
	 */
	private static final int KEYSIZE = 4096;

	/**
	 * 암호화 방식
	 */
	private static final String METHOD = "RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING";

	/**
	 * 공개키
	 */
	private PublicKey publicKey = null;

	/**
	 * 비밀키
	 */
	private PrivateKey privateKey = null;

	/**
	 * 생성자
	 */
	public RsaEncryptHelper() {
	}

	/**
	 * 공개키 비밀키를 지정해서 생성한다.
	 * @param publicKeyText 공개키 문자열
	 * @param privateKeyText 비밀키 문자열
	 */
	public RsaEncryptHelper(String publicKeyText, String privateKeyText) {
		setPublicKey(publicKeyText);
		setPrivateKey(privateKeyText);
	}

	/**
	 * 파일로 부터 키를 로드한다.
	 */
	public boolean loadKeysFromFile() {

		return loadKeysFromFile("./public.key", "./private.key");
	}

	/**
	 * 파일로 부터 키를 로드한다.
	 * @param publicKeyPath 공개키 경로
	 * @param privateKeyPath 비밀키 경로
	 */
	public boolean loadKeysFromFile(String publicKeyPath, String privateKeyPath) {
		boolean result = false;

		// 파일로 부터 키문자열을 읽어들인다.
		String publicKeyText = loadStringFromFile(publicKeyPath);
		String privateKeyText = loadStringFromFile(privateKeyPath);

		// 키 문자열이 모두 유효한 경우, 키로 설정
		if(StringUtils.hasText(publicKeyText) && StringUtils.hasText(privateKeyText)) {
			setPublicKey(publicKeyText);
			setPrivateKey(privateKeyText);

			result = true;
		}

		return result;
	}

	/**
	 * 파일로 부터 문자열을 로드한다.
	 * @param path 파일 경로
	 * @return 파일 내 문자열
	 */
	private String loadStringFromFile(String path) {
		StringBuilder result = new StringBuilder();

		try {
			// 파일이 유효한 경우
			if(StringUtils.hasText(path)) {

				//파일 객체 생성
				File file = new File(path);

				// 파일이 존재하는 경우
				if(file.exists()) {
					//입력 스트림 생성
					FileReader fileReader = new FileReader(file);

					//입력 버퍼 생성
					BufferedReader bufReader = new BufferedReader(fileReader);
					String line;
					while((line = bufReader.readLine()) != null){
						result.append(line);
					}
					bufReader.close();
				}
			}
		}
		catch (Exception ex) {
			result.setLength(0);
		}

		return result.toString();
	}

	/**
	 * 비밀키를 설정한다.
	 * @param privateKeyText 비밀키 문자열
	 * @return 성공 여부
	 */
	@SuppressWarnings("UnusedReturnValue")
	public boolean setPrivateKey(String privateKeyText) {
		this.privateKey = restorePrivateKey(privateKeyText);
		return this.privateKey != null;
	}

	/**
	 * 개인키를 복원한다.
	 * @param privateKeyText 비밀키 문자열
	 * @return 비밀키 객체
	 */
	protected static PrivateKey restorePrivateKey(String privateKeyText) {
		try {
			byte[] privateKeyBytes = DatatypeConverter.parseBase64Binary(privateKeyText);

			KeyFactory factory = KeyFactory.getInstance("RSA");
			EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

			return factory.generatePrivate(privateKeySpec);
		}
		catch ( Exception e) {
			return null;
		}
	}

	/**
	 * 공개키를 설정한다.
	 * @param publicKeyText 공개키 문자열
	 * @return 성공여부
	 */
	@SuppressWarnings("UnusedReturnValue")
	public boolean setPublicKey(String publicKeyText) {
		this.publicKey = restorePublicKey(publicKeyText);
		return this.publicKey != null;
	}

	/**
	 * 공개키를 복원한다.
	 * @param publicKeyText 공개키 문자열
	 * @return 공개키 객체
	 */
	protected static PublicKey restorePublicKey(String publicKeyText) {
		try {
			byte[] publicKeyBytes = DatatypeConverter.parseBase64Binary(publicKeyText);

			KeyFactory factory = KeyFactory.getInstance("RSA");
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

			return factory.generatePublic(publicKeySpec );
		}
		catch ( Exception e) {
			return null;
		}
	}

	/**
	 * 지정된 공개키를 이용하여 암호화하고 Base64로 인코딩을 한다.
	 * @param plainText 암호화할 문자열
	 * @return 암호화하여 인코딩된 문자열
	 * @throws Exception 예외 객체
	 */
	public String encrypt (String plainText) throws Exception
	{
		if( this.publicKey == null)
			throw new NullPointerException("키가 로드되지 않았습니다.");

		return encrypt(plainText, this.publicKey);
	}

	/**
	 * 지정된 공개키를 이용하여 암호화하고 Base64로 인코딩을 한다.
	 * @param plainText 암호화할 문자열
	 * @param publicKeyText 공개키 문자열
	 * @return 암호화하여 인코딩된 문자열
	 * @throws Exception 예외 객체
	 */
	public static String encrypt(String plainText, String publicKeyText) throws Exception {
		PublicKey publicKey = restorePublicKey(publicKeyText);
		if( publicKey == null)
			throw new NullPointerException("공개키가 잘못되었습니다.");
		return encrypt(plainText, publicKey);
	}

	/**
	 * 지정된 공개키를 이용하여 암호화하고 Base64로 인코딩을 한다.
	 * @param plainText 암호화할 문자열
	 * @param publicKey 공개키 객체
	 * @return 암호화하여 인코딩된 문자열
	 * @throws Exception 예외 객체
	 */
	protected static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance(METHOD);

		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)) ;

		return DatatypeConverter.printBase64Binary(cipherText);
	}


	/**
	 * 지정된 비밀키를 이용하여 암호화된 문자열을 복호화 한다.
	 * @param encodedText 암화화하여 인코딩된 문자열
	 * @return 복호화된 문자열
	 * @throws Exception 예외 객체
	 */
	public String decrypt (String encodedText) throws Exception
	{
		if( privateKey == null)
			throw new NullPointerException("키가 로드되지 않았습니다.");
		return decrypt(encodedText, privateKey);
	}

	/**
	 * 지정된 비밀키를 이용하여 암호화된 문자열을 복호화 한다.
	 * @param encodedText 암화화하여 인코딩된 문자열
	 * @param privateKeyText 비밀키 문자열
	 * @return 복호화된 문자열
	 * @throws Exception 예외 객체
	 */
	public static String decrypt(String encodedText, String privateKeyText) throws Exception {
		PrivateKey privateKey = restorePrivateKey(privateKeyText);
		if( privateKey == null)
			throw new NullPointerException("비밀키가 잘못되었습니다.");
		return decrypt(encodedText, privateKey);
	}

	/**
	 * 지정된 비밀키를 이용하여 암호화된 문자열을 복호화 한다.
	 * @param encodedText 암화화하여 인코딩된 문자열
	 * @param privateKey 비밀키 객체
	 * @return 복호화된 문자열
	 * @throws Exception 예외 객체
	 */
	public static String decrypt(String encodedText, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance(METHOD);

		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte [] cipherTextArray = DatatypeConverter.parseBase64Binary(encodedText);

		byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);

		return new String(decryptedTextArray);
	}

	/**
	 * 비밀키를 이용하여 인증하고, 인증서를 문자열로 리턴한다.
	 * @param plainText 인증할 문자열
	 * @return 인증서 문자열
	 * @throws Exception 예외 객체
	 */
	public String sign(String plainText) throws Exception {
		if( privateKey == null)
			throw new NullPointerException("키가 로드되지 않았습니다.");
		return sign(plainText, privateKey);
	}

	/**
	 * 비밀키를 이용하여 인증하고, 인증서를 문자열로 리턴한다.
	 * @param plainText 인증할 문자열
	 * @param privateKeyText 비밀키 문자열
	 * @return 인증서 문자열
	 * @throws Exception 예외 객체
	 */
	public static String sign(String plainText, String privateKeyText) throws Exception {
		PrivateKey privateKey = restorePrivateKey(privateKeyText);
		if( privateKey == null)
			throw new NullPointerException("비밀키가 잘못되었습니다.");
		return sign(plainText, privateKey);
	}

	/**
	 * 비밀키를 이용하여 인증하고, 인증서를 문자열로 리턴한다.
	 * @param plainText 인증할 문자열
	 * @param privateKey 비밀키 객체
	 * @return 인증서 문자열
	 * @throws Exception 예외 객체
	 */
	protected static String sign(String plainText, PrivateKey privateKey) throws Exception {
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
		byte[] signedBytes = privateSignature.sign();
		return DatatypeConverter.printBase64Binary(signedBytes);
	}

	/**
	 * 공개키를 이용하여 sign된 텍스트와 원문을 비교하여 변조되지 않았는지 판단 한다.
	 * @param plainText 원문 문자열
	 * @param signatureText 인증서 문자열
	 * @return 변조되지 않았는지 여부
	 * @throws Exception 예외 객체
	 */
	public boolean verify(String plainText, String signatureText) throws Exception {
		if( publicKey == null)
			throw new NullPointerException("키가 로드되지 않았습니다.");
		return verify(plainText, signatureText, publicKey);
	}

	/**
	 * 공개키를 이용하여 sign된 텍스트와 원문을 비교하여 변조되지 않았는지 판단 한다.
	 * @param plainText 원문 문자열
	 * @param signatureText 인증서 문자열
	 * @param publicKeyText 공개키 문자열
	 * @return 변조되지 않았는지 여부
	 * @throws Exception 예외 객체
	 */
	public static boolean verify(String plainText, String signatureText, String publicKeyText)  throws Exception  {
		PublicKey publicKey = restorePublicKey(publicKeyText);
		if( publicKey == null)
			throw new NullPointerException("공개키가 잘못되었습니다.");
		return verify(plainText, signatureText, publicKey);
	}

	/**
	 * 공개키를 이용하여 sign된 텍스트와 원문을 비교하여 변조되지 않았는지 판단 한다.
	 * @param plainText 원문 문자열
	 * @param signatureText 인증서 문자열
	 * @param publicKey 공개키 객체
	 * @return 변조되지 않았는지 여부
	 * @throws Exception 예외 객체
	 */
	protected static boolean verify(String plainText, String signatureText, PublicKey publicKey)  throws Exception  {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update( plainText.getBytes(StandardCharsets.UTF_8) );
		byte[] signatureBytes = DatatypeConverter.parseBase64Binary(signatureText);

		return publicSignature.verify(signatureBytes);
	}

	/**
	 * 키를 생성한다.
	 * @return 성공 여부
	 */
	public boolean generateKey() {
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(KEYSIZE);

			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			publicKey = keyPair.getPublic();
			privateKey = keyPair.getPrivate();

			return true;
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
	}

	/**
	 * 공개키를 문자열로 변환한다.
	 * @return 공개키 문자열
	 */
	public String getPublicKey() {
		if( publicKey == null)
			throw new NullPointerException("키가 로드되지 않았습니다.");
		return DatatypeConverter.printBase64Binary(publicKey.getEncoded());
	}

	/**
	 * 비밀키를 문자열로 변환한다.
	 * @return 비밀키 문자열
	 */
	public String getPrivateKey() {
		if( privateKey == null)
			throw new NullPointerException("키가 로드되지 않았습니다.");
		return DatatypeConverter.printBase64Binary(privateKey.getEncoded());
	}

}
