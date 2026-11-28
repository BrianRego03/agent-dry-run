package com.example.agentdryrun.mappers;

import com.example.agentdryrun.dtos.RegisterVehicleRequest;
import com.example.agentdryrun.dtos.VehicleDto;
import com.example.agentdryrun.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VehicleMapper {
    VehicleDto toDto(Vehicle vehicle);

    @Mapping(target = "firstSeenAt", ignore = true)
    @Mapping(target = "offenceCount", ignore = true)
    Vehicle toEntity(RegisterVehicleRequest request);
}
