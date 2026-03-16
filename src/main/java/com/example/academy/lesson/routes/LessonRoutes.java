package com.example.academy.lesson.routes;

import com.example.academy.base.routes.BaseRoutes;

public class LessonRoutes {
    private static final String ROOT = BaseRoutes.API + "/lesson";
    public static final String CREATE = ROOT;
    public static final String BY_ID = ROOT + "/{id}";
    public static final String EDIT = BY_ID;
    public static final String SEARCH = ROOT;
}
