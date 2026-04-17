package com.project.interviewmanagement_service.common.config.mapper;
import org.mapstruct.MapperConfig;

/**
 * Central configuration for MapStruct mappers.
 *
 * Ensures all mappers use Spring's component model,
 * allowing them to be injected as Spring beans.
 */
@MapperConfig(componentModel = "spring")
public interface CentralMapperConfig {

}
