package org.web.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class FileUtil {
    private static ObjectMapper objectMapper = null;

    private FileUtil() {
    }

    public static ObjectMapper getObjectMapperInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public static Object readJsonValue(Object value, Class<?> httpRequestClass) {
        try {
            ObjectMapper objectMapperInstance = FileUtil.getObjectMapperInstance();
            return objectMapperInstance.readValue((String) value, httpRequestClass);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException("Read json fail");
        }
    }

    public static Object readXMLValue(Object value, Class<?> httpRequestClass) {
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(httpRequestClass);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller.unmarshal(new StringReader((String)value));
        } catch (JAXBException e) {
            System.out.println(e);
            throw new RuntimeException("Read xml fail");
        }
    }

    public static String writeJsonValue(Object value) {
        try {
            ObjectMapper objectMapperInstance = FileUtil.getObjectMapperInstance();
            return objectMapperInstance.writeValueAsString(value);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException("Write json fail");
        }
    }

    public static String writeXMLValue(Object value, Class<?> httpRequestClass) {
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(httpRequestClass);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter writer = new StringWriter();
            marshaller.marshal(value, writer);
            return writer.toString();
        } catch (JAXBException e) {
            System.out.println(e);
            throw new RuntimeException("Write xml fail");
        }
    }
}
