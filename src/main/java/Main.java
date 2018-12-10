import java.io.IOException;

public class Main {
    private static final String PATTERN_CRFTOKEN = "\"csrfToken\":\"[^.\"]++\"";
    private static final String PATTERN_YANDEXUID = "yandexuid=\\d++";

    public static void main(String[] args) {
        HTTPRequestService httpRequestService = new HTTPRequestService();
        httpRequestService.setUrl("https://yandex.ru/maps");
        httpRequestService.setCookie("yandexuid=2030977811544445763");
        try {
            String response = httpRequestService.getRequest();
            System.out.println(response);
            String crfToken = httpRequestService.parseByRegExp(response, PATTERN_CRFTOKEN);
            String yandexuid = httpRequestService.parseByRegExp(response, PATTERN_YANDEXUID);
            if (null != crfToken) {
                System.out.println(crfToken);
            }
            if (null != yandexuid) {
                System.out.println(yandexuid);
            }
            httpRequestService.setUrl("https://yandex.ru/maps/api/search?text=Ижевск, карла маркса, 246&lang=ru_RU&csrfToken=" + crfToken);
            response = httpRequestService.getRequest();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
