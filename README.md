# shorturl

## Short URL service

### 接口：
1. demo主页： http://localhost:port/index
2. 长连接转换短连接缓存redis+持久化： http://localhost:port/short/get (POST) 参数：orgurl : "长链接" ，customCode : "自定义字符";
3. 连接映射详情，包含短连接过期时间，访问次数：http://localhost:port/short/details (get)
(port:8081)
db:

### MySQL 

```sql
CREATE TABLE `shorturl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `short_code` varchar(255) NOT NULL,
  `org_url` varchar(255) NOT NULL,
  `create_time` varchar(255) NOT NULL,
  `update_time` varchar(255) NOT NULL,
  `click_times` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `short_code_index` (`short_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
```