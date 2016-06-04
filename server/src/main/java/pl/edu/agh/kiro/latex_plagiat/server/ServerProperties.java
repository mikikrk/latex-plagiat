package pl.edu.agh.kiro.latex_plagiat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServerProperties {
    private static final Logger log = LoggerFactory.getLogger(ServerProperties.class);
    public static String DOCS_PATH = "./docs";
    public static int NUMBER_OF_RESULTS = 20;
    public static double MIN_WORD_SIMILARITY_PERCENTAGE = 50;
    public static int PAGES_TO_SEARCH = 1;

    static {
        loadConfiguration();
    }


    private static void loadConfiguration() {
        Properties mainProperties = tryLoadProperties();

        if (mainProperties != null) {
            readPropertiesValues(mainProperties);
        }
    }

    private static Properties tryLoadProperties() {
        String configPath = "./configuration.properties";
        Properties mainProperties = new Properties();
        FileInputStream file;
        try {
            file = new FileInputStream(configPath);
            mainProperties.load(file);
            file.close();
        } catch (IOException e) {
            log.debug("configuration.properties file not found!");
            return null;
        }
        return mainProperties;
    }

    private static void readPropertiesValues(Properties mainProperties) {
        List<Field> staticFields = getStaticFields();
        for (Field property : staticFields) {
            String propertyName = property.getName();
            String value = (String) mainProperties.get(propertyName);

            if (value != null) {
                setPropertyValue(property, value);
            }
        }
    }

    private static List<Field> getStaticFields() {
        Field[] declaredFields = ServerProperties.class.getDeclaredFields();
        List<Field> staticFields = new ArrayList<>();
        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                staticFields.add(field);
            }
        }
        return staticFields;
    }

    private static void setPropertyValue(Field property, String value) {
        try {
            if (trySetInteger(property, value)) return;
            if (trySetDouble(property, value)) return;
            if (trySetBoolean(property, value)) return;
            if (trySetChar(property,value)) return;
            setString(property, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static boolean trySetInteger(Field property, String value) throws IllegalAccessException {
        try {
            int intVal = Integer.parseInt(value);
            property.set(null, intVal);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    private static boolean trySetDouble(Field property, String value) throws IllegalAccessException {
        try {
            double doubleVal = Double.parseDouble(value);
            property.set(null, doubleVal);
            return true;
        } catch (NumberFormatException e1) {
        }
        return false;
    }

    private static boolean trySetBoolean(Field property, String value) throws IllegalAccessException {
        String lowerCaseValue = value.toLowerCase();
        if ("true".equals(lowerCaseValue) || "false".equals(lowerCaseValue)) {
            boolean boolVal = Boolean.parseBoolean(lowerCaseValue);
            property.set(null, boolVal);
            return true;
        } else {
            return false;
        }
    }

    private static void setString(Field property, String value) throws IllegalAccessException {
        property.set(null, value);
    }

    private static boolean trySetChar(Field property, String value) throws IllegalAccessException {
        if(value.length() == 1) {
            property.set(null, value.charAt(0));
            return true;
        } else {
            return false;
        }
    }
}
