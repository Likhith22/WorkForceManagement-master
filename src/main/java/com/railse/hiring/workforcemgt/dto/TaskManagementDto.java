package com.railse.hiring.workforcemgt.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.railse.hiring.workforcemgt.common.model.enums.ReferenceType;
import com.railse.hiring.workforcemgt.model.enums.Priority;
import com.railse.hiring.workforcemgt.model.enums.Task;
import com.railse.hiring.workforcemgt.model.enums.TaskStatus;

import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskManagementDto {
   private Long id;
   private Long referenceId;
   private ReferenceType referenceType;
   private Task task;
   private String description;
   private TaskStatus status;
   private Long assigneeId;
   private Long taskDeadlineTime;
   private Priority priority;
   private List<CommentDto> comments; 
   private List<ActivityDto> activityHistory;
}


