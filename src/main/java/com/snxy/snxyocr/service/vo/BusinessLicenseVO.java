package com.snxy.snxyocr.service.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusinessLicenseVO {
    private String legalPerson;                  //法人
    private String establishedDate;             //成立日期
    private String registeredCapital;           //注册资本
    private String socialCreditCode;            //证件编号/社会信用代码/注册号
    private String address;                      //地址
    private String companyName;                 //单位名称
    private String validity;                    //有效期
}
