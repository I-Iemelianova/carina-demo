package com.qaprosoft.carina.demo.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public class CreateRepoMethod extends AbstractApiMethodV2 {

    public CreateRepoMethod() {
        super("api/users/_post/repo_rq.json", "api/users/_post/repo_rs.json");
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        request.header("Authorization", "Bearer " + R.TESTDATA.get("token"));
    }
}
