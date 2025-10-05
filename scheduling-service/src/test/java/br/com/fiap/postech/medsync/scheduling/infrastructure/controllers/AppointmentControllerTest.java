package br.com.fiap.postech.medsync.scheduling.infrastructure.controllers;

import br.com.fiap.postech.medsync.scheduling.application.dtos.AppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CancelAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.CreateAppointmentDTO;
import br.com.fiap.postech.medsync.scheduling.application.dtos.MedicalDataRequestDTO;
import br.com.fiap.postech.medsync.scheduling.application.usecases.*;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentStatus;
import br.com.fiap.postech.medsync.scheduling.domain.enums.AppointmentType;
import br.com.fiap.postech.medsync.scheduling.infrastructure.exceptions.handler.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateAppointmentUseCase createAppointmentUseCase;

    @Mock
    private AddMedicalDataUseCase addMedicalDataUseCase;

    @Mock
    private CompleteAppointmentUseCase completeAppointmentUseCase;

    @Mock
    private GetAppointmentUseCase getAppointmentUseCase;

    @Mock
    private ListAppointmentsUseCase listAppointmentsUseCase;

    @Mock
    private CancelAppointmentUseCase cancelAppointmentUseCase;

    @InjectMocks
    private AppointmentController appointmentController;

    private ObjectMapper objectMapper;
    private AppointmentDTO appointmentDTO;
    private CreateAppointmentDTO createAppointmentDTO;
    private MedicalDataRequestDTO medicalDataRequestDTO;
    private CancelAppointmentDTO cancelAppointmentDTO;

    @BeforeEach
    void setUp() {
        // Configurar ObjectMapper com suporte para Java 8 Date/Time
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configurar MockMvc com suporte a validação e Pageable
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setValidator(new LocalValidatorFactoryBean()) // ← Adicione esta linha
                .build();

        // Configurar objetos de teste VÁLIDOS
        createAppointmentDTO = new CreateAppointmentDTO(
                1L,
                "patient@example.com",
                2L,
                LocalDateTime.now().plusDays(1),
                "CONSULTATION",
                30,
                "Initial consultation"
        );

        appointmentDTO = new AppointmentDTO(
                1L,
                1L,
                2L,
                "patient@example.com",
                LocalDateTime.now().plusDays(1),
                AppointmentStatus.SCHEDULED,
                AppointmentType.CONSULTATION,
                30,
                "Initial consultation",
                null,
                "Headache",
                "Migraine",
                "Ibuprofen 400mg",
                "Patient reports frequent headaches",
                2L
        );

        medicalDataRequestDTO = new MedicalDataRequestDTO(
                "Headache",
                "Migraine",
                "Ibuprofen 400mg",
                "Patient reports frequent headaches",
                2L
        );

        cancelAppointmentDTO = new CancelAppointmentDTO();
        cancelAppointmentDTO.setCancellationReason("Patient rescheduled");
        cancelAppointmentDTO.setUpdatedBy(1L);
    }

    @Test
    void createAppointment_ShouldReturnCreatedAppointment() throws Exception {
        // Arrange
        when(createAppointmentUseCase.execute(any(CreateAppointmentDTO.class)))
                .thenReturn(appointmentDTO);

        // Act & Assert
        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAppointmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.patientUserId").value(1L))
                .andExpect(jsonPath("$.doctorUserId").value(2L))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));

        verify(createAppointmentUseCase).execute(any(CreateAppointmentDTO.class));
    }

    @Test
    void createAppointment_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange - Criar DTO realmente inválido
        CreateAppointmentDTO invalidDTO = new CreateAppointmentDTO();
        // Não setar nenhum campo - todos os @NotNull/@NotBlank falharão

        // Act & Assert
        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addMedicalData_ShouldReturnUpdatedAppointment() throws Exception {
        // Arrange
        Long appointmentId = 1L;
        when(addMedicalDataUseCase.execute(eq(appointmentId), any(MedicalDataRequestDTO.class)))
                .thenReturn(appointmentDTO);

        // Act & Assert
        mockMvc.perform(patch("/appointments/{id}/medical-data", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalDataRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.chiefComplaint").value("Headache"))
                .andExpect(jsonPath("$.diagnosis").value("Migraine"))
                .andExpect(jsonPath("$.prescription").value("Ibuprofen 400mg"));

        verify(addMedicalDataUseCase).execute(eq(appointmentId), any(MedicalDataRequestDTO.class));
    }

    @Test
    void completeAppointment_ShouldReturnCompletedAppointment() throws Exception {
        // Arrange
        Long appointmentId = 1L;
        Long updatedBy = 2L;

        AppointmentDTO completedAppointment = new AppointmentDTO(
                1L, 1L, 2L, "patient@example.com", LocalDateTime.now().plusDays(1),
                AppointmentStatus.COMPLETED, AppointmentType.CONSULTATION, 30,
                "Initial consultation", null, "Headache", "Migraine",
                "Ibuprofen 400mg", "Patient reports frequent headaches", 2L
        );

        when(completeAppointmentUseCase.execute(appointmentId, updatedBy))
                .thenReturn(completedAppointment);

        // Act & Assert
        mockMvc.perform(patch("/appointments/{id}/complete", appointmentId)
                        .param("updatedBy", updatedBy.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(completeAppointmentUseCase).execute(appointmentId, updatedBy);
    }

    @Test
    void cancelAppointment_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long appointmentId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/appointments/{id}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelAppointmentDTO)))
                .andExpect(status().isNoContent());

        verify(cancelAppointmentUseCase).execute(eq(appointmentId), any(CancelAppointmentDTO.class));
    }

    @Test
    void cancelAppointment_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Long appointmentId = 1L;
        CancelAppointmentDTO invalidDTO = new CancelAppointmentDTO();
        // Sem reason e updatedBy - viola @NotNull

        // Act & Assert
        mockMvc.perform(delete("/appointments/{id}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAppointment_ShouldReturnAppointment() throws Exception {
        // Arrange
        Long appointmentId = 1L;
        when(getAppointmentUseCase.execute(appointmentId))
                .thenReturn(appointmentDTO);

        // Act & Assert
        mockMvc.perform(get("/appointments/{id}", appointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.patientUserId").value(1L))
                .andExpect(jsonPath("$.doctorUserId").value(2L));

        verify(getAppointmentUseCase).execute(appointmentId);
    }

    @Test
    void listAppointments_WithoutFilters_ShouldReturnAllAppointments() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<AppointmentDTO> appointmentPage = new PageImpl<>(List.of(appointmentDTO), pageable, 1);

        when(listAppointmentsUseCase.execute(isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(appointmentPage);

        // Act & Assert
        mockMvc.perform(get("/appointments")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(listAppointmentsUseCase).execute(isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    void listAppointments_WithFilters_ShouldReturnFilteredAppointments() throws Exception {
        // Arrange
        Long patientId = 1L;
        Long doctorId = 2L;
        String status = "SCHEDULED";
        Pageable pageable = PageRequest.of(0, 10);
        Page<AppointmentDTO> appointmentPage = new PageImpl<>(List.of(appointmentDTO), pageable, 1);

        when(listAppointmentsUseCase.execute(eq(patientId), eq(doctorId), eq(status), any(Pageable.class)))
                .thenReturn(appointmentPage);

        // Act & Assert
        mockMvc.perform(get("/appointments")
                        .param("patientId", patientId.toString())
                        .param("doctorId", doctorId.toString())
                        .param("status", status)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].patientUserId").value(patientId))
                .andExpect(jsonPath("$.content[0].doctorUserId").value(doctorId))
                .andExpect(jsonPath("$.content[0].status").value(status));

        verify(listAppointmentsUseCase).execute(eq(patientId), eq(doctorId), eq(status), any(Pageable.class));
    }

    @Test
    void listAppointments_WithPartialFilters_ShouldReturnAppointments() throws Exception {
        // Arrange
        Long patientId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<AppointmentDTO> appointmentPage = new PageImpl<>(List.of(appointmentDTO), pageable, 1);

        when(listAppointmentsUseCase.execute(eq(patientId), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(appointmentPage);

        // Act & Assert
        mockMvc.perform(get("/appointments")
                        .param("patientId", patientId.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].patientUserId").value(patientId));

        verify(listAppointmentsUseCase).execute(eq(patientId), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    void addMedicalData_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Long appointmentId = 1L;
        MedicalDataRequestDTO invalidDTO = new MedicalDataRequestDTO();
        // Sem updatedBy - viola @NotNull

        // Act & Assert
        mockMvc.perform(patch("/appointments/{id}/medical-data", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    // Teste adicional para garantir que o endpoint de medical-data funciona sem validação no controller
    @Test
    void addMedicalData_WithValidData_ShouldWork() throws Exception {
        // Arrange
        Long appointmentId = 1L;
        when(addMedicalDataUseCase.execute(eq(appointmentId), any(MedicalDataRequestDTO.class)))
                .thenReturn(appointmentDTO);

        // Act & Assert
        mockMvc.perform(patch("/appointments/{id}/medical-data", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalDataRequestDTO)))
                .andExpect(status().isOk());

        verify(addMedicalDataUseCase).execute(eq(appointmentId), any(MedicalDataRequestDTO.class));
    }
}