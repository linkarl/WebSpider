package com.github.linkarl.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.TextUtils;
import org.jsoup.helper.StringUtil;

/**
 * ��������ͷ�� ͨ���ƶ��ļ���
 */
public class HeaderUtil {
	private final static String preFix="header/";
	
	/**
	 * Ĭ��ʹ��utf-8����
	 */
	public static List<Header> resolve(String file) {
		return resolve(file, Consts.UTF_8);
	}
	
	/**
	 * ���������ĸ�ʽ��  ���е�Host: www.baidu.com
	 * test.txt�Ƿ���
	 */
	public static List<Header> resolve(String file, Charset charset) {
		List<Header> list=new ArrayList<>();
		try {
			List<String> readLines = FileUtils.readLines(new File(preFix+file),
					charset);
			for (String string : readLines) {
				String[] split = string.split(":");
				String key = split[0].trim();
				String value="";
				if (split.length>1) {
					value = split[1].trim();
				}
				list.add(new BasicHeader(key, value));
				System.out.println(new BasicHeader(key, value).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		resolve("test.txt");
	}
}