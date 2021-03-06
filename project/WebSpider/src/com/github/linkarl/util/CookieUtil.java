package com.github.linkarl.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

public class CookieUtil {
	private final static String preFix = "cookie/";

	public static List<Cookie> resolve(String file) {
		return resolve(file, Consts.UTF_8);
	}

	/**
	 * domain path expiry 在我们的提交请求中不会用到
	 * test.txt是范例
	 */
	public static List<Cookie> resolve(String file, Charset charset) {
		List<Cookie> list = new ArrayList<>();
		try {
			List<String> readLines = FileUtils.readLines(
					new File(preFix + file), charset);
			for (String string : readLines) {
				String[] split = string.split(";");
				String key = split[0].trim();
				String value = "";
				if (split.length > 1) {
					value = split[1].trim();
				}
				BasicClientCookie cookie = new BasicClientCookie(key, value);
				list.add(cookie);
				System.out.println(cookie.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) {
		resolve("test.txt", Consts.UTF_8);
	}
}
