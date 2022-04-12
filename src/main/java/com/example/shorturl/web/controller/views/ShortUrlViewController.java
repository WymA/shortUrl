package com.example.shorturl.web.controller.views;

import com.example.shorturl.web.common.Slog;
import com.example.shorturl.web.exception.AppException;
import com.example.shorturl.web.service.UrlService;
import com.example.shorturl.web.utils.CodeChecking;
import com.example.shorturl.web.utils.Constants;
import com.example.shorturl.web.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by dell on 2019/4/16.
 */
@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ShortUrlViewController {
    @Autowired
    private Slog slog;
    @Autowired
    private UrlService urlService;

    @RequestMapping("/index")
    public String index(){
        return "/index";
    }

    @RequestMapping("/{urlcode}")
    public String redirectUrl(@PathVariable("urlcode") String code) throws AppException {
        slog.info("redirect url 开始:"+ code);
        if (!CodeChecking.checking(code, Constants.CODE_LENGTH_MAX)){
            throw new AppException("0","code 格式化错误");
        }
        String orgUrl = urlService.getOrgUrlByCode(code);
        if (orgUrl == null){
            throw  new AppException("0","orgUrl 找不到 by code :"+code);
        }
        return "redirect://"+orgUrl;
    }
}
