package com.qaprosoft.carina.demo.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;

public class FetchInfoByUsernameBlankMethod extends AbstractApiMethodV2 {

    public FetchInfoByUsernameBlankMethod(String username) {
        super(null, "api/users/_get/not_found_rs.json");
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("username", username);
    }
}
