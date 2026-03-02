package com.schoolapp.dao;

//package com.crmemp.dto;

import java.util.Date;
import java.util.List;

public class AutoclaveDTO {

    public Long id;

    public String autoclaveNo;
    public String runNo;
    public String startedAt;
    public Date startedDate;
    public String completedAt;
    public Date completedDate;
    public String remarks;

    public int userId;
    public int branchId;
    public int orgId;

    public List<AutoclaveWagonDTO> wagons;
}
