/**
 * Copyright 2013 Lee, Seong Hyun (Kevin)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lckymn.kevin.gitlab.api.impl;

import static com.lckymn.kevin.gitlab.api.GitLabApiUtil.*;
import static org.elixirian.kommonlee.util.Objects.*;

import java.util.Arrays;
import java.util.List;

import org.elixirian.jsonstatham.core.JsonStatham;

import com.lckymn.kevin.gitlab.api.GitLabProjectService;
import com.lckymn.kevin.gitlab.json.GitLabProject;
import com.lckymn.kevin.http.HttpRequestForJsonSource;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-31)
 */
public class GitLabProjectServiceImpl implements GitLabProjectService
{
  private final HttpRequestForJsonSource httpRequestForJsonSource;
  private final JsonStatham jsonStatham;
  private final String url;

  public GitLabProjectServiceImpl(final HttpRequestForJsonSource httpRequestForJsonSource,
      final JsonStatham jsonStatham, final String url)
  {
    this.httpRequestForJsonSource = httpRequestForJsonSource;
    this.jsonStatham = jsonStatham;
    this.url = buildApiUrlForProjects(url);
  }

  @Override
  public List<GitLabProject> getAllGitLabProjects(final String privateToken)
  {
    final String result = httpRequestForJsonSource.get(prepareUrl(url, privateToken))
        .body();

    final GitLabProject[] gitLabProjects = jsonStatham.convertFromJson(GitLabProject[].class, result);
    return Arrays.asList(gitLabProjects);
  }

  @Override
  public GitLabProject getProjectByPathWithNamespace(final String privateToken, final String pathWithNamespace)
  {
    final List<GitLabProject> gitLabProjectList = getAllGitLabProjects(privateToken);

    for (final GitLabProject gitLabProject : gitLabProjectList)
    {
      if (equal(gitLabProject.pathWithNamespace, pathWithNamespace))
      {
        return gitLabProject;
      }
    }
    return GitLabProject.EMPTY_GIT_LAB_PROJECT;
  }
}