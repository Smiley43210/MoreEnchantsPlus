/*
 * Decompiled with CFR 0_122.
 */
package me.adam561.mep2;

public class RomanNumerals {
	private static final String[] romans = new String[]{"M", "XM", "CM", "D", "XD", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
	private static final int[] numbers = new int[]{1000, 990, 900, 500, 490, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
	
	public static String getRoman(int number) {
		StringBuilder roman = new StringBuilder();
		int i = 0;
		while (number > 0 || numbers.length == i - 1) {
			while (number - numbers[i] >= 0) {
				number -= numbers[i];
				roman.append(romans[i]);
			}
			++i;
		}
		return roman.toString();
	}
	
	public static int getNumber(String roman) {
		int result = 0;
		char[] chars = roman.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			if (i + 1 < chars.length) {
				result += RomanNumerals.getSingleNumber("" + chars[i] + "" + chars[i + 1] + "");
				++i;
				continue;
			}
			result += RomanNumerals.getSingleNumber("" + chars[i] + "");
		}
		return result;
	}
	
	private static int getSingleNumber(String roman) {
		int result = 0;
		for (int i = 0; i < romans.length; ++i) {
			if (!romans[i].equalsIgnoreCase(roman)) {
				continue;
			}
			result = numbers[i];
		}
		if (result == 0) {
			result += RomanNumerals.getSingleNumber("" + roman.toCharArray()[0] + "");
			result += RomanNumerals.getSingleNumber("" + roman.toCharArray()[1] + "");
		}
		return result;
	}
}

