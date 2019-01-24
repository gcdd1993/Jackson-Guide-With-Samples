package com.gcdd.jacksonGuide.advancedJacksonMarshalling.jacksonDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author: gaochen
 * Date: 2019/1/24
 */
public class SimpleTest {
    @Test
    public void whenSerializingDateWithJackson_thenSerializedToTimestamp() throws JsonProcessingException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = df.parse("01-01-1970 01:00");
        Event event = new Event("party", date);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(event);
    }

    @Test
    public void whenSerializingDateToISO8601_thenSerializedToText()
            throws JsonProcessingException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        String toParse = "01-01-1970 02:30";
        Date date = df.parse(toParse);
        Event event = new Event("party", date);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // StdDateFormat is ISO8601 since jackson 2.9
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        String result = mapper.writeValueAsString(event);
        assertThat(result, containsString("1970-01-01T02:30:00.000+00:00"));
    }

    @Test
    public void whenSettingObjectMapperDateFormat_thenCorrect()
            throws JsonProcessingException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");

        String toParse = "20-12-2014 02:30";
        Date date = df.parse(toParse);
        Event event = new Event("party", date);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);

        String result = mapper.writeValueAsString(event);
        assertThat(result, containsString(toParse));
    }

    @Test
    public void whenUsingJsonFormatAnnotationToFormatDate_thenCorrect()
            throws JsonProcessingException, ParseException {

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        String toParse = "20-12-2014 02:30:00";
        Date date = df.parse(toParse);
        Event event = new Event("party", date);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(event);
        assertThat(result, containsString(toParse));
    }

    @Test
    public void whenUsingCustomDateSerializer_thenCorrect()
            throws JsonProcessingException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        String toParse = "20-12-2014 02:30:00";
        Date date = df.parse(toParse);
        Event event = new Event("party", date);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(event);
        assertThat(result, containsString(toParse));
    }

    @Test
    public void whenSerializingJodaTime_thenCorrect()
            throws JsonProcessingException {
        DateTime date = new DateTime(2014, 12, 20, 2, 30,
                DateTimeZone.forID("Europe/London"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String result = mapper.writeValueAsString(date);
        assertThat(result, containsString("2014-12-20T02:30:00.000Z"));
    }

    @Test
    public void whenSerializingJodaTimeWithJackson_thenCorrect()
            throws JsonProcessingException {
        DateTime date = new DateTime(2014, 12, 20, 2, 30);
        JodoEvent event = new JodoEvent("party", date);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(event);
        assertThat(result, containsString("2014-12-20 02:30"));
    }

    @Test
    public void whenSerializingJava8Date_thenCorrect()
            throws JsonProcessingException {
        LocalDateTime date = LocalDateTime.of(2014, 12, 20, 2, 30);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String result = mapper.writeValueAsString(date);
        assertThat(result, containsString("2014-12-20T02:30"));
    }

    @Test
    public void whenSerializingJava8DateWithCustomSerializer_thenCorrect()
            throws JsonProcessingException {

        LocalDateTime date = LocalDateTime.of(2014, 12, 20, 2, 30);
        Java8Event event = new Java8Event("party", date);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(event);
        assertThat(result, containsString("2014-12-20 02:30"));
    }

    @Test
    public void whenDeserializingDateWithJackson_thenCorrect()
            throws JsonProcessingException, IOException {

        String json = "{\"name\":\"party\",\"date\":\"20-12-2014 02:30:00\"}";

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);

        Event event = mapper.readerFor(Event.class).readValue(json);
        assertEquals("20-12-2014 02:30:00", df.format(event.getDate()));
    }

    @Test
    public void whenDeserialisingZonedDateTimeWithDefaults_thenNotCorrect()
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
        String converted = objectMapper.writeValueAsString(now);

        ZonedDateTime restored = objectMapper.readValue(converted, ZonedDateTime.class);
        System.out.println("serialized: " + now);
        System.out.println("restored: " + restored);
        assertThat(now, is(restored));
    }

    @Test
    public void whenDeserializingDateUsingCustomDeserializer_thenCorrect()
            throws JsonProcessingException, IOException {

        String json = "{\"name\":\"party\",\"date\":\"20-12-2014 02:30:00\"}";

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        ObjectMapper mapper = new ObjectMapper();

        Event event = mapper.readerFor(Event.class).readValue(json);
        assertEquals("20-12-2014 02:30:00", df.format(event.getDate()));
    }
}
