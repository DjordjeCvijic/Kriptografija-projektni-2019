package get_properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;


public class GetConfigPropertyValues {




    public static String getPropValue(String propertyName) {
        String result = "";
        try {

            String propertyFileName = "src" + File.separator + "resources" + File.separator + "config.properties";
            Properties property = new Properties();
            BufferedReader br = new BufferedReader(new FileReader(propertyFileName));
            property.load(br);
            result = property.getProperty(propertyName);
            br.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;

    }


}

