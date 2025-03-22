package com.tbb.payment.controller;

import com.tbb.payment.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final WorkflowService workflowService;

    @PostMapping("/start")
    public String startPaymentWorkflow(@RequestParam String paymentId) {
        workflowService.startPaymentWorkflows(paymentId);
        return "Workflow started for payment: " + paymentId;
    }
}
