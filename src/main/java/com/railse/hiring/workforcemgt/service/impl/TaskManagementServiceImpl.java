package com.railse.hiring.workforcemgt.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.railse.hiring.workforcemgt.common.exception.ResourceNotFoundException;
import com.railse.hiring.workforcemgt.dto.AddCommentRequest;
import com.railse.hiring.workforcemgt.dto.AssignByReferenceRequest;
import com.railse.hiring.workforcemgt.dto.TaskCreateRequest;
import com.railse.hiring.workforcemgt.dto.TaskFetchByDateRequest;
import com.railse.hiring.workforcemgt.dto.TaskManagementDto;
import com.railse.hiring.workforcemgt.dto.UpdateTaskPriorityRequest;
import com.railse.hiring.workforcemgt.dto.UpdateTaskRequest;
import com.railse.hiring.workforcemgt.mapper.ITaskManagementMapper;
import com.railse.hiring.workforcemgt.model.Activity;
import com.railse.hiring.workforcemgt.model.Comment;
import com.railse.hiring.workforcemgt.model.TaskManagement;
import com.railse.hiring.workforcemgt.model.enums.Priority;
import com.railse.hiring.workforcemgt.model.enums.Task;
import com.railse.hiring.workforcemgt.model.enums.TaskStatus;
import com.railse.hiring.workforcemgt.repository.TaskRepository;
import com.railse.hiring.workforcemgt.service.TaskManagementService;


@Service

public class TaskManagementServiceImpl implements TaskManagementService {
 

   private final TaskRepository taskRepository;
   private final ITaskManagementMapper taskMapper;


   public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
       this.taskRepository = taskRepository;
       this.taskMapper = taskMapper;
   }


   @Override
   public TaskManagementDto findTaskById(Long id) {
       TaskManagement task = taskRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
       return taskMapper.modelToDto(task);
   }


   @Override
   public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
       List<TaskManagement> createdTasks = new ArrayList<>();
       for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
           TaskManagement newTask = new TaskManagement();
           newTask.setReferenceId(item.getReferenceId());
           newTask.setReferenceType(item.getReferenceType());
           newTask.setTask(item.getTask());
           newTask.setAssigneeId(item.getAssigneeId());
           newTask.setPriority(item.getPriority());
           newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
           newTask.setStatus(TaskStatus.ASSIGNED);
           newTask.setDescription("New task created.");
           createdTasks.add(taskRepository.save(newTask));
       }
       return taskMapper.modelListToDtoList(createdTasks);
   }


   @Override
   public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
       List<TaskManagement> updatedTasks = new ArrayList<>();
       for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
           TaskManagement task = taskRepository.findById(item.getTaskId())
                   .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));


           if (item.getTaskStatus() != null) {
               task.setStatus(item.getTaskStatus());
           }
           if (item.getDescription() != null) {
               task.setDescription(item.getDescription());
           }
           updatedTasks.add(taskRepository.save(task));
       }
       return taskMapper.modelListToDtoList(updatedTasks);
   }


   @Override
   public String assignByReference(AssignByReferenceRequest request) {
       List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
       List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

       // First, cancel all non-completed tasks for this reference.
       for (TaskManagement existingTask : existingTasks) {
           if (existingTask.getStatus() != TaskStatus.COMPLETED && existingTask.getStatus() != TaskStatus.CANCELLED) {
               existingTask.setStatus(TaskStatus.CANCELLED);
               taskRepository.save(existingTask);
           }
       }

       // Now, create a new task for each applicable type and assign it.
       for (Task taskType : applicableTasks) {
           TaskManagement newTask = new TaskManagement();
           newTask.setReferenceId(request.getReferenceId());
           newTask.setReferenceType(request.getReferenceType());
           newTask.setTask(taskType);
           newTask.setAssigneeId(request.getAssigneeId());
           newTask.setStatus(TaskStatus.ASSIGNED);
           newTask.setDescription("Task reassigned to new assignee.");
           // We'll set a default priority and deadline, as they are not provided in the request
           newTask.setPriority(Priority.LOW);
           newTask.setTaskDeadlineTime(System.currentTimeMillis() + 86400000);
           taskRepository.save(newTask);
       }
       return "Tasks assigned successfully for reference " + request.getReferenceId();
   }


   @Override
   public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
       List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());
       long startDate = request.getStartDate();
       long endDate = request.getEndDate();

       // BUG #2 is here. It should filter out CANCELLED tasks but doesn't.
       List<TaskManagement> filteredTasks = tasks.stream()
               .filter(task -> {
                   // This logic is incomplete for the assignment.
                   // It should check against startDate and endDate.
                   // For now, it just returns all tasks for the assignees.
            	 if( task.getStatus()== TaskStatus.CANCELLED || task.getStatus()== TaskStatus.COMPLETED)
            	 {
            		return false; 
            	 }
            	 boolean startsWithinRange =task.getTaskStartTime()>=startDate && task.getTaskStartTime()<=endDate;
            	 boolean startedBeforeRange = task.getTaskStartTime() < startDate;
            	 return startsWithinRange || startedBeforeRange;
                // return true;
               })
               .collect(Collectors.toList());


       return taskMapper.modelListToDtoList(filteredTasks);
   }
   
   @Override
   public TaskManagementDto updateTaskPriority(Long taskId, UpdateTaskPriorityRequest request) {
	   TaskManagement task = taskRepository.findById(taskId)
               .orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found."));
       
       String oldPriority = task.getPriority().name();
       task.setPriority(request.getPriority());
       task.getActivityHistory().add(new Activity("Priority changed from " + oldPriority + " to " + request.getPriority().name(), System.currentTimeMillis()));

       taskRepository.save(task);
       return taskMapper.modelToDto(task);
   }
   
   @Override
   public TaskManagementDto addComment(Long taskId, AddCommentRequest request) {
	   TaskManagement task = taskRepository.findById(taskId)
               .orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found."));

       task.getComments().add(new Comment(request.getAuthor(), request.getMessage(), System.currentTimeMillis()));
       task.getActivityHistory().add(new Activity("New comment added by " + request.getAuthor(), System.currentTimeMillis()));
                  
       taskRepository.save(task);
       return taskMapper.modelToDto(task);
   }
   
}


