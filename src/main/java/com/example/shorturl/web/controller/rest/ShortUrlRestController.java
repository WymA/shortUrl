package com.example.shorturl.web.controller.rest;

import com.example.shorturl.web.common.Slog;
import com.example.shorturl.web.entity.ShortUrl;
import com.example.shorturl.web.exception.AppException;
import com.example.shorturl.web.service.UrlService;
import com.example.shorturl.web.utils.CodeChecking;
import com.example.shorturl.web.utils.Constants;
import com.example.shorturl.web.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by dell on 2019/4/16.
 */
@RestController
@RequestMapping(value = "/short" , produces = MediaType.APPLICATION_JSON_VALUE)
public class ShortUrlRestController {
    @Autowired
    private Slog slog;
    @Autowired
    private UrlService urlService;

    @GetMapping("/{urlcode}")
    public ResponseEntity redirectUrl(@PathVariable("urlcode") String code) throws AppException {
        slog.info("redirect url 开始:"+ code);
        if (!CodeChecking.checking(code, Constants.CODE_LENGTH_MAX)){
            throw new AppException("0","code 格式化错误");
        }
        String orgUrl = urlService.getOrgUrlByCode(code);
        if (orgUrl == null){
            throw  new AppException("0","orgUrl 找不到 by code :"+code);
        }
        return ResponseEntity.ok(Utils.getRespons(orgUrl));
    }

    @PostMapping(value = "/get")
    public ResponseEntity createUrl(String orgurl,String customCode) throws AppException{
        slog.info("short url 创建开始, orgurl: "+orgurl+" customCode: "+ customCode);

        if (StringUtils.isNotBlank(customCode)){
            // 长度判断
            if (customCode.length() != 6){
                throw  new AppException("0","自定义字符长度必须为6位 customCode: "+customCode);
            }
        }

        Map<String ,String> shortUrl = urlService.createShortUrl(orgurl,customCode);
        return ResponseEntity.ok(Utils.getRespons(shortUrl));
    }

    @GetMapping(value = "/details/{urlcode}")
    public ResponseEntity details(@PathVariable("urlcode") String urlCode){
        ShortUrl shortUrl = urlService.getShortUrlDetails(urlCode);
        return ResponseEntity.ok(Utils.getRespons(shortUrl));
    }
}
