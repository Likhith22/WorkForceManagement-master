package com.railse.hiring.workforcemgt.repository;

import com.railse.hiring.workforcemgt.model.TaskManagement;
import com.railse.hiring.workforcemgt.model.enums.TaskStatus;


import java.util.List;
import java.util.Optional;


public interface TaskRepository {
   Optional<TaskManagement> findById(Long id);
   TaskManagement save(TaskManagement task);
   List<TaskManagement> findAll();
   List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, com.railse.hiring.workforcemgt.common.model.enums.ReferenceType referenceType);
   List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds);
}
