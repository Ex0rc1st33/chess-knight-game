package chesspuzzle.results.jaxb;

import java.io.*;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

/**
 * Helper class to work with JAXB.
 */
public class JAXBHelper {

    /**
     * Serializes an object to XML. The output document is written in UTF-8 encoding.
     *
     * @param o  the object to serialize
     * @param os the {@code OutputStream} to write to
     * @throws IOException   If any I/O errors occurred
     * @throws JAXBException if any problem occurs during serialization
     */
    public static void toXML(Object o, OutputStream os) throws IOException, JAXBException {
        try (os) {
            JAXBContext context = JAXBContext.newInstance(o.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(o, os);
        }
    }

    /**
     * Deserializes an object from XML.
     *
     * @param clazz the class of the object
     * @param is    the {@code InputStream} to read from
     * @param <T>   the type of the object deserialized from the XML
     * @return the resulting object
     * @throws IOException   If any I/O errors occurred
     * @throws JAXBException if any problem occurs during deserialization
     */
    public static <T> T fromXML(Class<T> clazz, InputStream is) throws IOException, JAXBException {
        try (is) {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(is);
        }
    }

}
