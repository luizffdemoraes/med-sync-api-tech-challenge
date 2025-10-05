package br.com.fiap.postech.medsync.scheduling.application.dtos;


import jakarta.validation.constraints.NotNull;

public class CancelAppointmentDTO {
    @NotNull(message = "CancellationReason is required")
    private String cancellationReason;
    @NotNull(message = "UpdatedBy is required")
    private Long updatedBy;

    public CancelAppointmentDTO() {}

    public CancelAppointmentDTO(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}