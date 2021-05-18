package chesspuzzle.results.jaxb;

import java.time.LocalDateTime;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter class to convert {@code LocalDateTime} objects to {@code String} objects and vice versa,
 * to help serializing objects to XML, and deserializing objects from XML.
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    /**
     * {@return the {@code LocalDateTime} object corresponding to the specified {@code String}}
     *
     * @param s the {@code String} object to be converted
     */
    @Override
    public LocalDateTime unmarshal(String s) {
        return LocalDateTime.parse(s);
    }

    /**
     * {@return the {@code String} object corresponding to the specified {@code LocalDateTime}}
     *
     * @param localDateTime the {@code LocalDateTime} object to be converted
     */
    @Override
    public String marshal(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

}
