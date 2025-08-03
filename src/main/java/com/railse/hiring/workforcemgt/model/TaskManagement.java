package com.railse.hiring.workforcemgt.model;

import java.util.ArrayList;
import java.util.List;

import com.railse.hiring.workforcemgt.common.model.enums.ReferenceType;
import com.railse.hiring.workforcemgt.model.enums.Priority;
import com.railse.hiring.workforcemgt.model.enums.Task;
import com.railse.hiring.workforcemgt.model.enums.TaskStatus;

import lombok.Data;


@Data
public class TaskManagement {
   private Long id;
   private Long referenceId;
   private ReferenceType referenceType;
   private Task task;
   private String description;
   private TaskStatus status;
   private Long assigneeId; // Simplified from Entity for this assignment
   private Long taskStartTime;
   private Long taskDeadlineTime; 
   private Long taskCompletionTime; 
   private Priority priority;
   
   private List<Comment> comments = new ArrayList<>(); 
   private List<Activity> activityHistory = new ArrayList<>();
}




