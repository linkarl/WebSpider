package com.github.linkarl.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;

/**
 * ��� pdf excel txt ͼƬ �ȵ�
 */
public class OutputUtil {
	private final static String preFix = "output/";
	
	public static void outputPdf(String file,String data,Charset charset) {

	}
	//ǰ����data�Ѿ���excel���� ��txtתexcelҪ����д����
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
