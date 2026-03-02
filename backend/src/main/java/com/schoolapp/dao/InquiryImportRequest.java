package com.schoolapp.dao;

import java.util.List;

public class InquiryImportRequest {

    private List<InquiryDto> inquiries;
    private int uploadedBy;

    public List<InquiryDto> getInquiries() {
        return inquiries;
    }

    public void setInquiries(List<InquiryDto> inquiries) {
        this.inquiries = inquiries;
    }

    public int getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(int uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
