/**
 * Copyright (C) 2015 OpenTravel Alliance (info@opentravel.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opentravel.pubs.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Used to perform two-way encryption for passwords that are stored in the
 * application configuration settings.
 */
public class PasswordHelper {
	
	protected static final String DEFAULT_ENCRYPTION_PASSWORD = "sl-kdibu*dkt(hcgen28~27^7";
	private static final String ENCRYPTION_ALGORITHM = "PBEWithMD5AndDES";
	private static final byte[] SALT = {
			(byte) 0xa2, (byte) 0x96, (byte) 0x94, (byte) 0x14,
			(byte) 0xb7, (byte) 0x5f, (byte) 0xe5, (byte) 0x27
		};
	
	/**
	 * Encrypts the given password.
	 * 
	 * @param clearTextPassword  the clear-text password to encrypt
	 * @return String
	 */
	public static String encrypt(String clearTextPassword) {
		try {
			String encryptionPassword = ConfigSettingsFactory.getConfig().getEncryptionPassword();
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( ENCRYPTION_ALGORITHM );
			SecretKey key = keyFactory.generateSecret( new PBEKeySpec( encryptionPassword.toCharArray() ) );
			Cipher pbeCipher = Cipher.getInstance( ENCRYPTION_ALGORITHM );
			
			pbeCipher.init( Cipher.ENCRYPT_MODE, key, new PBEParameterSpec( SALT, 20 ) );
			return new String( new Base64().encode( pbeCipher.doFinal( clearTextPassword.getBytes( "UTF-8" ) ) ) );
			
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			throw new RuntimeException( "Error during password encryption.", e );
		}
	}
	
	/**
	 * Decrypts the given encrypted password.
	 * 
	 * @param encryptedPassword  the encrypted password to decrypt
	 * @return
	 */
	public static String decrypt(String encryptedPassword) {
		try {
			String encryptionPassword = ConfigSettingsFactory.getConfig().getEncryptionPassword();
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( ENCRYPTION_ALGORITHM );
			SecretKey key = keyFactory.generateSecret( new PBEKeySpec( encryptionPassword.toCharArray() ) );
			Cipher pbeCipher = Cipher.getInstance( ENCRYPTION_ALGORITHM );
			
			pbeCipher.init( Cipher.DECRYPT_MODE, key, new PBEParameterSpec( SALT, 20 ) );
			return new String( pbeCipher.doFinal( new Base64().decode( encryptedPassword ) ), "UTF-8" );
			
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException( "Error during password decryption.", e );
		}
	}
	
}
