package com.eSewaIntegration.service;

import com.eSewaIntegration.config.ESewaConfig;
import com.eSewaIntegration.entity.ESewa;
import com.google.gson.Gson;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class ESewaService {

    @Autowired
    private Gson gson;

    @Autowired
    private ESewaConfig config;

    public ESewa parseEsewaResponse(String encodedData) {
        if (encodedData == null || encodedData.isBlank()) return null;

        byte[] decoded = Base64.getDecoder().decode(encodedData);
        String decodedJson = new String(decoded, StandardCharsets.UTF_8);
        ESewa response = gson.fromJson(decodedJson, ESewa.class);

        if (response.getTotal_amount() != null) {
            response.setTotal_amount(response.getTotal_amount().replace(",", ""));
        }
        return response;
    }

    public boolean validateResponse(ESewa response) {
        if (response==null) return false;
        boolean isValid = config.verifySignature(response);
        boolean paymentComplete = "COMPLETE".equalsIgnoreCase(response.getStatus());
        return isValid && paymentComplete;
    }

}
