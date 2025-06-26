package com.pijieh.personalsite.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class LoginForm {
    @NonNull
    String username;
    @NonNull
    String password;
}
