package com.qaprosoft.carina.demo;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import com.qaprosoft.carina.core.foundation.report.testrail.TestRailCases;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import io.restassured.path.json.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.IAbstractTest;
import com.qaprosoft.carina.core.foundation.api.http.HttpResponseStatusType;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import com.qaprosoft.carina.demo.api.*;

public class APIGitHubTest implements IAbstractTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final static String USERNAME = R.TESTDATA.get("username");
    private final static String RANDOM_USERNAME = R.TESTDATA.get("random_username");
    private final static String REPO_NAME = R.TESTDATA.get("repo_name");
    private final static String DELETE_REPO_RQ = Configuration.getEnvArg("api_url") + "/repos/" + R.TESTDATA.get("username") + "/" + R.TESTDATA.get("repo_name");
    private final static String POST_ISSUE_RQ = Configuration.getEnvArg("api_url") + "/repos/" + R.TESTDATA.get("username") + "/" + R.TESTDATA.get("repo_name") + "/issues";

    @TestRailCases(testCasesId = "1")
    @Test(description = "User is able to get info about user by username")
    @MethodOwner(owner = "iiemelianova")
    public void fetchInfoByUsernameTest() {
        FetchInfoByUsernameMethod infoUser = new FetchInfoByUsernameMethod(USERNAME);
        infoUser.expectResponseStatus(HttpResponseStatusType.OK_200);
        infoUser.callAPI();
        infoUser.validateResponse();
    }

    @TestRailCases(testCasesId = "2")
    @Test(description = "User can't get info about user that doesn't exist")
    @MethodOwner(owner = "iiemelianova")
    public void randomUsernameTest() {
        FetchInfoByUsernameBlankMethod blankInfo = new FetchInfoByUsernameBlankMethod(RANDOM_USERNAME);
        blankInfo.expectResponseStatus(HttpResponseStatusType.NOT_FOUND_404);
        blankInfo.callAPI();
        blankInfo.validateResponse();
        }

    @TestRailCases(testCasesId = "3")
    @Test(description = "User is able to get info about user's followers by username")
    @MethodOwner(owner = "iiemelianova")
    public void getFollowersTest() {
        FetchUsersFollowersMethod followers = new FetchUsersFollowersMethod(USERNAME);
        followers.expectResponseStatus(HttpResponseStatusType.OK_200);
        followers.callAPI();
    }

    @TestRailCases(testCasesId = "4,5")
    @Test(description = "User is able to create a new repository.")
    @MethodOwner(owner = "iiemelianova")
    public void createAndValidateRepoTest() {
//        C4
        CreateRepoMethod createRepo = new CreateRepoMethod();
        createRepo.expectResponseStatus(HttpResponseStatusType.CREATED_201);
        createRepo.callAPI();
        createRepo.validateResponseAgainstSchema("api/users/_post/repo_rs.schema");
        LOGGER.info(REPO_NAME + " repo was successfully created!");
//        C5
        GetRepoMethod getRepo = new GetRepoMethod(USERNAME, REPO_NAME);
        getRepo.expectResponseStatus(HttpResponseStatusType.OK_200);
        getRepo.callAPI();
        getRepo.validateResponse();
    }

    @TestRailCases(testCasesId = "6")
    @Test(description = "User is able to delete own repository by name.")
    @MethodOwner(owner = "iiemelianova")
    public void deleteRepoTest() throws IOException {
        GetResponseCode code = new GetResponseCode();
        int responseCode = code.getResponseCode(DELETE_REPO_RQ);
        if (responseCode == HttpResponseStatusType.NOT_FOUND_404.getCode()) {
            DeleteRepoMethod deleteInvalid = new DeleteRepoMethod(USERNAME, REPO_NAME, "api/users/_delete/not_found_rs.json");
            deleteInvalid.expectResponseStatus(HttpResponseStatusType.NOT_FOUND_404);
            deleteInvalid.callAPI();
            deleteInvalid.validateResponse();
            Assert.fail("There is no repo by name " + REPO_NAME + ", please, make sure you are using a valid repo name!");
        }
        else {
            DeleteRepoMethod deleteRepo = new DeleteRepoMethod(USERNAME, REPO_NAME);
            deleteRepo.expectResponseStatus(HttpResponseStatusType.NO_CONTENT_204);
            deleteRepo.callAPI();
            LOGGER.info(REPO_NAME + " repo was successfully deleted!");
        }
    }

    @TestRailCases(testCasesId = "7,8,9,10")
    @Test(description = "User is able to create an issue for the repository by it's name")
    @MethodOwner(owner = "iiemelianova")
    public void validateIssueTest() throws IOException {
//        C7
        GetResponseCode code = new GetResponseCode();
        int responseCode = code.getResponseCode(POST_ISSUE_RQ);
        if (responseCode == HttpResponseStatusType.NOT_FOUND_404.getCode()) {
            CreateIssueMethod createInvalid = new CreateIssueMethod(USERNAME, REPO_NAME, "api/users/_post/not_found_issue_rs.json");
            createInvalid.expectResponseStatus(HttpResponseStatusType.NOT_FOUND_404);
            createInvalid.callAPI();
            createInvalid.validateResponse();
            Assert.fail("There is no repo by name " + REPO_NAME + ", please, make sure you are using a valid repo name!");
        }
        CreateIssueMethod createIssue = new CreateIssueMethod(USERNAME, REPO_NAME);
        createIssue.expectResponseStatus(HttpResponseStatusType.CREATED_201);
        createIssue.callAPI();
        createIssue.validateResponse();
        LOGGER.info("Issue was successfully created!");
//        C8
        String rs = createIssue.callAPI().asString();
        JsonPath jsonPath = new JsonPath(rs);
        String titleBeforeEditing = jsonPath.getString("title");
        int issueNumber = jsonPath.getInt("number");

        EditIssueMethod editIssue = new EditIssueMethod(USERNAME, REPO_NAME, issueNumber);
        editIssue.expectResponseStatus(HttpResponseStatusType.OK_200);
        editIssue.callAPI();
        LOGGER.info("Issue title was edited successfully!");
//        C9
        GetIssueMethod getIssue = new GetIssueMethod(USERNAME, REPO_NAME, issueNumber);
        getIssue.expectResponseStatus(HttpResponseStatusType.OK_200);
        getIssue.callAPI();
        getIssue.validateResponse();
        LOGGER.info("Issue was successfully gathered!");

        String rsEdited = getIssue.callAPI().asString();
        JsonPath jsonPathEdited = new JsonPath(rsEdited);
        String actualTitle = jsonPathEdited.getString("title");
        Assert.assertNotEquals(actualTitle, titleBeforeEditing, String.format("Something went wrong! Seams, like title wasn't changed after all, title before editing was '%s' and found '%s'", titleBeforeEditing, actualTitle));
//        C10
        DeleteIssueMethod deleteIssue = new DeleteIssueMethod(USERNAME, REPO_NAME, issueNumber);
        deleteIssue.expectResponseStatus(HttpResponseStatusType.NO_CONTENT_204);
        deleteIssue.callAPI();
        LOGGER.info("Issue was successfully deleted!");
    }
}
