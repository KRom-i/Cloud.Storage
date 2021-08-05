package Config;


import java.io.FileInputStream;
import java.util.Properties;

public class Property {

    public static String get(String path, String property){

        try(FileInputStream fis = new FileInputStream(path);) {

            Properties appProps = new Properties();
            appProps.load(fis);

            return  appProps.getProperty(property);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
