import exceptions.NotSetCookieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String PATTERN_CRFTOKEN = "\"csrfToken\":\"[^.\"]++\"";
    private static final String PATTERN_YANDEXUID = "yandexuid=\\d++";
    private static final String ADDRESS = "Удмуртская Республика, Ижевск, улица Максима Горького, 161";
    private static final String PATTERN_ADDRES_COORDINATES = "\"address\":\"" + ADDRESS
            + "\",\"coordinates\":\\[[\\d.\\d,]++\\]";
    private static final String URL_YANDEX = "https://yandex.ru/maps/";
    private static final String PATTERN_COORDINATES = "[\\d\\.]+\\.[\\d]+";
    private static final String COOKIE = "_ym_d=1544445890;" +
            "_ym_uid=1544445890527578342;" +
            "i=kC93XB5qVLvpX8essSvvxeGLbw6zPijK1mYc+peGXtto7CCR9AJax1iQt5hn4GXQHeSVFQdgDLN8+dSQDVnE3dht8ac=;" +
            "maps_los=0;" +
            "mda=0;" +
            "my=YwA=;" +
            "spravka=dD0xNTQ0NDQ1ODk4O2k9NzkuMTMzLjc1LjU4O3U9MTU0NDQ0NTg5ODEyMDkwOTU4ODtoPWRlZjIxOGZmYzFjYjlkZGM0OTY4MzM2Y2VkZTEwM2Nm;" +
            "yandexuid=1155325281541656074;" +
            "yp=1560277203.szm.1:1920x1080:1920x963#1859870647.yrtsi.1544510647#1544597047.yu.5524790431544445885;";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        HTTPRequestService httpRequestService = new HTTPRequestService();
        httpRequestService.setUrl(URL_YANDEX);
        try {
            httpRequestService.setCookie(COOKIE);
            String response = httpRequestService.getRequest();
            logger.info(response);
            String crfToken = httpRequestService.parseByRegExp(response, PATTERN_CRFTOKEN).get(0);
            String yandexuid = httpRequestService.parseByRegExp(response, PATTERN_YANDEXUID).get(0);

            if (null != yandexuid) {
                logger.info(String.format("yandexuid = %s", yandexuid));
            }

            if (null != crfToken) {
                crfToken = crfToken.split(":\"")[1].replaceAll("\"", "");
                String address = URL_YANDEX + "?text=" + URLEncoder.encode(
                        ADDRESS, StandardCharsets.UTF_8.name()) +
                        "&lang=ru_RU&csrfToken=";
                logger.info(String.format("crfToken = %s", crfToken));
                httpRequestService.setUrl(address + crfToken);
                response = httpRequestService.getRequest();
            }


            logger.info(response);
            logger.info(String.format("Coordinates : %s , %s", httpRequestService.parseByRegExp(
                    httpRequestService.parseByRegExp(response, PATTERN_ADDRES_COORDINATES).get(0),
                    PATTERN_COORDINATES).get(0),
                    httpRequestService.parseByRegExp(
                            httpRequestService.parseByRegExp(response, PATTERN_ADDRES_COORDINATES).get(0),
                            PATTERN_COORDINATES).get(1)));


        } catch (IOException e) {
            logger.error("Wrong request");
        } catch (NotSetCookieException e) {
            logger.error("Cookie was not setted");
        }
    }
}
