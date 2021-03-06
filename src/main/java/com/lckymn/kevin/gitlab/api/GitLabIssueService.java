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
package com.lckymn.kevin.gitlab.api;

import java.util.List;

import com.lckymn.kevin.gitlab.json.GitLabIssue;
import com.lckymn.kevin.gitlab.json.GitLabIssue.GitLabIssueForCreation;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-31)
 */
public interface GitLabIssueService
{
  List<GitLabIssue> getAllIssues(String privateToken, Integer projectId);

  GitLabIssue createIssue(String privateToken, Integer projectId, GitLabIssueForCreation gitLabIssueForCreation);
}
