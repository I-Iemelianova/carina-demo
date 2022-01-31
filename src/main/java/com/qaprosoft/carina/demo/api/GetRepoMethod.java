package com.qaprosoft.carina.demo.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public class GetRepoMethod extends AbstractApiMethodV2 {

    public GetRepoMethod(String owner, String repo) {
        super(null, "api/users/_get/repo_rs.json");
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("repo", repo);
        replaceUrlPlaceholder("owner", owner);
        request.header("Authorization", "Bearer " + R.TESTDATA.get("token"));
    }
}