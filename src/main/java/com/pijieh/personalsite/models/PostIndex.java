package com.pijieh.personalsite.models;

import java.time.OffsetDateTime;
import lombok.NonNull;
import lombok.Value;

/**
 * The form model for the (id, created_at) index.
 *
 * @author patrickijieh
 */
@Value
public class PostIndex {
    @NonNull
    Integer id;
    @NonNull
    OffsetDateTime createdAt;
}
