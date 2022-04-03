package ch.heigvd.app.utils;

import com.google.gson.Gson;

/**
 * JsonConverter class
 */
public class JsonConverter {
    /**
     * Convert a json string into a java object
     * @param input the json string
     * @return the java object
     * @see <a href="https://github.com/google/gson">gson</a>
     */
    public static JavaConfig convert(String input){
        Gson gson = new Gson();
        JavaConfig javaConf = gson.fromJson(input, JavaConfig.class);

        return javaConf;
    }
}

/**
 * Mimics the config.json structure in order map its data
 * into a java object.
 */
class JavaConfig {
    /**
     * Constructor for test purpose
     * @param title a title
     * @param lang a language (ex: fr)
     * @param charset a charset (ex: utf-8)
     */
    public JavaConfig(String title, String lang, String charset){
        this.title = title;
        this.lang = lang;
        this.charset = charset;
    }
    String title;
    String lang;
    String charset;
}