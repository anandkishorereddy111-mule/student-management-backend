package com.students.studentmanagement.controller; 

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.students.service.SheetsService;
import com.students.service.EmailService;
import com.students.service.RazorpayService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*") 
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;
    
    @Autowired
    private SheetsService sheetsService;
    
    @Autowired
    private EmailService emailService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam double amount, @RequestParam String receiptId) {
        try {
            Order order = razorpayService.createOrder(amount, receiptId);
            return ResponseEntity.ok(order.toString());
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body("Order creation failed: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestParam String razorpay_order_id,
            @RequestParam String razorpay_payment_id,
            @RequestParam String razorpay_signature,
            @RequestParam String email,
            @RequestParam String studentName) {
        try {
            boolean isValid = razorpayService.verifySignature(
                    razorpay_order_id, razorpay_payment_id, razorpay_signature);

            if (!isValid) {
                return ResponseEntity.status(400).body("Payment verification failed");
            }

            boolean updated = sheetsService.updateStatusByEmail(email);

            if (!updated) {
                return ResponseEntity.status(404).body("Student record not found for email: " + email);
            }

            emailService.sendPaymentConfirmation(email, studentName);

            return ResponseEntity.ok("Payment verified and status updated");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Verification error: " + e.getMessage());
        }
    }
}