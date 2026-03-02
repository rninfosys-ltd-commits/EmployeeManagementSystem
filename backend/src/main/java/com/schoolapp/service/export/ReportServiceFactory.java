package com.schoolapp.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ReportServiceFactory {

    private final Map<String, StageReportService> services;

    @Autowired
    public ReportServiceFactory(List<StageReportService> serviceList) {
        this.services = serviceList.stream()
                .collect(Collectors.toMap(
                        s -> s.getStageName().toLowerCase(),
                        Function.identity()));
    }

    public Optional<StageReportService> getService(String stage) {
        return Optional.ofNullable(services.get(stage.toLowerCase()));
    }
}
