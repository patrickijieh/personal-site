package com.pijieh.personalsite.helpers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.OffsetDateTime;

public class OffsetDateTimeAdapter extends TypeAdapter<OffsetDateTime> {
    @Override
    public void write(final JsonWriter jsonWriter,
            final OffsetDateTime localDate) throws IOException {
        jsonWriter.value(localDate.toString());
    }

    @Override
    public OffsetDateTime read(final JsonReader jsonReader) throws IOException {
        return OffsetDateTime.parse(jsonReader.nextString());
    }
}
