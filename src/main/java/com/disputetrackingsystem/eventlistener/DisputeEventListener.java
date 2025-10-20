package com.disputetrackingsystem.eventlistener;

import com.disputetrackingsystem.model.Client;
import com.disputetrackingsystem.model.Dispute;
import com.disputetrackingsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DisputeEventListener {

    @Autowired
    private EmailService emailService;

    @Async
    @EventListener
    public void handleDisputeCreated(DisputeCreatedEvent event) {
        Dispute dispute = event.getDispute();
        Client client = dispute.getSavingsAccountTransaction()
                .getSavingsAccount()
                .getClient();

        if (client != null && client.getEmail() != null) {
            String subject = "DTMS | Confirmation: Dispute Created with Dispute ID #DSP202500" + dispute.getId() + " Successfully";
            String text = String.format("""
                            Dear %s,
                            
                            Your dispute (ID: DSP202500%d) has been registered successfully against transaction ID #TXN202500%d by our team.
                            
                            Our team will now review your case and get back to you with updates as soon as possible.
                            
                            Thank you for your patience and cooperation.
                            
                            Regards,
                            Dispute Management Team
                            """,
                    client.getName(),
                    dispute.getId(),
                    dispute.getSavingsAccountTransaction().getId()
            );

            emailService.sendEmail(client.getEmail(), subject, text);
        }
    }

    @Async
    @EventListener
    public void handleDisputeClosed(DisputeClosedEvent event) {
        Dispute dispute = event.getDispute();
        // Assuming the client is linked via dispute.getSavingsAccountTransaction().getSavingsAccount().getClient()
        Client client = dispute.getSavingsAccountTransaction()
                .getSavingsAccount()
                .getClient();

        if (client != null && client.getEmail() != null) {
            String subject = "DTMS | Dispute Status Update";
            String text = String.format("""
                Dear %s,

                Your dispute (ID: DSP202500%d) has been reviewed and closed by our team.

                Status: %s
                Sub-Status: %s
                Comments: %s

                Thank you for your patience and cooperation.

                Regards,
                Dispute Management Team
                """,
                    client.getName(),
                    dispute.getId(),
                    dispute.getStatus().getName(),
                    dispute.getSubStatus() != null ? dispute.getSubStatus().getName() : "N/A",
                    dispute.getComments() != null ? dispute.getComments() : "No comments");

            emailService.sendEmail(client.getEmail(), subject, text);
        }
    }

    @Async
    @EventListener
    public void handleDisputeClosed(DisputeDeleteEvent event) {
        Dispute dispute = event.getDispute();
        // Assuming the client is linked via dispute.getSavingsAccountTransaction().getSavingsAccount().getClient()
        Client client = dispute.getSavingsAccountTransaction()
                .getSavingsAccount()
                .getClient();

        if (client != null && client.getEmail() != null) {
            String subject = "DTMS | Dispute Status Update";
            String text = String.format("""
                Dear %s,

                Your dispute (ID: DSP202500%d) has been deleted by our team as per your request or due to invalid credentials.
                Contact or visit the bank for more information.

                Thank you for your patience and cooperation.

                Regards,
                Dispute Management Team
                """,
                    client.getName(),
                    dispute.getId()
            );

            emailService.sendEmail(client.getEmail(), subject, text);
        }
    }
}
