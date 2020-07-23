package com.khaniv.openalert.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MatchPaths {
    public final String CONTROLLER = "v1/match";
    public final String ID = "/{id}";
    public final String FIND_BY_LOST_PERSON_ID = "/lost/{lostPersonId}";
    public final String FIND_BY_SEEN_PERSON_ID = "/seen/{seenPersonId}";
    public final String SAVE_ALL = "/all";
    public final String UPDATE_USER_STATUS = "/status/user";
    public final String UPDATE_OPERATOR_STATUS = "/status/operator";
    public final String ACTIVATE = "/activate" + ID;
    public final String INACTIVATE = "/inactivate" + ID;
    public final String VIEWED_BY_USER = "/viewed/user" + ID;
    public final String VIEWED_BY_OPERATOR = "/viewed/operator" + ID;
}
