import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final String PATTERN_CRFTOKEN = "\"csrfToken\":\"[^.\"]++\"";
    private static final String PATTERN_YANDEXUID = "yandexuid=\\d++";
    private static final String ADDRESS = "ул. Карла Маркса, 246";
    private static final String PATTERN_COORDINATES = "\"address\":\"" +
            ADDRESS + "\",\"coordinates\":\\[[.]++\\]";
    private static final String URL_YANDEX = "https://yandex.ru/maps/";
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        HTTPRequestService httpRequestService = new HTTPRequestService();
        httpRequestService.setUrl(URL_YANDEX);
        try {
            httpRequestService.setCookie("yandexuid=2843151081544514694");
            String response = httpRequestService.getRequest();
            logger.info(response);
            String crfToken = httpRequestService.parseByRegExp(response, PATTERN_CRFTOKEN);
            String yandexuid = httpRequestService.parseByRegExp(response, PATTERN_YANDEXUID);
            if (null != yandexuid) {
                httpRequestService.setCookie(yandexuid);
                logger.info(String.format("yandexuid = %s", yandexuid));
            }
            if (null != crfToken) {
                crfToken = crfToken.split(":\"")[1].replaceAll("\"", "");
                logger.info(String.format("crfToken = %s", crfToken));
                httpRequestService.setUrl(URL_YANDEX + "?text=" + URLEncoder.encode(
                        ADDRESS, StandardCharsets.UTF_8.name()) +
                        "&lang=ru_RU&csrfToken=" + crfToken);
                response = httpRequestService.getRequest();
            }
            logger.info(response);
            logger.info(httpRequestService.parseByRegExp(response, PATTERN_COORDINATES));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
