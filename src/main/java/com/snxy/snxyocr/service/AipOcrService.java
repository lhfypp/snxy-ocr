package com.snxy.snxyocr.service;

import com.snxy.snxyocr.service.vo.BusinessLicenseVO;
import com.snxy.snxyocr.service.vo.DriverLicenseVO;
import com.snxy.snxyocr.service.vo.IdCardInfoVO;
import com.snxy.snxyocr.service.vo.VehicleLicenseVO;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AipOcrService {

    IdCardInfoVO idcardFront(MultipartFile idFrontUrl) throws IOException;

    IdCardInfoVO idcardBack( MultipartFile idBackUrl) throws IOException;

    DriverLicenseVO drivingLicenseFront(MultipartFile drivingLicenseFrontUrl) throws IOException;

    VehicleLicenseVO vehicleLicense(MultipartFile vehicleDrivingLicenseFrontUrl) throws IOException;

    BusinessLicenseVO businessLicense(MultipartFile corporateCertificationUrl) throws IOException;
}
