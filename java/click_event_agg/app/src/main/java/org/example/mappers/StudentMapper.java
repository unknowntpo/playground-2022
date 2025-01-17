package org.example.mappers;

import org.example.dtos.StudentDTO;
import org.example.entities.Student;
import org.example.mappers.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper  extends EntityMapper<Student, StudentDTO> {
}
