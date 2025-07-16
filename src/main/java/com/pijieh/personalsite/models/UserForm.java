package com.pijieh.personalsite.models;

import lombok.NonNull;
import lombok.Value;

/**
 * The form model for a user creating another user.
 *
 * @author patrickijieh
 */
@Value
public class UserForm {
    @NonNull
    String username;
    @NonNull
    String password;
}
