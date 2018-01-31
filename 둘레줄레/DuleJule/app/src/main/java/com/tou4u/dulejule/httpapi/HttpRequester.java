package com.tou4u.dulejule.httpapi;

import com.tou4u.dulejule.util.DebugUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequester {

    private static final String TAG = "HttpRequester";

    public String requestGetData(String url, Map<String, String> header, Map<String, Object> params) {
        String result = receiveGetData(url, header, params);
        return result;
    }

    public String requestPostData(String url, Map<String, String> header, Map<String, Object> args,
                                  Map<String, Object> params) {
        String result = receivePostData(url, header, args, params);
        return result;
    }

    private HttpURLConnection httpConnect(String urlString, boolean read, boolean write, String method,
                                          Map<String, String> header) {
        URL url;
        try {
            url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoInput(read);
            httpURLConnection.setDoOutput(write);

            if (header != null) {
                Iterator<Entry<String, String>> i = header.entrySet().iterator();
                while (true) {
                    Entry<String, String> h = i.next();
                    httpURLConnection.setRequestProperty(h.getKey(), h.getValue());
                    if (!i.hasNext()) {
                        break;
                    }
                }
            }

            httpURLConnection.connect();
            return httpURLConnection;
        } catch (MalformedURLException e) {
            DebugUtil.logE(TAG, "Error On Connect", e);
        } catch (IOException e) {
            DebugUtil.logE(TAG, "Error On Connect IOExcept", e);
        }
        return null;
    }

    private String receivePostData(String url, Map<String, String> header, Map<String, Object> args,
                                   Map<String, Object> params) {

        String arg = null;
        if (args != null) {
            arg = "";
            Iterator<Entry<String, Object>> i = args.entrySet().iterator();
            while (true) {
                Entry<String, Object> e = i.next();
                String a = String.format("%s=%s", e.getKey(), e.getValue());
                arg += a;
                if (i.hasNext()) {
                    arg += "&";
                } else {
                    break;
                }
            }
        }

        if (arg != null) {
            url += ("?" + arg);
        }

        boolean write = (params != null);
        HttpURLConnection httpConnection = httpConnect(url, true, write, "POST", header);

        String param = null;
        if (params != null) {
            param = "";
            Iterator<Entry<String, Object>> i = params.entrySet().iterator();
            while (true) {
                Entry<String, Object> e = i.next();
                String p = String.format("%s=%s", e.getKey(), e.getValue());
                param += p;
                if (i.hasNext()) {
                    param += "&";
                } else {
                    break;
                }
            }
        }

        String result = getData(httpConnection, param);
        return result;
    }

    private String receiveGetData(String url, Map<String, String> header, Map<String, Object> params) {

        String param = null;
        if (params != null) {
            param = "";
            Iterator<Entry<String, Object>> i = params.entrySet().iterator();
            while (true) {
                Entry<String, Object> e = i.next();
                String p = String.format("%s=%s", e.getKey(), e.getValue());
                param += p;
                if (i.hasNext()) {
                    param += "&";
                } else {
                    break;
                }
            }
        }

        if (param != null) {
            url += ("?" + param);
        }

        HttpURLConnection httpConnection = httpConnect(url, true, false, "GET", header);

        String result = getData(httpConnection, null);
        return result;
    }

    private String getData(HttpURLConnection httpConnection, String param) {

        try {

            if (param != null) {
                OutputStream os = httpConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write(param);
                bw.flush();
                bw.close();
            }

            int responseStatusCode = httpConnection.getResponseCode();
            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
            } else {
                inputStream = httpConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();

            return sb.toString();
        } catch (NullPointerException ne) {
            DebugUtil.logE(TAG, "Error On Get Data", ne);
        } catch (IOException e) {
            DebugUtil.logE(TAG, "Error On Get Data", e);
        }

        return null;
    }

}
