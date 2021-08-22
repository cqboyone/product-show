package com.hikvision.productshow.task;

import com.hikvision.productshow.module.entity.ProductInfo;
import com.hikvision.productshow.module.service.ProductInfoService;
import com.hikvision.productshow.module.service.RedisService;
import com.hikvision.productshow.module.dto.LikedCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @creator vv
 * @date 2021/6/2 11:22
 */
@Slf4j
@Component
public class LikeTask {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductInfoService productInfoService;

    @Scheduled(cron = "${cron.1:0 0/1 * * * ?}")
    public void updateLikedCountForProductInfo() {
        log.info("点赞计数同步任务--开始定时同步点赞数量到产品表");
        List<LikedCountDTO> list = redisService.getLikedCountFromRedis();
        log.info("点赞计数同步任务--得到{}条带统计的数据", list.size());
        if (ObjectUtils.isEmpty(list)) {
            return;
        }
        list.stream()
                .forEach(e -> {
                    ProductInfo byId = productInfoService.getById(e.getId());
                    byId.setLikeTotal(byId.getLikeTotal() + e.getCount());
                    productInfoService.updateById(byId);
                });
        List<String> ids = list.stream().map(e -> e.getId()).collect(Collectors.toList());
        log.info("点赞计数同步任务--准备清理点赞计数缓存数据，size={}", ids.size());
        redisService.clearLikedCountFromRedis(ids);
    }
}
