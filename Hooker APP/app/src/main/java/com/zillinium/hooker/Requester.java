package com.zillinium.hooker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.webkit.CookieManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Requester extends Activity {
    static CookieManager cm;
    static Context me;
    static List<String> cookies;
    static String testCookie;
    static HttpURLConnection conn;
    static String USER_AGENT = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36";
    static String currrentDocument;
    static String proxyHost;
    static int proxyPort;
    static SharedPreferences sharedPref;
    static SharedPreferences.Editor editor;

    public static void setup(Context ctx) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        editor = sharedPref.edit();
        me = ctx;

    }

    public static String Post(String url, String postParams) throws Exception {
        URL urlobj = new URL(url);
        conn = (HttpURLConnection) urlobj.openConnection();

        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if (cookies != null) {
            for (String cookie : cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
        if(sharedPref.contains("cookie"))
        {
        conn.addRequestProperty("Cookie", sharedPref.getString("cookie", ""));
        }
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
        conn.setFollowRedirects(true);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Send post request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        BufferedReader in = 
            new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder responseBuff = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseBuff.append(inputLine);
        }
        int responseCode = conn.getResponseCode();
        String responseMessage = conn.getResponseMessage();

        in.close();
        String response = responseBuff.toString();
        if (response.contains("src=\"/aes.js\"") || response.contains("src=\"/aes.min.js\"")) {
            testCookie = buildCookie(response);
            editor.putString("cookie", testCookie);
            editor.commit();
        }
        conn.disconnect();
        return response; 

    }

    public static String Get(String url) throws Exception {
        URL urlobj = new URL(url);
        cookies = getCookies();

        conn = (HttpURLConnection) urlobj.openConnection();
        // default is GET
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);

        // act like a browser

        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if (cookies != null) {
            for (String cookie : cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
        if(sharedPref.contains("cookie"))
        {
        conn.addRequestProperty("Cookie", sharedPref.getString("cookie", ""));
        }
        int responseCode = conn.getResponseCode();
        String responseMessage = conn.getResponseMessage();

        BufferedReader in = 
            new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer responseBuff = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            responseBuff.append(inputLine);
        }
        in.close();

        // Get the response cookies
        setCookies(conn.getHeaderFields().get("Set-Cookie"));

        String response = responseBuff.toString();
        if (response.contains("src=\"/aes.js\"") || response.contains("src=\"/aes.min.js\"")) {
            testCookie = buildCookie(response);
            editor.putString("cookie", testCookie);
            editor.commit();
        }
        conn.disconnect();
        return response;    
    }




    public static String buildCookie(String response) {
        String beginOffsetA = "var a=toNumbers(\"";
        String beginOffsetB = "\"),b=toNumbers(\"";
        String beginOffsetC = "\"),c=toNumbers(\"";
        String endOffsetC = "\");document.cookie=";
        String a = response.substring((response.indexOf(beginOffsetA) + (beginOffsetA).length()), response.indexOf(beginOffsetB)); // Value of var a
        String b = response.substring((response.indexOf(beginOffsetB) + (beginOffsetB).length()), response.indexOf(beginOffsetC)); // Value of var b
        String c = response.substring((response.indexOf(beginOffsetC) + (beginOffsetC).length()), response.indexOf(endOffsetC)); // Value of var c
        return "__test=" + encrypt(hexStringToByteArray(a), hexStringToByteArray(b), hexStringToByteArray(c)).toLowerCase() + "; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/";   
    }



    public static String encrypt(byte[] key, byte[] initVector, byte[] data) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(data);
            return bytesToHex(encrypted);
        }
        catch (Exception ex) {

        }
        return null;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }






    public String getFormParams(String html, String username, String password)
    throws UnsupportedEncodingException {
        Document doc = Jsoup.parse(html);
        // Google form id
        Elements inputElements = doc.getElementsByTag("input");
        List<String> paramList = new ArrayList<String>();
        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");

            if (key.equals("email"))
                value = username;
            else if (key.equals("pass"))
                value = password;
            else if (key.equals("login"))
                value = "Login";

            paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
            paramList.remove("sign_up=Create+New+Account");
        }

        // build parameters list
        StringBuilder result = new StringBuilder();
        for (String param : paramList) {
            if (result.length() == 0) {
                result.append(param);
            }
            else {
                result.append("&" + param);
            }
        }
        return result.toString();
    }

    public static List<String> getCookies() {
        return cookies;
    }

    public static void setCookies(List<String> cookie) {
        cookies = cookie;
    }

}


