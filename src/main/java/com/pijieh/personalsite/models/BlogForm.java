package com.pijieh.personalsite.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class BlogForm {
    @NonNull
    String postTitle;
    @NonNull
    String postBody;
    @NonNull
    String createdBy;
}
