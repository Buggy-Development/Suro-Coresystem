package de.TheHolyException.suro.utils;

import java.util.Base64;

public class Base64Utils {
	
	public static String encodeB64(String value) {
		try {
			return Base64.getEncoder().encodeToString(value.getBytes());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String decodeB64(String value) {
		try {
			byte[] decodedValue = Base64.getDecoder().decode(value.getBytes());
			return new String(decodedValue);
		} catch (Exception e) {
			return null;
		}
	}

}
