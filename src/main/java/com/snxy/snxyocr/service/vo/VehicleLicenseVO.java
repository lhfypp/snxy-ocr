package com.snxy.snxyocr.service.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//行驶证信息
public class VehicleLicenseVO {
    private String vehicleIdentificationNo;
    private String address;
    private String brandModelNo;
    private String issueDate;
    private String vehicleType;
    private String holder;
    private String useNature;
    private String engineNo;
    private String licensePlate;
    private String registrationDate;
}
