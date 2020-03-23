/*
 * Copyright 2014 by ShangHai Huateng Software Systems CO.,LTD.
 * 
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of Huateng
 * Corporation ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Huateng Corporation.
 */
package com.huateng.fm.ui.complex.lockpattern;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Lock pattern utils, for convert pattern to string or encrypt pattern
 * 
 * @author LvBeier
 * @version V1.0 2014-12-25
 */

public class LockPatternUtils {

	/**
	 * Serialize a pattern.
	 * 
	 * @param pattern
	 *            The pattern.
	 * @return The pattern in string form.
	 */
	public static String patternToString(List<FmLockPatternView.Cell> pattern) {
		if (pattern == null) {
			return "";
		}
		final int patternSize = pattern.size();

		byte[] res = new byte[patternSize];
		for (int i = 0; i < patternSize; i++) {
			FmLockPatternView.Cell cell = pattern.get(i);
			res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
		}
		System.out.println(Arrays.toString(res));
		String pwd = new String(res);
		return pwd;
	}

	/**
	 * Deserialize a pattern.
	 * 
	 * @author LvBeier
	 * @param string
	 *            The pattern serialized with {@link #patternToString}
	 * @return The pattern.
	 */
	public static List<FmLockPatternView.Cell> stringToPattern(String string) {
		List<FmLockPatternView.Cell> result = new ArrayList<FmLockPatternView.Cell>();

		final byte[] bytes = string.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			result.add(FmLockPatternView.Cell.of(b / 3, b % 3));
		}
		return result;
	}

	/**
	 * Generate an SHA-1 hash for the pattern. Not the most secure, but it is at
	 * least a second level of protection. First level is that the file is in a
	 * location only readable by the system process.
	 * 
	 * @author LvBeier
	 * @param pattern
	 *            the gesture pattern.
	 * @return the hash of the pattern in a byte array.
	 */
	public static byte[] patternToHash(List<FmLockPatternView.Cell> pattern) {
		if (pattern == null) {
			return null;
		}

		final int patternSize = pattern.size();
		byte[] res = new byte[patternSize];
		for (int i = 0; i < patternSize; i++) {
			FmLockPatternView.Cell cell = pattern.get(i);
			res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
		}
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] hash = md.digest(res);
			return hash;
		} catch (NoSuchAlgorithmException nsa) {
			return res;
		}
	}

}
