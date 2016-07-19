package com.github.linkarl.proxy;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;

public class ProxyPool {
	private final static String path = "proxy/test.txt";
	private List<Proxy> proxyList = new ArrayList<>();

	public ProxyPool() {
		super();
		initProxy();
		testProxy();
	}

	/**
	 * 测试代理的效率 条件连接网页用时短的在前面
	 */
	private void testProxy() {
		// 主线程定时启动该服务 更新代理优先队列
		// 多线程测试代理的活性
		//活性的定义 根据连接某个网站的超时时间
	}

	/**
	 * 获取有效的代理
	 */
	public void getVaildProxy() {
		
	}

	/**
	 * 代理用完后 设置该代理目前效率 显示调用加回代理队列 相应的位置
	 */
	public void put(Proxy proxy) {
		
	}

	private void initProxy() {
		try {
			List<String> readLines = FileUtils.readLines(new File(path),
					Consts.UTF_8.name());
			for (String string : readLines) {
				String[] split = string.split("\\s");
				String ip = split[0].trim();
				String port = split[1].trim();
				SocketAddress address = new InetSocketAddress(ip,
						Integer.valueOf(port));
				proxyList.add(new Proxy(Proxy.Type.HTTP, address));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ProxyPool pool = new ProxyPool();
	}
}
