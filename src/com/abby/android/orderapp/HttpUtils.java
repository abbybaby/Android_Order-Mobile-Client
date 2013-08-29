package com.abby.android.orderapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class HttpUtils {
	public static final int TIMEOUT = 2000;// ³¬Ê±Ê±¼ä

	
	public static Object post(String url, Map<String, String> params)
			throws Exception {
		PostTask postTask = new PostTask(params);
		postTask.execute(url);
		return postTask.get();

	}


	public static void sendObject(String url, Object object) throws Exception {
		SendObjectTask sendObjectTask = new SendObjectTask(object);
		sendObjectTask.execute(url);
	}

	private static class SendObjectTask extends
			AsyncTask<String, Integer, Void> {

		Object object;

		public SendObjectTask(Object object) {
			this.object = object;
		}

		@Override
		protected Void doInBackground(String... url) {
			HttpURLConnection connection;
			try {
				connection = (HttpURLConnection) new URL(url[0])
						.openConnection();
				connection.setConnectTimeout(TIMEOUT);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type",
						"application/octet-stream");
				OutputStream os = connection.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(object);
				oos.flush();
				oos.close();
				os.close();
				connection.getInputStream().close();
				connection.disconnect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	private static class PostTask extends AsyncTask<String, Integer, Object> {

		Map<String, String> params = null;

		public PostTask(Map<String, String> params) {
			this.params = params;
		}

		@Override
		protected Object doInBackground(String... url) {
			Object object = null;
			HttpClient client = new DefaultHttpClient();
			HttpParams p = client.getParams();
			p.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
			p.setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
			HttpPost post = new HttpPost(url[0]);

			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> e : params.entrySet()) {
					formParams.add(new BasicNameValuePair(e.getKey(), e
							.getValue()));
				}
				UrlEncodedFormEntity formEntity;
				try {
					formEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
					post.setEntity(formEntity);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

			}
			HttpResponse response;
			try {
				response = client.execute(post);
				int status = response.getStatusLine().getStatusCode();
				if (status != 200) {
					try {
						throw new Exception("ErrorCode:" + status);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				HttpEntity entity = response.getEntity();
				if (entity.getContentLength() == 0) {
					Log.i("aaaaaaaaaaaaaaaaaaa",
							"---------------------------------");
					return null;
				}
				Log.i("bbbbbbbbbbbbbbbbbbbbbbbbb",
						"---------------------------------");
				InputStream is = entity.getContent();
				ObjectInputStream ois = new ObjectInputStream(is);

				try {
					object = ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				ois.close();
				is.close();
				client.getConnectionManager().shutdown();

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return object;
		}

	}
/*	public static final int TIMEOUT=2000;
	
	public static Object post(String url,Map<String, String>params)  throws Exception
	{
		
		HttpClient client=new DefaultHttpClient();
		HttpParams p=client.getParams();
		p.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
		p.setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
		HttpPost post=new HttpPost(url);
		if (params !=null && !params.isEmpty()) 
		{
			List<NameValuePair> formParams=new ArrayList<NameValuePair>();
			for(Map.Entry<String, String> e :params.entrySet())
				formParams.add(new BasicNameValuePair(e.getKey(),e.getValue()));
			UrlEncodedFormEntity formEntity=new UrlEncodedFormEntity(formParams,"UTF-8");
			post.setEntity(formEntity);
		}
		HttpResponse resp=client.execute(post);
		int status=resp.getStatusLine().getStatusCode();
		if (status!=200) 
			throw new Exception("ErrorCode:"+status);
		HttpEntity entity=resp.getEntity();
		if (entity.getContentLength()==0)
			return null;
		InputStream is=entity.getContent();
		ObjectInputStream ois=new ObjectInputStream(is);
		Object obj=ois.readObject();
		ois.close();
		is.close();
		client.getConnectionManager().shutdown();
		return obj;
	}
	
	public static void sendObject(String url,Object object) throws Exception 
	{
		HttpURLConnection conn=(HttpURLConnection) new URL(url).openConnection();
		conn.setConnectTimeout(TIMEOUT);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/octet-stream");
		OutputStream os=conn.getOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(os);
		oos.writeObject(object);
		oos.flush();
		oos.close();
		os.close();
		conn.getInputStream().close();
		conn.disconnect();
	}*/
}
