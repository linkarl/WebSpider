package com.github.linkarl.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;

/**
 * 输出 pdf excel txt 图片 等等
 */
public class OutputUtil {
	private final static String preFix = "output/";
	
	public static void outputPdf(String file,String data,Charset charset) {

	}
	//前提是data已经是excel的了 从txt转excel要重新写方法
	public static void outputExcel(String file,String data,Charset charset) {

	}
	
	public static void outputString(String file,String data,Charset charset) {
		if (null==charset) {
			charset=Consts.UTF_8;
		}
		try {
			FileUtils.writeStringToFile(new File(file), data, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
