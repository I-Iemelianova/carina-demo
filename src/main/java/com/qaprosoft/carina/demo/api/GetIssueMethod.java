package com.qaprosoft.carina.demo.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public class GetIssueMethod extends AbstractApiMethodV2 {

    public GetIssueMethod(String owner, String repo, int issue_number){
        super(null, "api/users/_post/issue_rs.json");
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("repo", repo);
        replaceUrlPlaceholder("owner", owner);
        replaceUrlPlaceholder("issue_number", String.valueOf(issue_number));
        request.header("Authorization", "Bearer " + R.TESTDATA.get("token"));
    }
}
