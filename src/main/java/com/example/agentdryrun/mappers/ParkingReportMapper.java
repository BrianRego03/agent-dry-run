package com.example.agentdryrun.mappers;

import com.example.agentdryrun.dtos.BaseParkingReportRequest;
import com.example.agentdryrun.dtos.ParkingReportDto;
import com.example.agentdryrun.entity.ParkingReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {VehicleMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ParkingReportMapper {
    @Mapping(source = "reporter.id", target = "reporterId")
    ParkingReportDto toDto(ParkingReport parkingReport);

    @Mapping(target = "reporter", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "clearanceStatus", ignore = true)
    ParkingReport toEntity(BaseParkingReportRequest request);
}
