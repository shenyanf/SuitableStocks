package com.shen.test;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JustTest {

	public static void main(String[] args) {
		String str1 = "111姗";
		String str2 = "-222还是地方";
		String str3 = "3.33%";
		String str4 = "-44.4sdfew";
		Pattern pattern = Pattern.compile("(^-?\\d*\\.?\\d*).*");
		Matcher matcher = pattern.matcher(str3);
		if (matcher.find()) {
			System.out.println(matcher.group(1));
		}
		
		System.out.println(Integer.valueOf("-433.3%"));
	}

}
