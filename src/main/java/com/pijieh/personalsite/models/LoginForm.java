package com.pijieh.personalsite.models;

import lombok.NonNull;
import lombok.Value;

/**
 * The form model for a user logging in.
 *
 * @author patrickijieh
 */
@Value
public class LoginForm {
    @NonNull
    String username;
    @NonNull
    String password;
}
