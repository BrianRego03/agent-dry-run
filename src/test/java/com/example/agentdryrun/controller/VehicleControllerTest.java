package com.example.agentdryrun.controller;

import com.example.agentdryrun.entity.Vehicle;
import com.example.agentdryrun.service.JwtService;
import com.example.agentdryrun.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void getVehicleById_whenFound_returnsOkWithVehicle() throws Exception {
        Instant firstSeenAt = Instant.parse("2026-11-28T10:15:30Z");
        Vehicle vehicle = new Vehicle("MH12AB1234", "Tata", "Nexon", "White", firstSeenAt);
        when(vehicleService.getById(1L)).thenReturn(vehicle);

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value("MH12AB1234"))
                .andExpect(jsonPath("$.make").value("Tata"))
                .andExpect(jsonPath("$.model").value("Nexon"))
                .andExpect(jsonPath("$.colour").value("White"))
                .andExpect(jsonPath("$.firstSeenAt").value("2026-11-28T10:15:30Z"))
                .andExpect(jsonPath("$.offenceCount").value(0));

        verify(vehicleService).getById(1L);
    }

    @Test
    @WithMockUser
    void getVehicleById_whenNotFound_returnsNotFound() throws Exception {
        when(vehicleService.getById(99L)).thenThrow(new IllegalArgumentException("Vehicle not found: 99"));

        mockMvc.perform(get("/vehicles/99"))
                .andExpect(status().isNotFound());

        verify(vehicleService).getById(99L);
    }

    @Test
    void getVehicleById_whenUnauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isUnauthorized());
    }
}
