package com.example.shorturl.web.dao;

;
import com.example.shorturl.web.entity.ShortUrl;
import org.springframework.stereotype.Component;


@Component
public interface ShortUrlMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShortUrl record);

    int insertSelective(ShortUrl record);

    ShortUrl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShortUrl record);

    int updateByPrimaryKey(ShortUrl record);

    ShortUrl selectBycode(String code);

    void updateUrlStatus(String code);
    void createUrlsInfo(ShortUrl entity);
    ShortUrl selectByLongCode(String code);
}