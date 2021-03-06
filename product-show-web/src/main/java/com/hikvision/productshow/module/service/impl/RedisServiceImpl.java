package com.hikvision.productshow.module.service.impl;

import com.hikvision.productshow.common.enums.LikedStatusEnum;
import com.hikvision.productshow.common.util.RedisKeyUtils;
import com.hikvision.productshow.module.service.RedisService;
import com.hikvision.productshow.module.dto.LikedCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    LikedService likedService;

    @Override
    public Integer queryLikeFromRedis(String likedTargetId, String likeUserId) {
        String key = RedisKeyUtils.getLikedKey(likedTargetId, likeUserId);
        Object o = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
        if (o == null || !(o instanceof Integer)) {
            return null;
        }
        return (Integer) o;
    }

    @Override
    public void saveLiked2Redis(String likedTargetId, String likeUserId) {
        String key = RedisKeyUtils.getLikedKey(likedTargetId, likeUserId);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, LikedStatusEnum.LIKE.getCode());
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED_DB, key, LikedStatusEnum.LIKE.getCode());
    }

    @Override
    public void unlikeFromRedis(String likedTargetId, String likeUserId) {
        String key = RedisKeyUtils.getLikedKey(likedTargetId, likeUserId);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, LikedStatusEnum.UNLIKE.getCode());
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED_DB, key, LikedStatusEnum.UNLIKE.getCode());
    }

    @Override
    public void deleteLikedFromRedis(String likedTargetId, String likeUserId) {
        String key = RedisKeyUtils.getLikedKey(likedTargetId, likeUserId);
        redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
        redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED_DB, key);
    }

    @Override
    public void incrementLikedCount(String likedTargetId) {
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, likedTargetId, 1);
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT_DB, likedTargetId, 1);
    }

    @Override
    public void decrementLikedCount(String likedTargetId) {
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, likedTargetId, -1);
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT_DB, likedTargetId, -1);
    }

    /**
     * ???????????????????????????
     *
     * @param likedTargetId
     * @return
     */
    @Override
    public Integer queryCountByTargetId(String likedTargetId) {
        Object o = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, likedTargetId);
        if (o instanceof Integer) {
            return (Integer) o;
        }
        return 0;
    }

    //    @Override
//    public List<UserLike> getLikedDataFromRedis() {
//        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED, ScanOptions.NONE);
//        List<UserLike> list = new ArrayList<>();
//        while (cursor.hasNext()) {
//            Map.Entry<Object, Object> entry = cursor.next();
//            String key = (String) entry.getKey();
//            //????????? likedUserId???likedPostId
//            String[] split = key.split("::");
//            String likedUserId = split[0];
//            String likedPostId = split[1];
//            Integer value = (Integer) entry.getValue();
//
//            //????????? UserLike ??????
//            UserLike userLike = new UserLike(likedUserId, likedPostId, value);
//            list.add(userLike);
//
//            //?????? list ?????? Redis ?????????
//            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
//        }
//
//        return list;
//    }
//
    @Override
    public List<LikedCountDTO> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT_DB, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            //???????????????????????? LikedCountDT
            if (!(map.getKey() instanceof String) || !(map.getValue() instanceof Number)) {
                continue;
            }
            String key = (String) map.getKey();
            Integer value = (Integer) map.getValue();
            list.add(new LikedCountDTO(key, value));
        }
        return list;
    }

    /**
     * ??????redis?????????????????????DB???????????????????????????????????????
     *
     * @param ids
     */
    @Override
    public void clearLikedCountFromRedis(List<String> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return;
        }
        //???Redis?????????????????????
        ids.stream().forEach(e -> redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT_DB, e));
    }
}
