package br.com.fiap.postech.medsync.scheduling.application.dtos;


public class CancelAppointmentDTO {
    private String cancellationReason;

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
}