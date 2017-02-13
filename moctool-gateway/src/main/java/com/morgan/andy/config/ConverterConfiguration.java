package com.morgan.andy.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.morgan.andy.moc.automata.NfaToDfaConverter;
import com.morgan.andy.moc.automata.NfaToReConverter;
import com.morgan.andy.moc.automata.REToNfaConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class ConverterConfiguration {
    @Bean
    public NfaToDfaConverter nfaToDfaConverter() {
        return new NfaToDfaConverter();
    }

    @Bean
    public REToNfaConverter reToNfaConverter() {
        return new REToNfaConverter();
    }

    @Bean
    public CustomJsonDateDeserializer customJsonDateDeserializer() {
        return new CustomJsonDateDeserializer();
    }

    @Bean
    public NfaToReConverter nfaToReConverter() { return new NfaToReConverter(); }

    public class CustomJsonDateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser jsonparser,
                                DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            String date = jsonparser.getText();
            date = date.substring(0, date.length()-1);
            try {
                return format.parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
