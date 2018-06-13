/*
 * Copyright 2018 The Data Transfer Project Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dataportabilityproject.transfer.todoist.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpTransport;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.UUID;
import org.dataportabilityproject.spi.transfer.provider.ImportResult;
import org.dataportabilityproject.spi.transfer.provider.ImportResult.ResultType;
import org.dataportabilityproject.spi.transfer.provider.Importer;
import org.dataportabilityproject.transfer.todoist.tasks.model.Project;
import org.dataportabilityproject.transfer.todoist.tasks.model.Task;
import org.dataportabilityproject.types.transfer.auth.AuthData;
import org.dataportabilityproject.types.transfer.auth.TokensAndUrlAuthData;
import org.dataportabilityproject.types.transfer.models.tasks.TaskContainerResource;
import org.dataportabilityproject.types.transfer.models.tasks.TaskListModel;
import org.dataportabilityproject.types.transfer.models.tasks.TaskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoistTasksImporter implements Importer<TokensAndUrlAuthData, TaskContainerResource> {

  private final Logger logger = LoggerFactory.getLogger(TodoistTasksImporter.class);
  private ObjectMapper objectMapper;
  private HttpTransport httpTransport;
  private volatile TodoistTasksService service;

  public TodoistTasksImporter(ObjectMapper objectMapper, HttpTransport httpTransport) {
    this.objectMapper = objectMapper;
    this.httpTransport = httpTransport;
    this.service = null;
  }

  @VisibleForTesting
  TodoistTasksImporter(TodoistTasksService service) {
    this.service = service;
  }

  @Override
  public ImportResult importItem(UUID jobId, TokensAndUrlAuthData authData,
      TaskContainerResource data) {
    try {
      for (TaskListModel taskListModel : data.getLists()) {
        importSingleProject(jobId, authData, taskListModel);
      }
      for (TaskModel taskModel : data.getTasks()) {
        importSingleTask(jobId, authData, taskModel);
      }
    } catch (IOException e) {
      return new ImportResult(ResultType.ERROR, e.getMessage());
    }
    return ImportResult.OK;
  }

  @VisibleForTesting
  void importSingleProject(UUID jobId, TokensAndUrlAuthData authData, TaskListModel taskListModel)
      throws IOException {

  }

  @VisibleForTesting
  void importSingleTask(UUID jobId, TokensAndUrlAuthData authData, TaskModel taskModel) throws IOException {

  }

  private static Project convertToTodoistProject(TaskListModel taskListModel) {
    return null;
  }

  private static Task convertToTodoistTask(TaskModel taskModel) {
    return null;
  }

  private TodoistTasksService getOrCreateService(AuthData authData) {
    Preconditions.checkArgument(authData instanceof TokensAndUrlAuthData);
    return service == null ? createService((TokensAndUrlAuthData) authData) : service;
  }

  private TodoistTasksService createService(TokensAndUrlAuthData authData) {
    return new TodoistTasksService(httpTransport, objectMapper, authData);
  }
}
