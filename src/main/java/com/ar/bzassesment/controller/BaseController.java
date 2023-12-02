package com.ar.bzassesment.controller;

import java.security.Principal;

public class BaseController {

    //URL
    public static final String BASE_URL = "/api/v1";
    public String getUserName(Principal principal) {
        return principal.getName();
    }

}
