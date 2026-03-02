package com.schoolapp.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TransactionExportRequest {
    private String type; // SALES or PURCHASE
    private Date fromDate;
    private Date toDate;
    private String partyName;
    private Double minAmount;
    private Double maxAmount;
    private String format; // excel or pdf
}
