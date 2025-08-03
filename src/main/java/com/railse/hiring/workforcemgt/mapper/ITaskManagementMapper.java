package com.railse.hiring.workforcemgt.mapper;
import java.util.List;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.railse.hiring.workforcemgt.dto.ActivityDto;
import com.railse.hiring.workforcemgt.dto.CommentDto;
import com.railse.hiring.workforcemgt.dto.TaskManagementDto;
import com.railse.hiring.workforcemgt.model.Activity;
import com.railse.hiring.workforcemgt.model.Comment;
import com.railse.hiring.workforcemgt.model.TaskManagement;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ITaskManagementMapper {
   ITaskManagementMapper INSTANCE = Mappers.getMapper(ITaskManagementMapper.class);

   @Mapping(target = "comments", source = "comments")
   @Mapping(target = "activityHistory", source = "activityHistory")
   TaskManagementDto modelToDto(TaskManagement model);


   TaskManagement dtoToModel(TaskManagementDto dto);


   List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models);
   List<CommentDto> commentListToDtoList(List<Comment> comments);
   List<ActivityDto> activityListToDtoList(List<Activity> activityHistory);
  
}

