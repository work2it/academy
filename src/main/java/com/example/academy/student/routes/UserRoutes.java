package com.example.academy.student.routes;

import com.example.academy.base.routes.BaseRoutes;

public class UserRoutes {
    public static final String ROOT = BaseRoutes.API + "/student";
    public static final String REG = BaseRoutes.NOT_SECURED + "/v1/reg";
    public static final String EDIT = ROOT;
    public static final String SEARCH = ROOT;
    public static final String BY_ID = ROOT + "/{id}";
}
