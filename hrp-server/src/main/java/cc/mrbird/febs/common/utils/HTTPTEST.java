package cc.mrbird.febs.common.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.jni.SSL;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class HTTPTEST {

    public static void main(String[] args) {
//		System.out.print(getToken());
        System.out.print(getTokenByPOST());
        System.out.print(getTokenByGET());
        //getFile();
    }

    public static void getFile(HttpServletResponse response, String fileName,String path) throws UnsupportedEncodingException {
        log.info("开始下载方法");
        StringBuffer parameters = new StringBuffer();
        parameters.append("fileId="+path);
        parameters.append("&token="+getTokenByPOST());
        log.info("token:"+getTokenByPOST());
        URL preUrl = null;
        URLConnection uc = null;
        response.setCharacterEncoding("utf-8");
        //自动判断下载文件类型
        response.setContentType("multipart/form-data");

        //设置文件头  Content-Disposition 让浏览器弹出下载对话框
        response.setHeader("Content-Disposition", "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));
        try {

            getSslf_con();
            //preUrl = new URL("http://172.16.1.156:8888/seeyon/services/downloadService");
            preUrl = new URL("https://10.250.11.140:5888/seeyon/services/downloadService");
            String s = parameters.toString();



            uc = preUrl.openConnection();
            uc.setDoOutput(true);
            uc.setUseCaches(false);

            uc.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            HttpURLConnection hc = (HttpURLConnection) uc;

            hc.setRequestMethod("POST");


            OutputStream os = hc.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeBytes(s);
            dos.flush();
            dos.close();

                log.info("这是请求的200");
                os = response.getOutputStream();
                InputStream is = hc.getInputStream();
                int ch;
                while ((ch = is.read()) != -1) {
                    os.write(ch);
                }
                if (is != null) {
                    is.close();
                }


        }catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static String getTokenByGET(){
        //String url = "http://172.16.1.156:8888/seeyon/rest/token/dee/2935a2a2-1a3f-4414-8330-2716ebbf3093";
        String url = "https://10.250.11.140:5888/seeyon/rest/token/dee/2d3e7ba4-d4a1-485f-ae8f-e307db688f40";
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        JSONObject jsonResult = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
            jsonResult = JSONObject.fromObject(result);
            Map param=(Map) jsonResult;
            if (param.containsKey("id")){
                System.out.println(param.get("id"));
                result = (String) param.get("id");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String getTokenByPOST() {
        String url = "https://10.250.11.140:5888/seeyon/rest/token";
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        JSONObject jsonResult = null;
        String result = "";
        // 创建httpClient实例
        try {
            httpClient = HttpClients.custom().setSSLSocketFactory(getSslf()).build();
        }
        catch (Exception ex){
            log.info(ex.getMessage());
        }
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例10.250.11.140
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();

        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
      //  httpPost.addHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
        httpPost.addHeader("Content-Type", "application/json");
        // 封装post请求参数
        Map<String,String> params=new HashMap<>();
        params.put("password","2d3e7ba4-d4a1-485f-ae8f-e307db688f40");
      //  params.put("password","lRW2bBaBWQN8q7i4+oNcbdnZYNU=");
        params.put("userName","dee");
        //java对象变成json对象
        JSONObject jsonObject=JSONObject.fromObject(params);
        //json对象转换成json字符串
        String jsonStr=jsonObject.toString();
        System.out.println(jsonStr);
       // log.info("json字符串:"+jsonStr);
        // 为httpPost设置封装好的请求参数
        try {
            httpPost.setEntity(new StringEntity(jsonStr));
        } catch (UnsupportedEncodingException e) {
            log.info("错误HR:"+e.getMessage());
            e.printStackTrace();
        }

        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
//            result = EntityUtils.toString(entity);
            result = EntityUtils.toString(entity);
            log.info("返回结果:"+result);
            jsonResult = JSONObject.fromObject(result);

            Map param=(Map) jsonResult;
            if (param.containsKey("id")){
                result = (String) param.get("id");
            }
        } catch (ClientProtocolException e) {
            log.info("错误HR2222:"+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static  SSLConnectionSocketFactory getSslf() throws NoSuchAlgorithmException, KeyManagementException{
        SSLContext sslContext= SSLContext.getInstance("SSL");
        X509TrustManager tm= new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };
        sslContext.init(null,new TrustManager[]{tm},null);
        SSLConnectionSocketFactory sslsf= new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return  sslsf;
    }
    public static void   getSslf_con() throws NoSuchAlgorithmException, KeyManagementException{
        SSLContext sslContext= SSLContext.getInstance("SSL");
        X509TrustManager tm= new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };
        sslContext.init(null,new TrustManager[]{tm},null);
        HostnameVerifier igonre= new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(igonre);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }
}