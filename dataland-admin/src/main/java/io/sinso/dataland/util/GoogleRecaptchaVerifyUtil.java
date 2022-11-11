package io.sinso.dataland.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author hengbol
 * @date 4/25/22 4:44 PM
 */
@Slf4j
public class GoogleRecaptchaVerifyUtil {

    public static synchronized boolean isCaptchaValid(String secretKey, String response) {
        try {
            log.info("secretKey=====>" + secretKey);
            String url = "https://www.google.com/recaptcha/api/siteverify",
                    params = "secret=" + secretKey + "&response=" + response;
            HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            OutputStream out = http.getOutputStream();
            out.write(params.getBytes("UTF-8"));
            out.flush();
            out.close();

            InputStream res = http.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(res, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            log.info("v2retuen====>" + sb.toString());
            JSONObject jsonObject = JSON.parseObject(sb.toString());
            boolean success = Boolean.parseBoolean(jsonObject.get("success").toString());
            res.close();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        String response = "";
        isCaptchaValid("3RrA1FDa6mdw5JwKbUxEbZbMcJgSyWjhNwxsbX5pSos8", response);
    }




}
