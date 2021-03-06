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
package com.lckymn.kevin.trac2gitlab.impl;

import java.util.List;
import java.util.Map;

import com.lckymn.kevin.gitlab.json.GitLabIssue;
import com.lckymn.kevin.gitlab.json.GitLabIssue.GitLabIssueForCreation;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.gitlab.json.GitLabProject;
import com.lckymn.kevin.gitlab.json.GitLabUser;
import com.lckymn.kevin.trac.json.TracIssue;
import com.lckymn.kevin.trac2gitlab.Trac2GitLabIssueConverter;
import com.lckymn.kevin.trac2gitlab.Trac2GitLabUtil;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-12)
 */
public class Trac2GitLabIssueConverterImpl implements Trac2GitLabIssueConverter
{
  @Override
  public GitLabIssueForCreation convert(final GitLabProject gitLabProject, final GitLabUser assignee,
      final GitLabMilestone milestone, final Map<String, String> labelMap, final TracIssue tracIssue)
  {
    final List<String> labels = extractLabels(labelMap, tracIssue);

    final GitLabIssue.GitLabIssueForCreation gitLabIssueForCreation =
      new GitLabIssueForCreation(gitLabProject.id, tracIssue.getSummary(),
          Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracIssue.getDescription()), labels, assignee.id,
          milestone.id);
    return gitLabIssueForCreation;
  }

  @Override
  public List<String> extractLabels(final Map<String, String> labelMap, final TracIssue tracIssue)
  {
    return Trac2GitLabUtil.extractLabels(labelMap, tracIssue);
  }
}
