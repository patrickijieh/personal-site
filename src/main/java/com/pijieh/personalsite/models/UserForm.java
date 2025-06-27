package com.pijieh.personalsite.models;

import lombok.NonNull;
import lombok.Value;

@Value
public class UserForm {
    @NonNull
    String username;
    @NonNull
    String password;
}
