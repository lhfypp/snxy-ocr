package com.snxy.snxyocr.service.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//驾驶证信息
public class DriverLicenseVO {

    private String effectiveStartDate;

    private String name;

    private String driverLicenseNo;

    private String bornDate;

    private String address;

    private String country;

    private String initialLicenseDate;

    private String driveType;

    private String validityTime;

    private String gender;

}
