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
import static org.elixirian.kommonlee.util.collect.Lists.*;
import static org.elixirian.kommonlee.util.collect.Maps.*;
import static org.elixirian.kommonlee.util.collect.Sets.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elixirian.jsonstatham.core.JsonStatham;

import com.lckymn.kevin.gitlab.api.GitLabMilestoneService;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.gitlab.json.GitLabMilestone.GitLabMilestoneForCreation;
import com.lckymn.kevin.http.HttpRequestForJsonSource;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-31)
 */
public class GitLabMilestoneServiceImpl implements GitLabMilestoneService
{
  private final HttpRequestForJsonSource httpRequestForJsonSource;
  private final JsonStatham jsonStatham;
  private final String url;

  public GitLabMilestoneServiceImpl(final HttpRequestForJsonSource httpRequestForJsonSource,
      final JsonStatham jsonStatham, final String url)
  {
    this.httpRequestForJsonSource = httpRequestForJsonSource;
    this.jsonStatham = jsonStatham;
    this.url = buildApiUrlForProjects(url);
  }

  HttpRequestForJsonSource getHttpRequestForJsonSource()
  {
    return httpRequestForJsonSource;
  }

  JsonStatham getJsonStatham()
  {
    return jsonStatham;
  }

  String getUrl()
  {
    return url;
  }

  @Override
  public List<GitLabMilestone> getAllGitLabMilestones(final String privateToken, final Long projectId)
  {
    final String result = httpRequestForJsonSource.get(prepareUrlForMilestones(url, privateToken, projectId))
        .body();
    final GitLabMilestone[] gitLabMilestones = getResultOrThrowException(jsonStatham, GitLabMilestone[].class, result);
    return Arrays.asList(gitLabMilestones);
  }

  @Override
  public GitLabMilestone createMilestone(final String privateToken, final Long projectId,
      final GitLabMilestoneForCreation gitLabMilestoneForCreation)
  {
    final Map<String, String> form = newHashMapWithInitialCapacity(3);
    form.put("title", gitLabMilestoneForCreation.title);
    form.put("description", gitLabMilestoneForCreation.description);
    form.put("due_date", gitLabMilestoneForCreation.getDueDateInUtcString());
    final String result = httpRequestForJsonSource.post(prepareUrlForMilestones(url, privateToken, projectId))
        .form(form)
        .body();
    final GitLabMilestone gitLabMilestone = getResultOrThrowException(jsonStatham, GitLabMilestone.class, result);
    return gitLabMilestone;
  }

  @Override
  public List<GitLabMilestone> createMilestonesIfNotExist(final String privateToken, final Long projectId,
      final List<GitLabMilestoneForCreation> gitLabMilestoneForCreationList)
  {
    final Set<GitLabMilestoneForCreation> gitLabMilestoneForCreationListWithUniqueTitle = newHashSet();
    for (final GitLabMilestoneForCreation gitLabMilestoneForCreation : gitLabMilestoneForCreationList)
    {
      boolean duplicateFound = false;
      for (final GitLabMilestoneForCreation uniqueGitLabMilestoneForCreation : gitLabMilestoneForCreationListWithUniqueTitle)
      {
        if (gitLabMilestoneForCreation.title.equalsIgnoreCase(uniqueGitLabMilestoneForCreation.title))
        {
          duplicateFound = true;
          break;
        }
      }
      if (!duplicateFound)
      {
        gitLabMilestoneForCreationListWithUniqueTitle.add(gitLabMilestoneForCreation);
      }
    }

    final List<GitLabMilestoneForCreation> gitLabMilestoneToAdd = newArrayList();
    final List<GitLabMilestone> gitLabMilestones = getAllGitLabMilestones(privateToken, projectId);
    for (final GitLabMilestoneForCreation gitLabMilestoneForCreation : gitLabMilestoneForCreationListWithUniqueTitle)
    {
      boolean titleFound = false;
      for (final GitLabMilestone gitLabMilestone : gitLabMilestones)
      {
        if (gitLabMilestoneForCreation.title.equalsIgnoreCase(gitLabMilestone.title))
        {
          titleFound = true;
          break;
        }
      }
      if (!titleFound)
      {
        gitLabMilestoneToAdd.add(gitLabMilestoneForCreation);
      }
    }

    final int size = gitLabMilestoneToAdd.size();
    if (0 == size)
    {
      return Collections.emptyList();
    }
    final List<GitLabMilestone> addedGitLabMilestoneList = newArrayListWithInitialCapacity(size);
    for (final GitLabMilestoneForCreation gitLabMilestoneForCreation : gitLabMilestoneToAdd)
    {
      addedGitLabMilestoneList.add(createMilestone(privateToken, projectId, gitLabMilestoneForCreation));
    }
    return Collections.unmodifiableList(addedGitLabMilestoneList);
  }
}
