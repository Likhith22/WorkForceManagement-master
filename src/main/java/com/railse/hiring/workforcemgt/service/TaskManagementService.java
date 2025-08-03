package com.railse.hiring.workforcemgt.service;

import com.railse.hiring.workforcemgt.dto.*;


import java.util.List;


public interface TaskManagementService {
   List<TaskManagementDto> createTasks(TaskCreateRequest request);
   List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
   String assignByReference(AssignByReferenceRequest request);
   List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
   TaskManagementDto findTaskById(Long id);
   TaskManagementDto updateTaskPriority(Long taskId, UpdateTaskPriorityRequest request);
   TaskManagementDto addComment(Long taskId, AddCommentRequest request);
   
}

