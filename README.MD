# README
## 开发
- [swagger](http://localhost:8022/ps/swagger-ui/)
### ffmpeg
#### window
- 下载
    - https://www.ffmpeg.org/download.html#build-windows
    - 解压即可使用
#### linux
- 下载
    - https://ffmpeg.org/releases/
    - 下载ffmpeg-4.4.tar.xz
    - 放到Linux下解压，给777权限 chmod 777 ./ffmpeg
    - 创建转换输出文件夹
- 脚本
    ```shell
    ./ffmpeg -i  ../trailer.mp4   -hls_time 10 -hls_list_size 0  -hls_segment_filename ../hls/lucene_%05d.ts ../hls/lucene.m3u8
    ```
## 部署
1. 我把ffmpeg集成到工程里面了，所以无需单独下载

2. 暂时只支持win部署

3. 部署时要填写上传文件系统缓存路径，确保文件夹存在。

   ```yml
   spring:
     servlet:
       multipart:
         # 缓存文件目录。这里要设置绝对路径，且文件夹要存在。
         location: D:/***/temp
   ```
   
4. nginx

    ```java
    location /ps { 
        proxy_set_header Host $http_host; 
        proxy_set_header X-Forward-For $remote_addr;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        rewrite ^/api/(.*)$ /$1 break;
        proxy_pass   http://10.196.42.105:8022/ps;
    }
    ```

   