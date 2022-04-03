package ch.heigvd.app.utils;

/**
 * JsonConverter class
 */
public class JsonConverter {

    public static JavaConfig convert(String input){
        JavaConfig javaConf = new JavaConfig("","","");
        return javaConf;
    }
}

/**
 * Mimics the config.json structure in order map its data
 * into a java object.
 */
class JavaConfig {
    public JavaConfig(String title, String lang, String charset){
        this.title = title;
        this.lang = lang;
        this.charset = charset;
    }
    String title;
    String lang;
    String charset;
}