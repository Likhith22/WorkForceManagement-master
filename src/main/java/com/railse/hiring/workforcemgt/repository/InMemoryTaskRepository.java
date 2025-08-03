package com.railse.hiring.workforcemgt.repository;



import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.railse.hiring.workforcemgt.common.model.enums.ReferenceType;
import com.railse.hiring.workforcemgt.model.TaskManagement;
import com.railse.hiring.workforcemgt.model.enums.Priority;
import com.railse.hiring.workforcemgt.model.enums.Task;
import com.railse.hiring.workforcemgt.model.enums.TaskStatus;


@Repository
public class InMemoryTaskRepository implements TaskRepository {


   private final Map<Long, TaskManagement> taskStore = new ConcurrentHashMap<>();
   private final AtomicLong idCounter = new AtomicLong(0L);


   public InMemoryTaskRepository() {
       // Seed data
	   createSeedTask(101L, ReferenceType.ORDER, Task.CREATE_INVOICE, 1L, TaskStatus.ASSIGNED, Priority.HIGH, System.currentTimeMillis() - 86400000);    // Task ID 1
       createSeedTask(101L, ReferenceType.ORDER, Task.ARRANGE_PICKUP, 1L, TaskStatus.COMPLETED, Priority.HIGH, System.currentTimeMillis() - 86400000);  // Task ID 2
       createSeedTask(102L, ReferenceType.ORDER, Task.CREATE_INVOICE, 2L, TaskStatus.ASSIGNED, Priority.MEDIUM, System.currentTimeMillis()); // Task ID 3
       createSeedTask(201L, ReferenceType.ENTITY, Task.ASSIGN_CUSTOMER_TO_SALES_PERSON, 2L, TaskStatus.ASSIGNED, Priority.LOW, System.currentTimeMillis()); // Task ID 4
       createSeedTask(201L, ReferenceType.ENTITY, Task.ASSIGN_CUSTOMER_TO_SALES_PERSON, 3L, TaskStatus.ASSIGNED, Priority.LOW, System.currentTimeMillis()); // Task ID 5
       createSeedTask(103L, ReferenceType.ORDER, Task.COLLECT_PAYMENT, 1L, TaskStatus.CANCELLED, Priority.MEDIUM, System.currentTimeMillis()); // Task ID 6
       createSeedTask(301L, ReferenceType.ORDER, Task.CREATE_INVOICE, 1L, TaskStatus.ASSIGNED, Priority.HIGH, System.currentTimeMillis() - 172800000); // Task ID 7 (A task from 2 days ago)
   }


   private void createSeedTask(Long referenceId, ReferenceType referenceType, Task task, Long assigneeId, TaskStatus status, Priority priority, Long startTime) {
       TaskManagement tm = new TaskManagement();
       tm.setId(idCounter.incrementAndGet());
       tm.setReferenceId(referenceId);
       tm.setReferenceType(referenceType);
       tm.setTask(task);
       tm.setDescription("This is a seed task.");
       tm.setStatus(status);
       tm.setAssigneeId(assigneeId);
       tm.setTaskStartTime(startTime);
       tm.setTaskDeadlineTime(startTime + 86400000); // Set deadline for 1 day later
       tm.setPriority(priority);
       if (status == TaskStatus.COMPLETED) {
           tm.setTaskCompletionTime(startTime + 43200000); // Set a completion time for completed tasks
       }
       taskStore.put(tm.getId(), tm);
   }
   


   @Override
   public Optional<TaskManagement> findById(Long id) {
       return Optional.ofNullable(taskStore.get(id));
   }


   @Override
   public TaskManagement save(TaskManagement task) {
       if (task.getId() == null) {
           task.setId(idCounter.incrementAndGet());
       }
       taskStore.put(task.getId(), task);
       return task;
   }


   @Override
   public List<TaskManagement> findAll() {
       return List.copyOf(taskStore.values());
   }


   @Override
   public List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType) {
       return taskStore.values().stream()
               .filter(task -> task.getReferenceId().equals(referenceId) && task.getReferenceType().equals(referenceType))
               .collect(Collectors.toList());
   }


   @Override
   public List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds) {
       return taskStore.values().stream()
               .filter(task -> assigneeIds.contains(task.getAssigneeId()))
               .collect(Collectors.toList());
   }
}
