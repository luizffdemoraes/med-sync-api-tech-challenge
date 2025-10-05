package br.com.fiap.postech.medsync.scheduling.infrastructure.gateways;

import br.com.fiap.postech.medsync.scheduling.domain.entities.Appointment;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.entity.AppointmentEntity;
import br.com.fiap.postech.medsync.scheduling.infrastructure.persistence.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentGatewayImplTest {

    private AppointmentRepository appointmentRepository;
    private AppointmentGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        gateway = new AppointmentGatewayImpl(appointmentRepository);
    }

    @Test
    void testSave() {
        Appointment domain = mock(Appointment.class);
        AppointmentEntity entity = mock(AppointmentEntity.class);
        AppointmentEntity savedEntity = mock(AppointmentEntity.class);

        when(savedEntity.toDomain()).thenReturn(domain);

        try (MockedStatic<AppointmentEntity> mocked = mockStatic(AppointmentEntity.class)) {
            mocked.when(() -> AppointmentEntity.toEntity(domain)).thenReturn(entity);
            when(appointmentRepository.save(entity)).thenReturn(savedEntity);

            Appointment result = gateway.save(domain);

            assertSame(domain, result);
            verify(appointmentRepository).save(entity);
        }
    }

    @Test
    void testUpdate() {
        Appointment domain = mock(Appointment.class);
        AppointmentEntity entity = mock(AppointmentEntity.class);
        AppointmentEntity updatedEntity = mock(AppointmentEntity.class);

        when(updatedEntity.toDomain()).thenReturn(domain);

        try (MockedStatic<AppointmentEntity> mocked = mockStatic(AppointmentEntity.class)) {
            mocked.when(() -> AppointmentEntity.toEntity(domain)).thenReturn(entity);
            when(appointmentRepository.save(entity)).thenReturn(updatedEntity);

            Appointment result = gateway.update(domain);

            assertSame(domain, result);
            verify(appointmentRepository).save(entity);
        }
    }

    @Test
    void testFindById() {
        Long id = 1L;
        Appointment domain = mock(Appointment.class);
        AppointmentEntity entity = mock(AppointmentEntity.class);

        when(entity.toDomain()).thenReturn(domain);
        when(appointmentRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<Appointment> result = gateway.findById(id);

        assertTrue(result.isPresent());
        assertSame(domain, result.get());
        verify(appointmentRepository).findById(id);
    }

    @Test
    void testFindAll_withPatientFilter() {
        Long patientId = 10L;
        Pageable pageable = PageRequest.of(0, 10);
        Appointment domain = mock(Appointment.class);
        AppointmentEntity entity = mock(AppointmentEntity.class);

        when(entity.toDomain()).thenReturn(domain);
        Page<AppointmentEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(appointmentRepository.findByPatientId(patientId, pageable)).thenReturn(page);

        Page<Appointment> result = gateway.findAll(patientId, null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertSame(domain, result.getContent().get(0));
        verify(appointmentRepository).findByPatientId(patientId, pageable);
    }

    @Test
    void testFindAll_withDoctorAndStatusFilter() {
        Long doctorId = 20L;
        String status = "SCHEDULED";
        Pageable pageable = PageRequest.of(0, 5);
        Appointment domain = mock(Appointment.class);
        AppointmentEntity entity = mock(AppointmentEntity.class);

        when(entity.toDomain()).thenReturn(domain);
        Page<AppointmentEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(appointmentRepository.findByDoctorIdAndStatus(doctorId, status, pageable)).thenReturn(page);

        Page<Appointment> result = gateway.findAll(null, doctorId, status, pageable);

        assertEquals(1, result.getTotalElements());
        assertSame(domain, result.getContent().get(0));
        verify(appointmentRepository).findByDoctorIdAndStatus(doctorId, status, pageable);
    }

    @Test
    void testFindAll_noFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Appointment domain = mock(Appointment.class);
        AppointmentEntity entity = mock(AppointmentEntity.class);

        when(entity.toDomain()).thenReturn(domain);
        Page<AppointmentEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(appointmentRepository.findAll(pageable)).thenReturn(page);

        Page<Appointment> result = gateway.findAll(null, null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertSame(domain, result.getContent().get(0));
        verify(appointmentRepository).findAll(pageable);
    }
}