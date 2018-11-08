package com.snxy.snxyocr.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import com.snxy.common.exception.BizException;
import com.snxy.snxyocr.service.AipOcrService;
import com.snxy.snxyocr.service.vo.BusinessLicenseVO;
import com.snxy.snxyocr.service.vo.DriverLicenseVO;
import com.snxy.snxyocr.service.vo.IdCardInfoVO;
import com.snxy.snxyocr.service.vo.VehicleLicenseVO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AipOcrServiceImpl implements AipOcrService {
    @Resource
    private AipOcr aipOcr;// = ocrConfig.getAipCcr(ocrConfig.getBaiduOcrInfo());
    //身份证正面识别
    @Override
    public IdCardInfoVO idcardFront(MultipartFile idFrontUrl) throws IOException {
        //log.info("idFrontUrl:[{}]",idFrontUrl);
        if(idFrontUrl == null){
            throw new BizException("没有传入图片");
        }
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");

        //front正面；back反面
        String idCardSide = "front";
        //把上传图片的路径转为byte数组
        InputStream inputStream = idFrontUrl.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        byte[] bytes = output.toByteArray();
        //掉接口获取json
        JSONObject jsonObject = aipOcr.idcard(bytes, idCardSide, options);

        IdCardInfoVO idCardInfoVO = IdCardInfoVO.builder().build();
        //解析json组成vo对象
        Map parse = (Map) JSON.parse(jsonObject.toString());
        /*reversed_side-未摆正身份证
        non_idcard-上传的图片中不包含身份证
        blurred-身份证模糊
        over_exposure-身份证关键字段反光或过曝
        unknown-未知状态*/
        if(parse.get("image_status").toString().equals("reversed_side")){
            throw new BizException("未摆正身份证");
        }else if(parse.get("image_status").toString().equals("non_idcard")){
            throw new BizException("上传的图片中不包含身份证");
        }else if(parse.get("image_status").toString().equals("blurred")){
            throw new BizException("身份证模糊");
        }else if(parse.get("image_status").toString().equals("over_exposure")){
            throw new BizException("身份证关键字段反光或过曝");
        }else if(parse.get("image_status").toString().equals("unknown")){
            throw new BizException("未知状态");
        }

        Map<String,Map> map = ((Map) parse.get("words_result"));

        Map<String,Object> map1 = map.get("姓名");
        idCardInfoVO.setName(map1.get("words").toString());
        //log.info("words:[{}]",words);
        Map<String,Object> map2 = map.get("民族");
        idCardInfoVO.setNation(map2.get("words").toString());

        Map<String,Object> map3 = map.get("住址");
        idCardInfoVO.setAddress(map3.get("words").toString());

        Map<String,Object> map4 = map.get("公民身份号码");
        idCardInfoVO.setIdNo(map4.get("words").toString());

        Map<String,Object> map5 = map.get("出生");
        idCardInfoVO.setBornDate(map5.get("words").toString());

        Map<String,Object> map6 = map.get("性别");
        idCardInfoVO.setGender(map6.get("words").toString());

        //log.info("idCardInfoVO:[{}]",idCardInfoVO);
        return idCardInfoVO;
    }

    //身份证反面识别
    @Override
    public IdCardInfoVO idcardBack(MultipartFile idBackUrl) throws IOException {
        if(idBackUrl == null){
            throw new BizException("没有传入图片");
        }
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");

        //front正面；back反面
        String idCardSide = "back";

        //路径转为byte数组
        InputStream inputStream = idBackUrl.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        byte[] bytes = output.toByteArray();

        //掉接口获取json
        JSONObject jsonObject = aipOcr.idcard(bytes, idCardSide, options);

        IdCardInfoVO idCardInfoVO = IdCardInfoVO.builder().build();
        //解析json组成vo对象
        Map parse = (Map) JSON.parse(jsonObject.toString());
        if(parse.get("image_status").toString().equals("reversed_side")){
            throw new BizException("未摆正身份证");
        }else if(parse.get("image_status").toString().equals("non_idcard")){
            throw new BizException("上传的图片中不包含身份证");
        }else if(parse.get("image_status").toString().equals("blurred")){
            throw new BizException("身份证模糊");
        }else if(parse.get("image_status").toString().equals("over_exposure")){
            throw new BizException("身份证关键字段反光或过曝");
        }else if(parse.get("image_status").toString().equals("unknown")){
            throw new BizException("未知状态");
        }

        Map<String,Map> map = ((Map) parse.get("words_result"));

        Map<String,Object> map1 = map.get("失效日期");
        //log.info("map1:[{}]",map1);
        idCardInfoVO.setExpirationDate(map1.get("words").toString());

        Map<String,Object> map2 = map.get("签发机关");
        idCardInfoVO.setIssueAuthority(map2.get("words").toString());

        Map<String,Object> map3 = map.get("签发日期");
        idCardInfoVO.setIssueDate(map3.get("words").toString());

        //log.info("idCardInfoVO:[{}]",idCardInfoVO);
        return idCardInfoVO;
    }

    //驾驶证首页识别
    @Override
    public DriverLicenseVO drivingLicenseFront(MultipartFile drivingLicenseFrontUrl) throws IOException {
        if(drivingLicenseFrontUrl == null){
            throw new BizException("没有传入图片");
        }
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");

        InputStream inputStream = drivingLicenseFrontUrl.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        byte[] bytes = output.toByteArray();

        DriverLicenseVO driverLicenseVO = DriverLicenseVO.builder().build();
        //掉接口获取json
        JSONObject jsonObject = aipOcr.drivingLicense(bytes, options);

        //json解析拆分信息
        Map parse = (Map) JSON.parse(jsonObject.toString());
        Map<String,Map> map = ((Map) parse.get("words_result"));
        map.forEach((k,v)->{
            v.forEach((m,o)->{
                if(o.toString().isEmpty() || o.toString()==null){
                    throw new BizException("上传图片无法识别");
                }
            });
        });

        Map<String,Object> map1 = map.get("有效起始日期");
        if(map1==null){
            map1 = map.get("至");
        }
        //log.info("map1:[{}]",map1);
        driverLicenseVO.setEffectiveStartDate(map1.get("words").toString());
        Map<String,Object> map2 = map.get("姓名");
        driverLicenseVO.setName(map2.get("words").toString());
        Map<String,Object> map3 = map.get("证号");
        driverLicenseVO.setDriverLicenseNo(map3.get("words").toString());
        Map<String,Object> map4 = map.get("出生日期");
        driverLicenseVO.setBornDate(map4.get("words").toString());
        Map<String,Object> map5 = map.get("住址");
        driverLicenseVO.setAddress(map5.get("words").toString());
        Map<String,Object> map6 = map.get("国籍");
        driverLicenseVO.setCountry(map6.get("words").toString());
        Map<String,Object> map7 = map.get("初次领证日期");
        driverLicenseVO.setInitialLicenseDate(map7.get("words").toString());
        Map<String,Object> map8 = map.get("准驾车型");
        driverLicenseVO.setDriveType(map8.get("words").toString());
        Map<String,Object> map9 = map.get("有效期限");
        driverLicenseVO.setValidityTime(map9.get("words").toString());
        Map<String,Object> map0 = map.get("性别");
        driverLicenseVO.setGender(map0.get("words").toString());

        //log.info("解析获取驾驶证信息==driverLicenseVO:[{}]",driverLicenseVO);
        return driverLicenseVO;
    }

    //行驶证首页识别
    @Override
    public VehicleLicenseVO vehicleLicense(MultipartFile vehicleDrivingLicenseFrontUrl) throws IOException {
        if(vehicleDrivingLicenseFrontUrl == null){
            throw new BizException("没有传入图片");
        }
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("accuracy", "normal");

        InputStream inputStream = vehicleDrivingLicenseFrontUrl.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        byte[] bytes = output.toByteArray();
        //掉接口获取json
        JSONObject jsonObject = aipOcr.vehicleLicense(bytes, options);

        //json解析拆分信息
        Map parse = (Map) JSON.parse(jsonObject.toString());
        Map<String,Map> map = ((Map) parse.get("words_result"));
        map.forEach((k,v)->{
            v.forEach((m,o)->{
                if(o.toString().isEmpty() || o.toString()==null){
                    throw new BizException("上传图片无法识别");
                }
            });
        });
        //组装成对象
        VehicleLicenseVO vehicleLicenseVO = VehicleLicenseVO.builder().build();
        Map<String,Object> map1 = map.get("车辆识别代号");
        vehicleLicenseVO.setVehicleIdentificationNo(map1.get("words").toString());
        Map<String,Object> map2 = map.get("住址");
        vehicleLicenseVO.setAddress(map2.get("words").toString());
        Map<String,Object> map3 = map.get("品牌型号");
        vehicleLicenseVO.setBrandModelNo(map3.get("words").toString());
        Map<String,Object> map4 = map.get("发证日期");
        vehicleLicenseVO.setIssueDate(map4.get("words").toString());
        Map<String,Object> map5 = map.get("车辆类型");
        vehicleLicenseVO.setVehicleType(map5.get("words").toString());
        Map<String,Object> map6 = map.get("所有人");
        vehicleLicenseVO.setHolder(map6.get("words").toString());
        Map<String,Object> map7 = map.get("使用性质");
        vehicleLicenseVO.setUseNature(map7.get("words").toString());
        Map<String,Object> map8 = map.get("发动机号码");
        vehicleLicenseVO.setEngineNo(map8.get("words").toString());
        Map<String,Object> map9 = map.get("号牌号码");
        vehicleLicenseVO.setLicensePlate(map9.get("words").toString());
        Map<String,Object> map0 = map.get("注册日期");
        vehicleLicenseVO.setRegistrationDate(map0.get("words").toString());
        log.info("解析获得的信息===>vehicleLicenseVO:[{}]",vehicleLicenseVO);
        return vehicleLicenseVO;
    }

    //营业执照识别
    @Override
    public BusinessLicenseVO businessLicense(MultipartFile corporateCertificationUrl) throws IOException {
        if(corporateCertificationUrl == null){
            throw new BizException("没有传入图片");
        }
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();

        InputStream inputStream = corporateCertificationUrl.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        byte[] bytes = output.toByteArray();

        //掉接口获取json
        JSONObject jsonObject = aipOcr.businessLicense(bytes, options);

        BusinessLicenseVO businessLicenseVO = BusinessLicenseVO.builder().build();

        //json解析拆分信息
        Map parse = (Map) JSON.parse(jsonObject.toString());
        Map<String,Map> map = ((Map) parse.get("words_result"));
        map.forEach((k,v)->{
            v.forEach((m,o)->{
                if(o.toString().isEmpty() || o.toString()==null){
                    throw new BizException("上传图片无法识别");
                }
            });
        });

        Map<String,Object> map1 = map.get("法人");
        businessLicenseVO.setLegalPerson(map1.get("words").toString());
        Map<String,Object> map2 = map.get("成立日期");
        businessLicenseVO.setEstablishedDate(map2.get("words").toString());
        Map<String,Object> map3 = map.get("注册资本");
        businessLicenseVO.setRegisteredCapital(map3.get("words").toString());
        Map<String,Object> map4 = map.get("证件编号");
        businessLicenseVO.setSocialCreditCode(map4.get("words").toString());
        Map<String,Object> map5 = map.get("地址");
        businessLicenseVO.setAddress(map5.get("words").toString());
        Map<String,Object> map6 = map.get("单位名称");
        businessLicenseVO.setCompanyName(map6.get("words").toString());
        Map<String,Object> map7 = map.get("有效期");
        businessLicenseVO.setValidity(map7.get("words").toString());
        log.info("businessLicenseVO:[{}]",businessLicenseVO);
        return businessLicenseVO;
    }


}
