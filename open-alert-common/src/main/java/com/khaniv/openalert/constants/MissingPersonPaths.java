package com.khaniv.openalert.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MissingPersonPaths {
    public final String CONTROLLER = "v1/person";
    public final String FIND_LOST = "/lost";
    public final String FIND_SEEN = "/seen";
    public final String ID = "/{id}";
    public final String UPDATE_DESCRIPTION = "/description";
    public final String UPDATE_STATUS = "/status";
    public final String ACTIVATE = "/activate" + ID;
    public final String INACTIVATE = "/inactivate" + ID;
}
