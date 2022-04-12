package com.example.shorturl.web.entity;

import lombok.*;

/**
 * 短连 长连接 映射实体
 * Created by dell on 2019/4/16.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrl {
    private Integer id;

    private String shortCode;

    private String orgUrl;

    private String createTime;

    private String updateTime;

    private Long clickTimes;
}
