package com.railse.hiring.workforcemgt.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.railse.hiring.workforcemgt.common.model.response.Response;
import com.railse.hiring.workforcemgt.common.model.response.ResponseStatus;
import com.railse.hiring.workforcemgt.dto.AddCommentRequest;
import com.railse.hiring.workforcemgt.dto.AssignByReferenceRequest;
import com.railse.hiring.workforcemgt.dto.TaskCreateRequest;
import com.railse.hiring.workforcemgt.dto.TaskFetchByDateRequest;
import com.railse.hiring.workforcemgt.dto.TaskManagementDto;
import com.railse.hiring.workforcemgt.dto.UpdateTaskPriorityRequest;
import com.railse.hiring.workforcemgt.dto.UpdateTaskRequest;
import com.railse.hiring.workforcemgt.service.TaskManagementService;


@RestController
@RequestMapping("/taskmgt")
public class TaskManagementController {


   private final TaskManagementService taskManagementService;


   public TaskManagementController(TaskManagementService taskManagementService) {
       this.taskManagementService = taskManagementService;
   }


   @GetMapping("/{id}")
   public Response<TaskManagementDto> getTaskById(@PathVariable Long id) {
       return new Response<>(taskManagementService.findTaskById(id));
   }


   @PostMapping("/create")
   public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {
       return new Response<>(taskManagementService.createTasks(request));
   }


   @PostMapping("/update")
   public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
       return new Response<>(taskManagementService.updateTasks(request));
   }


   @PostMapping("/assign-by-ref")
   public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
       return new Response<>(taskManagementService.assignByReference(request));
   }


   @PostMapping("/fetch-by-date/v2")
   public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
       return new Response<>(taskManagementService.fetchTasksByDate(request));
   }
   
   @PutMapping("/{id}/priority")
   public Response<TaskManagementDto> updateTaskPriority(@PathVariable("id") Long taskId,
                                                       @RequestBody UpdateTaskPriorityRequest request) {
       TaskManagementDto updatedTask = taskManagementService.updateTaskPriority(taskId, request);
       
       // This line is changed to match your constructor
       return new Response<>(updatedTask, null, new ResponseStatus(200, "Priority updated successfully"));
   }
   
   @PostMapping("/{id}/comments")
   public Response<TaskManagementDto> addComment(@PathVariable("id") Long taskId,
                                                 @RequestBody AddCommentRequest request) {
       TaskManagementDto updatedTask = taskManagementService.addComment(taskId, request);
       return new Response<>(updatedTask, null, new ResponseStatus(200, "Comment added successfully"));
   }
   
}



