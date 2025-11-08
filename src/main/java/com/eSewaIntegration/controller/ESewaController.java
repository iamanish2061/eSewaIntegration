package com.eSewaIntegration.controller;

import com.eSewaIntegration.config.ESewaConfig;
import com.eSewaIntegration.entity.ESewa;
import com.eSewaIntegration.service.ESewaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@RestController
public class ESewaController {

    private static final Logger log = LoggerFactory.getLogger(ESewaController.class);
    @Autowired
    private ESewaService eSewaService;

    @Autowired
    private ESewaConfig config;

    //controller that handles user request (i.e clicking pay with esewa button)
    @PostMapping("/pay-with-esewa")
    public void payWithEsewa(HttpServletRequest request, HttpServletResponse response)throws IOException{
        //do processing with the help of service (According to your logic)
        //i am just setting the hardcoded fields required for esewa
        ESewa eSewa = new ESewa();
        eSewa.setAmount(BigDecimal.valueOf(1000));
        eSewa.setTaxAmt(BigDecimal.valueOf(100));
        eSewa.setTotal_amount("1100");

        eSewa.setTransaction_uuid(config.generateTransactionUuid());

        eSewa.setProductServiceCharge(BigDecimal.valueOf(0));
        eSewa.setProductDeliveryCharge(BigDecimal.valueOf(0));

        String data = config.prepareDataForSignature(eSewa.getTotal_amount(), eSewa.getTransaction_uuid());
        eSewa.setSignature(config.getSignature(data));

        //insert into database if you want to (example shipping table) with the help of service and repo classes

        request.setAttribute("EsewaInfo", eSewa);

        try {
            request.getRequestDispatcher("/send-form-to-esewa").forward(request, response);
        } catch (Exception e) {
            log.error("Error: {}",e.getMessage());
        }
    }



//    controller that handles the form submission (internally forward to this controller for submitting form to esewa)
    @RequestMapping("/send-form-to-esewa")
    public void sendFormToEsewa(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ESewa esewa = (ESewa) request.getAttribute("EsewaInfo");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<form id='esewaForm' action='" + config.getPaymentUrl() + "' method='POST'>");
        out.println("<input type='hidden' name='amount' value='" + esewa.getAmount() + "' />");
        out.println("<input type='hidden' name='tax_amount' value='" + esewa.getTaxAmt() + "' />");
        out.println("<input type='hidden' name='total_amount' value='" + esewa.getTotal_amount() + "' />");
        out.println("<input type='hidden' name='transaction_uuid' value='" + esewa.getTransaction_uuid() + "' />");
        out.println("<input type='hidden' name='product_code' value='" + config.getMerchantId() + "' />");
        out.println("<input type='hidden' name='product_service_charge' value='" + esewa.getProductServiceCharge() + "' />");
        out.println("<input type='hidden' name='product_delivery_charge' value='" + esewa.getProductDeliveryCharge() + "' />");
        out.println("<input type='hidden' name='success_url' value='" + config.getResponseHandlingUrl() + "' />");
        out.println("<input type='hidden' name='failure_url' value='" + config.getResponseHandlingUrl() + "' />");
        out.println("<input type='hidden' name='signed_field_names' value='total_amount,transaction_uuid,product_code' />");
        out.println("<input type='hidden' name='signature' value='" + esewa.getSignature() + "' />");
        out.println("<input type='submit' id='submit-btn' value='Pay Now' style='display:none;'>");
        out.println("</form>");

//        auto submitting form
        out.println("<script>document.getElementById('esewaForm').submit();</script>");
        out.println("</body></html>");


//        if code runs successfully, the response from esewa will be received to url that we have stated in RESPONSE_HANDLING_URL of ESewaConfig class
    }


//    controller that handles the response received from esewa after payment is done
    @GetMapping("/esewa-response-handle")
    public void esewaResponseHandler(@RequestParam(required = false) String data,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {

        if (data == null || data.isEmpty()) {
            response.sendRedirect("/payment-failure");
            return;
        }
        ESewa paymentResponse = eSewaService.parseEsewaResponse(data);
        boolean validity = eSewaService.validateResponse(paymentResponse);

        try {
            request.getRequestDispatcher(validity?"/payment-success":"/payment-failure").forward(request,response);
        } catch (ServletException e) {
            log.error("Error forwarding to success/failure page", e);
            response.sendRedirect("/payment-failure");
        }

    }


    //controller that handles successful transaction
    @RequestMapping("/payment-success")
    public RedirectView redirectToSuccessPage(HttpServletRequest request, HttpServletResponse response) {
        return new RedirectView("/success.html");
    }

    //controller that handles failed transaction
    @RequestMapping("/payment-failure")
    public RedirectView redirectToFailurePage(HttpServletRequest request, HttpServletResponse response) {
        return new RedirectView("/failure.html");
    }

}
