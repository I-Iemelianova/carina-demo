package com.qaprosoft.carina.demo.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public class CreateIssueMethod extends AbstractApiMethodV2 {

    public CreateIssueMethod(String owner, String repo){
        super("api/users/_post/issue_rq.json","api/users/_post/issue_rs.json");
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("repo", repo);
        replaceUrlPlaceholder("owner", owner);
        request.header("Authorization", "Bearer " + R.TESTDATA.get("token"));
    }

    public CreateIssueMethod(String owner, String repo, String path){
        super("api/users/_post/issue_rq.json",path);
        replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
        replaceUrlPlaceholder("repo", repo);
        replaceUrlPlaceholder("owner", owner);
        request.header("Authorization", "Bearer " + R.TESTDATA.get("token"));
    }
}