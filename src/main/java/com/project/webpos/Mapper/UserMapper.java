package com.project.webpos.Mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.webpos.Entity.User;
import com.project.webpos.Entity.DTO.UserCreateRequest;
import com.project.webpos.Entity.DTO.UserPutRequest;
import com.project.webpos.Entity.DTO.UserResponse;

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
  UserResponse toResponse(User user);
  List<UserResponse> toResponseList(List<User> users);

  @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
  User toPostUser(UserCreateRequest userCreateRequest, @Context PasswordEncoder encoder);
  @Named("encodePassword")
  default String encodePassword(String password, @Context PasswordEncoder encoder){
    return encoder.encode(password);
  }

  void toPutUser(UserPutRequest userPutRequest, @MappingTarget User user);
}
