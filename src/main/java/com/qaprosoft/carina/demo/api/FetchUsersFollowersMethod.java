package com.qaprosoft.carina.demo.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;

public class FetchUsersFollowersMethod extends AbstractApiMethodV2 {

    public FetchUsersFollowersMethod(String username) {
        super(null, "api/users/_delete/rs.json");
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("username", username);
    }
}
