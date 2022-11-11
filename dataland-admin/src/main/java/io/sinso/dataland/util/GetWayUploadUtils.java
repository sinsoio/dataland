package io.sinso.dataland.util;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author hengbol
 * @date 6/20/22 11:33 AM
 */
public class GetWayUploadUtils {
    /**
     * type 1 Picture 2 File 3 Video 4 audio
     *
     * @return
     */
    public static String upload(InputStream inputStream, String name, String getWayUrl, String token, String type) {
        try {
            String encodeStr = URLEncoder.encode(name, "UTF-8");
            URL url = new URL(getWayUrl + "?name=" + encodeStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //Please enter your token
            conn.setRequestProperty("Token", token);
            conn.setRequestProperty("Content-type", type);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(1024 * 1024);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setConnectTimeout(50000);
            conn.connect();
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            DataInputStream in = new DataInputStream(inputStream);
            int bytes = 0;
            byte[] bufferOut = new byte[2048];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();

            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                return line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Download the file from the network Url
     *
     * @param urlStr
     * @throws IOException
     */
    public static InputStream downLoadFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            return conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
