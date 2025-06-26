package com.pijieh.personalsite.models;

import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Blog {
    @NonNull
    Integer id;
    @NonNull
    String title;
    @NonNull
    String postBody;
    @NonNull
    String createdBy;
    @NonNull
    OffsetDateTime createdAt;
}
