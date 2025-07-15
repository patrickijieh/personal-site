package com.pijieh.personalsite.models;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class BlogPost {
    @NonNull
    Integer id;
    @NonNull
    String title;
    String postBody;
    @NonNull
    String createdBy;
    @NonNull
    OffsetDateTime createdAt;
}
