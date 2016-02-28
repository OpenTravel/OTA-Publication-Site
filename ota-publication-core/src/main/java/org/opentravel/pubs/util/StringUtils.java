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
package org.opentravel.pubs.util;

/**
 * Static utility methods to assist with string manipulation tasks.
 * 
 * @author S. Livezey
 */
public class StringUtils {
	
	private static final char[] hex = "0123456789abcdef".toCharArray();
	
	/**
	 * Trims the given string.  If the length of the resulting string is zero, this
	 * method will return null.
	 * 
	 * @param str  the string to trim
	 * @return String
	 */
	public static String trimString(String str) {
		String result = (str == null) ? null : str.trim();
		
		if ((result != null) && (result.length() == 0)) {
			result = null;
		}
		return result;
	}
	
	/**
	 * Returns a user-displayable string based on the original string provided.  The
	 * resulting string includes spaces anywhere a camel-case word break is detected.
	 * 
	 * @param str  the original string to convert
	 * @return String
	 */
	public static String toDisplayString(String str) {
		StringBuilder out = new StringBuilder();
		boolean lastLowercase = false;
		
		for (char ch : str.replaceAll("_", " ").toCharArray()) {
			boolean currentLowercase = Character.isLowerCase( ch );
			
			if (lastLowercase && !currentLowercase) {
				out.append(' ');
			}
			out.append( ch );
			lastLowercase = currentLowercase;
		}
		return out.toString();
	}
	
	/**
	 * Returns a hex string representation of the given byte array.
	 * 
	 * @param bytes  the byte array to convert to hex representation
	 * @return String
	 */
	public static String toHexString(byte[] bytes) {
		if (null == bytes) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder(bytes.length << 1);
		
		for (int i = 0; i < bytes.length; ++i) {
			sb.append(hex[(bytes[i] & 0xf0) >> 4]).append(hex[(bytes[i] & 0x0f)]);
		}
		
		return sb.toString();
	}
    
	/**
	 * Parses and returns the numeric string value or zero in the case of a null or error string value.
	 * 
	 * @param valueStr  the value string to parse
	 * @return int
	 */
	public static int parseIntValue(String valueStr) {
		int result = 0;
		
		if (valueStr != null) {
			try {
				result = Integer.parseInt( valueStr );
				
			} catch (NumberFormatException e) {
				// No exception - return zero
			}
		}
		return result;
	}
	
	/**
	 * Parses and returns the numeric string value or zero in the case of a null or error string value.
	 * 
	 * @param valueStr  the value string to parse
	 * @return long
	 */
	public static long parseLongValue(String valueStr) {
		long result = 0L;
		
		if (valueStr != null) {
			try {
				result = Long.parseLong( valueStr );
				
			} catch (NumberFormatException e) {
				// No exception - return zero
			}
		}
		return result;
	}
	
	/**
	 * Parses and returns the boolean string value or false in the case of a null or error string value.
	 * 
	 * @param valueStr  the value string to parse
	 * @return boolean
	 */
	public static boolean parseBooleanValue(String valueStr) {
		boolean result = false;
		
		if (valueStr != null) {
			try {
				result = Boolean.parseBoolean( valueStr );
				
			} catch (NumberFormatException e) {
				// No exception - return false
			}
		}
		return result;
	}
	
}
