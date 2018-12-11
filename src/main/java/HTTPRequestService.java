import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPRequestService {
    private String hostURL;
    private String cookie;

    public void setUrl(String url) {
        this.hostURL = url;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getRequest() throws IOException {
        URL url = new URL(hostURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        if (null != cookie) {
            con.setRequestProperty("Cookie", cookie);
        }
        con.setRequestMethod("GET");
        StringBuilder result = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        con.disconnect();
        return result.toString();
    }

    public String parseByRegExp(String line, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }
}
