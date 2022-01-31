package com.qaprosoft.carina.demo.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public class DeleteIssueMethod extends AbstractApiMethodV2 {

    public DeleteIssueMethod(String owner, String repo, int issue_number){
        super( "api/users/_post/issue_rs.json", null);
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("repo", repo);
        replaceUrlPlaceholder("owner", owner);
        replaceUrlPlaceholder("issue_number", String.valueOf(issue_number));
        request.header("Authorization", "Bearer " + R.TESTDATA.get("token"));
    }
}