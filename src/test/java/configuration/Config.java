package configuration;

public class Config {
    public static final String BASE_URL ="https://qa-stellarburgers.education-services.ru";
    //Метод генерации уникального email
    public static String uniqueEmail() {
        return "peace_data_" + System.currentTimeMillis() + "@yandex.ru";
    }
}
