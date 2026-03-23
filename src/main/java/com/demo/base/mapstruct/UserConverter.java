package com.demo.base.mapstruct;

import com.demo.base.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {
    void cover(User user, @MappingTarget User target);
}
