package com.eSewaIntegration.entity;

import java.math.BigDecimal;

public class ESewa {

    private BigDecimal amount;
    private BigDecimal taxAmt;
    private BigDecimal productServiceCharge;
    private BigDecimal productDeliveryCharge;

    //for response
    private String transaction_code;
    private String status;
    private String total_amount;
    private String transaction_uuid;
    private String product_code;
    private String signed_field_names;
    private String signature;


    public ESewa() {}

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getTaxAmt() {
        return taxAmt;
    }
    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }
    public BigDecimal getProductServiceCharge() {
        return productServiceCharge;
    }
    public void setProductServiceCharge(BigDecimal productServiceCharge) {
        this.productServiceCharge = productServiceCharge;
    }
    public BigDecimal getProductDeliveryCharge() {
        return productDeliveryCharge;
    }
    public void setProductDeliveryCharge(BigDecimal productDeliveryCharge) {
        this.productDeliveryCharge = productDeliveryCharge;
    }
    public String getTransaction_code() {
        return transaction_code;
    }
    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTotal_amount() {
        return total_amount;
    }
    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }
    public String getTransaction_uuid() {
        return transaction_uuid;
    }
    public void setTransaction_uuid(String transaction_uuid) {
        this.transaction_uuid = transaction_uuid;
    }
    public String getProduct_code() {
        return product_code;
    }
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
    public String getSigned_field_names() {
        return signed_field_names;
    }
    public void setSigned_field_names(String signed_field_names) {
        this.signed_field_names = signed_field_names;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }


}
