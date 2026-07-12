package com.students.service;

import com.razorpay.Order;
import com.razorpay.Utils;
import com.razorpay.RazorpayException;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.students.dto.Course;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public Order createOrder(double amount, String receiptId) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (amount * 100)); // Razorpay uses paise, not rupees
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receiptId);
        orderRequest.put("payment_capture", 1); // auto-capture payment

        return client.orders.create(orderRequest);
    }
    public boolean verifySignature(String orderId, String paymentId, String signature) throws RazorpayException {
    JSONObject options = new JSONObject();
    options.put("razorpay_order_id", orderId);
    options.put("razorpay_payment_id", paymentId);
    options.put("razorpay_signature", signature);

    return Utils.verifyPaymentSignature(options, keySecret);
}
}