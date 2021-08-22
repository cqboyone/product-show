package com.hikvision.productshow.module.controller;

import com.hikvision.productshow.common.BO.BaseResult;
import com.hikvision.productshow.common.enums.LikedStatusEnum;
import com.hikvision.productshow.common.util.IPUtil;
import com.hikvision.productshow.module.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @description:
 * @creator vv
 * @date 2021/5/28 16:11
 */
@RestController
@RequestMapping("like")
@Api(tags = "点赞")
@Validated //要验证list需要使用该注解
@Slf4j
public class LikeController extends BaseController {

    @Resource
    private RedisService redisService;

    @ApiOperation(value = "点赞", notes = "点赞")
    @PostMapping
    public BaseResult like(@RequestParam("likedTargetId") String likedTargetId, HttpServletRequest request) {
        //todo 这里要判断，没有点赞过才+1
        //先查询redis，如果没有数据，再查询数据库
        Integer hadLikeFromRedis = redisService.queryLikeFromRedis(likedTargetId, IPUtil.getIp2(request));
        //todo 查询数据库
        if (Objects.equals(hadLikeFromRedis, LikedStatusEnum.LIKE.getCode())) {
            return BaseResult.success();
        }
        //先把数据存到Redis里,再定时存回数据库
        redisService.saveLiked2Redis(likedTargetId, IPUtil.getIp2(request));
        redisService.incrementLikedCount(likedTargetId);
        return BaseResult.success();
    }

    @ApiOperation(value = "取消点赞", notes = "取消点赞")
    @DeleteMapping
    public BaseResult unLike(@RequestParam("likedTargetId") String likedTargetId, HttpServletRequest request) {
        Integer hadLikeFromRedis = redisService.queryLikeFromRedis(likedTargetId, IPUtil.getIp2(request));
        //因为这个项目只有点赞和不点赞，没有点踩，所以就这样判断更简单。当查询出来的结果不是点赞，即为null（没有点赞记录）或者0的时候，直接返回。
        if (!Objects.equals(hadLikeFromRedis, LikedStatusEnum.LIKE.getCode())) {
            return BaseResult.success();
        }
        //取消点赞,先存到Redis里面，再定时写到数据库里
        redisService.unlikeFromRedis(likedTargetId, IPUtil.getIp2(request));
        redisService.decrementLikedCount(likedTargetId);
        return BaseResult.success();
    }

    @ApiOperation(value = "查询点赞状态", notes = "1点赞；0取消点赞/未点赞")
    @GetMapping
    public BaseResult<Integer> isLike(@RequestParam("likedTargetId") String likedTargetId, HttpServletRequest request) {
        Integer hadLikeFromRedis = redisService.queryLikeFromRedis(likedTargetId, IPUtil.getIp2(request));
        return BaseResult.success(hadLikeFromRedis == null ? LikedStatusEnum.UNLIKE.getCode() : hadLikeFromRedis);
    }
}
