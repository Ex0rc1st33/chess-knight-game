package chesspuzzle.results.jaxb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class JAXBHelper {

    public static void toXML(Object o, OutputStream os) throws IOException, JAXBException {
        try (OutputStream out = os) {
            JAXBContext context = JAXBContext.newInstance(o.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(o, out);
        }
    }

    public static <T> T fromXML(Class<T> clazz, InputStream is) throws IOException, JAXBException {
        try (InputStream input = is) {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(input);
        }
    }

}
