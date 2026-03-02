package com.schoolapp.service.export;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StageReportService {
    String getStageName();

    String[] getHeaders();

    List<Map<String, Object>> getData(Date fromDate, Date toDate);
}
