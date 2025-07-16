package com.pijieh.personalsite.models;

import lombok.NonNull;
import lombok.Value;

/**
 * The form model for a user creating a blog post.
 *
 * @author patrickijieh
 */
@Value
public class BlogForm {
    @NonNull
    String postTitle;
    @NonNull
    String postBody;
}
