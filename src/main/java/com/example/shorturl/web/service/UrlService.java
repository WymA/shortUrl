package com.example.shorturl.web.service;

import com.example.shorturl.web.common.Slog;
import com.example.shorturl.web.common.Sredis;
import com.example.shorturl.web.dao.ShortUrlMapper;
import com.example.shorturl.web.entity.ShortUrl;
import com.example.shorturl.web.exception.AppException;
import com.example.shorturl.web.utils.Constants;
import com.example.shorturl.web.utils.Utils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2019/4/16.
 */
@Service
public class UrlService {

    @Autowired
    private ShortUrlMapper shorturlMapper;
    @Autowired
    private Slog slog;
    @Autowired
    private Sredis sredis;
    @Value("${shorturl.incrkey}")
    String short_url_index;
    @Value("${shorturl.http_prefix}")
    String http_prefix;


    public  String getOrgUrlByCode(String code) {
        String orgUrl = sredis.getString("short:"+code);
        if(orgUrl == null){
            ShortUrl shorturl = shorturlMapper.selectBycode(code);
            if(shorturl !=null){
                orgUrl = shorturl.getOrgUrl();
            }
        }

        final String tempUrl = orgUrl;
        TaskService.submit(() -> {
            updateShortUrlInfo(code, tempUrl);
            return 0;
        });

        slog.info("获取orgUrl by code:" + code + " org:" + orgUrl);
        return orgUrl;
    }
    public Boolean updateShortUrlInfo(String code, String orgUrl) {

        try {
            if (orgUrl != null) {
                sredis.addString("short:"+code, orgUrl, Constants.REDIS_URL_EXP);
                shorturlMapper.updateUrlStatus(code);
            }
        } catch (Exception ee) {
            slog.error("更新失败 error:" + code + " url:" + orgUrl);
        }
        return true;
    }

    public Map<String,String> createShortUrl(String orgurl,String customCode) throws AppException{
        Map<String, String> item_map = Maps.newHashMap();
        Map<String, String> db_item = Maps.newHashMap();
        String oldCode = sredis.getString(orgurl);
        if (StringUtils.isNotBlank(customCode)){
            oldCode = null;
        }
        String code = "";
        if (Utils.checkHttpUrl(orgurl) == false) {
            item_map.put("url_short", "");
            item_map.put("url_long", orgurl);
            item_map.put("type", "0");
            item_map.put("result", "false");
        }
        if (oldCode != null) {
            item_map.put("url_short", http_prefix + oldCode);
            item_map.put("url_long", orgurl);
            item_map.put("type", "0");
            item_map.put("result", "true");
            slog.info(" 从redis获取 ret_maps :" + item_map);
        } else {
            if (StringUtils.isNotBlank(customCode)){
                code = customCode;
            }else {
                code = Utils.str2MD5(orgurl);
            }
            item_map.put("url_short", http_prefix + code );
            item_map.put("url_long", orgurl);
            item_map.put("type", "0");
            item_map.put("result", "true");

            db_item.put("code", code);
            db_item.put("url_long", orgurl);
        }
        try {
            sredis.addString("short:"+code, orgurl, Constants.REDIS_URL_EXP);
            sredis.addString(orgurl,code,Constants.REDIS_URL_SAME_EXP);
        } catch (Exception ee) {
            slog.error("保存redis error:" + code + " url:" + orgurl);
        }
        //save to mysql ansyc
        TaskService.submit(() -> {
            saveUrlInfoToDb(db_item);
            return 0;
        });
        return item_map;
    }
    public Boolean saveUrlInfoToDb(Map<String, String> url) throws AppException {
        if (url == null) {
            return true;
        }
        List<ShortUrl> entitys = Lists.newArrayList();
        ShortUrl entity = new ShortUrl();
        entity.setShortCode(url.get("code"));
        entity.setOrgUrl(url.get("url_long"));
        try {
            shorturlMapper.createUrlsInfo(entity);
        } catch (Exception e) {
            slog.error(" saveUrlInfoToDb error:" + e.getMessage());
        }
        return true;
    }

    public ShortUrl getShortUrlDetails(String code){
        if (StringUtils.isBlank(code)){
            return null;
        }
        ShortUrl shortUrl = shorturlMapper.selectBycode(code);
        return shortUrl;
    }
}
