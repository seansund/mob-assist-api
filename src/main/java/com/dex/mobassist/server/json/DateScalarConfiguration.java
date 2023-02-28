package com.dex.mobassist.server.json;

import graphql.language.StringValue;
import graphql.schema.*;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DateScalarConfiguration {

    @Bean
    public RuntimeWiringConfigurer configurer() {
        final String dateFormat = "MM/dd/yyyy";

        final GraphQLScalarType dateScalar = GraphQLScalarType.newScalar()
                .name("Date")
                .description("Java Date as scalar")
                .coercing(new Coercing<Date, String>() {
                    @Override
                    public String serialize(final @NonNull Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof Date) {
                            return new SimpleDateFormat(dateFormat).format(dataFetcherResult);
                        } else {
                            throw new CoercingSerializeException("Date object expected.");
                        }
                    }

                    @Override
                    public @NonNull Date parseValue(final @NonNull Object input) throws CoercingParseValueException {
                        try {
                            if (input instanceof String) {
                                return new SimpleDateFormat(dateFormat).parse((String)input);
                            } else {
                                throw new CoercingParseValueException("String expected.");
                            }
                        } catch (ParseException ex) {
                            throw new CoercingParseValueException(String.format("Date format is not valid: '%s'", input), ex);
                        }
                    }

                    @Override
                    public @NonNull Date parseLiteral(final @NonNull Object input) throws CoercingParseLiteralException {
                        if (input instanceof StringValue) {
                            try {
                                return new SimpleDateFormat(dateFormat).parse(((StringValue)input).getValue());
                            } catch (ParseException ex) {
                                throw new CoercingParseLiteralException(ex);
                            }
                        } else {
                            throw new CoercingParseLiteralException("StringValue expected.");
                        }
                    }
                })
                .build();

        return (builder) -> builder.scalar(dateScalar);
    }
}
