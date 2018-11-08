package com.snxy.snxyocr.controller;

import com.snxy.common.response.ResultData;
import com.snxy.snxyocr.service.AipOcrService;
import com.snxy.snxyocr.service.vo.BusinessLicenseVO;
import com.snxy.snxyocr.service.vo.DriverLicenseVO;
import com.snxy.snxyocr.service.vo.IdCardInfoVO;
import com.snxy.snxyocr.service.vo.VehicleLicenseVO;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/ocr")
public class AipOcrController {
    @Resource
    AipOcrService aipOcrService;

    @RequestMapping("/idcardFront")
    public ResultData idcardFront(MultipartFile idFrontUrl) throws IOException {
        IdCardInfoVO idCardInfoVO = aipOcrService.idcardFront(idFrontUrl);
        return ResultData.success(idCardInfoVO);
    }

    @RequestMapping("/idcardBack")
    public ResultData idcardBack(MultipartFile idBackUrl) throws IOException {
        IdCardInfoVO idCardInfoVO = aipOcrService.idcardBack(idBackUrl);
        return ResultData.success(idCardInfoVO);
    }

    @RequestMapping("/drivingLicenseFront")
    public ResultData drivingLicenseFront(MultipartFile drivingLicenseFrontUrl) throws IOException {
        DriverLicenseVO driverLicenseVO = aipOcrService.drivingLicenseFront(drivingLicenseFrontUrl);
        return ResultData.success(driverLicenseVO);
    }

    @RequestMapping("/vehicleLicense")
    public ResultData vehicleLicense(MultipartFile vehicleDrivingLicenseFrontUrl) throws IOException {
        VehicleLicenseVO vehicleLicenseVO = aipOcrService.vehicleLicense(vehicleDrivingLicenseFrontUrl);
        return ResultData.success(vehicleLicenseVO);
    }

    @RequestMapping("/businessLicense")
    public ResultData businessLicense(MultipartFile corporateCertificationUrl) throws IOException {
        BusinessLicenseVO businessLicenseVO = aipOcrService.businessLicense(corporateCertificationUrl);
        return ResultData.success(businessLicenseVO);
    }
}
