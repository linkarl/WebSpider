package com.github.linkarl.main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public abstract class WebSpider {
	private final Logger logger = Logger.getLogger(WebSpider.class);
	private final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)";
	protected Charset defaultCharset = Charsets.UTF_8;
	protected BasicCookieStore cookieStore;// cookie存储
	protected CloseableHttpClient httpClient;
	protected String url;
	protected String reponseHtml;
	protected HttpResponse response;

	public WebSpider(String url) {
		super();
		cookieStore = new BasicCookieStore();
		httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		this.url = url;
	}

	public WebSpider(BasicCookieStore cookieStore,
			CloseableHttpClient httpClient, String url) {
		super();
		this.cookieStore = cookieStore;
		this.httpClient = httpClient;
		this.url = url;
	}

	/**
	 * 打印cookie的参数
	 */
	protected void outputCookie() {
		if (null != cookieStore) {
			List<Cookie> cookies = cookieStore.getCookies();
			for (Cookie cookie : cookies) {
				// todo 应该像 firefox的copycookie那样的格式
				logger.info(cookie.getName() + ":" + cookie.getValue());
			}
		} else {
			logger.info("uninitialize cookieStore");
		}
	}

	protected void init() {
		setProxy();
		setHttpMethod();
		checkSuccess();
		praseHtml();
		outputData();
	}
	
	/**
	 * 输出数据 如图片 pdf 文本 excel 等等 
	 */
	protected abstract void  outputData();
	/**
	 * 处理返回的html数据获取真正的结果
	 */
	protected abstract void praseHtml();
	/**
	 * 校验是否返回成功  
	 * 响应状态不一定能说明返回是否成功
	 */
	protected abstract void checkSuccess();
	/**
	 * 设置代理 TODO
	 */
	protected abstract void setProxy();

	/**
	 * 设置请求方式
	 */
	protected abstract void setHttpMethod();

	protected String post(CloseableHttpClient httpclient, String url,
			Map<String, String> params) {
		HttpPost httpPost = postForm(url, params);
		logger.info("httppost:" + url);
		response = getResponse(httpclient, httpPost);
		reponseHtml = getReponseHtml(response);
		httpPost.releaseConnection();
		return reponseHtml;
	}

	protected String get(CloseableHttpClient httpclient, String url,
			List<Header> headerList) {
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Accept-Charset", defaultCharset.name());
		httpGet.addHeader("User-Agent", USER_AGENT);
		if (null != headerList) {
			for (Header header : headerList) {
				httpGet.addHeader(header);
			}
		}
		logger.info("httpget:" + url);
		response = getResponse(httpclient, httpGet);
		reponseHtml = getReponseHtml(response);
		httpGet.releaseConnection();
		return reponseHtml;
	}

	/**
	 * 解析response
	 * 
	 * @param response
	 * @return 返回响应内容
	 */
	private String getReponseHtml(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		logger.info("statusline:" + response.getStatusLine());
		String content = null;
		try {
			ContentType charset = ContentType.getOrDefault(entity);
			if (null != charset) {
				logger.info("charset:" + charset.getCharset());
			}
			content = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;

	}

	/**
	 * http请求
	 * 
	 * @param httpclient
	 * @param httpRequest
	 * @return 响应结果HttpResponse
	 */
	private HttpResponse getResponse(CloseableHttpClient httpclient,
			HttpRequestBase httpRequest) {
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpRequest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 请求参数加入到请求中
	 * 
	 * @param url
	 * @param paramMap
	 * @return HttpPost
	 */
	private HttpPost postForm(String url, Map<String, String> paramMap) {
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> paramlist = new ArrayList<NameValuePair>();
		Set<String> keySet = paramMap.keySet();
		logger.info("postParam:");
		for (String key : keySet) {
			logger.info(key + ":" + paramMap.get(key));
			paramlist.add(new BasicNameValuePair(key, paramMap.get(key)));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(paramlist, defaultCharset));
		return httpPost;
	}
}
