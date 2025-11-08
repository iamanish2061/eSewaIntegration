package com.eSewaIntegration.config;

import com.eSewaIntegration.entity.ESewa;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration
@ConfigurationProperties(prefix="esewa")
public class ESewaConfig {
    private static final Logger log = LoggerFactory.getLogger(ESewaConfig.class);
    private String merchantId;
    private String secretKey;
    private String paymentUrl;
    private String responseHandlingUrl;

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getPaymentUrl() { return paymentUrl; }
    public void setPaymentUrl(String paymentUrl) { this.paymentUrl = paymentUrl; }
    public String getResponseHandlingUrl() { return responseHandlingUrl; }
    public void setResponseHandlingUrl(String responseHandlingUrl) { this.responseHandlingUrl = responseHandlingUrl; }


    //this method is used to generate unique transaction uuid each time its requested
    public String generateTransactionUuid() {
        return UUID.randomUUID().toString();
    }

    //this method is used to generate data required for generating signature (according to the requirement stated by esewa to generate signature)
    public String prepareDataForSignature(String totalAmount, String transactionUuid) {
        return String.format("total_amount=%s,transaction_uuid=%s,product_code=%s",totalAmount, transactionUuid, merchantId);
    }

    // used for generating signature based on given data
    public String getSignature(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);
            byte[] hashBytes = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }

    //to verify signature sent from esewa
    //if response from esewa then verifying
    public boolean verifySignature(ESewa response) {
        try {
            if (response == null || response.getSigned_field_names() == null || response.getSignature() == null) {
                log.warn("Missing signed_field_names or signature");
                return false;
            }

            String[] fields = response.getSigned_field_names().split(",");
            StringBuilder signedData = new StringBuilder();
            for (int i = 0; i < fields.length; i++) {
                String key = fields[i];
                String value = getValueFromResponse(response, key);
                signedData.append(key).append("=").append(value);
                if (i < fields.length - 1) signedData.append(",");
            }
            String esewaGeneratedSignature = getSignature(signedData.toString());

            return (response.getSignature().equals(esewaGeneratedSignature));
        } catch (Exception e) {
            log.error("Signature verification failed: ",e);
            return false;
        }
    }

    public String getValueFromResponse(ESewa response, String fieldName) {
        switch (fieldName) {
            case "transaction_code": return response.getTransaction_code();
            case "status": return response.getStatus();
            case "total_amount": return response.getTotal_amount();
            case "transaction_uuid": return response.getTransaction_uuid();
            case "product_code": return response.getProduct_code();
            case "signed_field_names": return response.getSigned_field_names();
            default: return "";
        }
    }

}
