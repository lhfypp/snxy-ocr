package com.snxy.snxyocr.web;

import com.snxy.common.response.ResultData;
import com.snxy.snxyocr.service.AipOcrService;
import com.snxy.snxyocr.service.vo.BusinessLicenseVO;
import com.snxy.snxyocr.service.vo.DriverLicenseVO;
import com.snxy.snxyocr.service.vo.IdCardInfoVO;
import com.snxy.snxyocr.service.vo.VehicleLicenseVO;
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
    public ResultData idcardFront(MultipartFile file) throws IOException {
        IdCardInfoVO idCardInfoVO = aipOcrService.idcardFront(file);
        return ResultData.success(idCardInfoVO);
    }

    @RequestMapping("/idcardBack")
    public ResultData idcardBack(MultipartFile file) throws IOException {
        IdCardInfoVO idCardInfoVO = aipOcrService.idcardBack(file);
        return ResultData.success(idCardInfoVO);
    }

    @RequestMapping("/drivingLicenseFront")
    public ResultData drivingLicenseFront(MultipartFile file) throws IOException {
        DriverLicenseVO driverLicenseVO = aipOcrService.drivingLicenseFront(file);
        return ResultData.success(driverLicenseVO);
    }

    @RequestMapping("/vehicleLicense")
    public ResultData vehicleLicense(MultipartFile file) throws IOException {
        VehicleLicenseVO vehicleLicenseVO = aipOcrService.vehicleLicense(file);
        return ResultData.success(vehicleLicenseVO);
    }

    @RequestMapping("/businessLicense")
    public ResultData businessLicense(MultipartFile file) throws IOException {
        BusinessLicenseVO businessLicenseVO = aipOcrService.businessLicense(file);
        return ResultData.success(businessLicenseVO);
    }

    @RequestMapping("/plateLicense")
    public ResultData plateLicense(MultipartFile file) throws IOException {
        String number = aipOcrService.plateLicense(file);
        return ResultData.success(number);
    }
}
