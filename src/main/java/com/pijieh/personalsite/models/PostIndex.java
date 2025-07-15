package com.pijieh.personalsite.models;

import java.time.OffsetDateTime;
import lombok.NonNull;
import lombok.Value;

@Value
public class PostIndex {
    @NonNull
    Integer id;
    @NonNull
    OffsetDateTime createdAt;
}
