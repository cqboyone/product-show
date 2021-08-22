package com.hikvision.productshow.module.service;

import com.hikvision.productshow.module.dto.LikedCountDTO;

import java.util.List;

public interface RedisService {

    /**
     * 查询是否已喜欢（点赞）
     *
     * @param likedTargetId
     * @param likeUserId
     * @return
     */
    Integer queryLikeFromRedis(String likedTargetId, String likeUserId);

    /**
     * 点赞。状态为1
     *
     * @param likedTargetId
     * @param likeUserId
     */
    void saveLiked2Redis(String likedTargetId, String likeUserId);

    /**
     * 取消点赞。将状态改变为0
     *
     * @param likedTargetId
     * @param likeUserId
     */
    void unlikeFromRedis(String likedTargetId, String likeUserId);

    /**
     * 从Redis中删除一条点赞数据
     *
     * @param likedTargetId
     * @param likeUserId
     */
    void deleteLikedFromRedis(String likedTargetId, String likeUserId);

    /**
     * 该目标的点赞数加1
     *
     * @param likedTargetId
     */
    void incrementLikedCount(String likedTargetId);

    /**
     * 该目标的点赞数减1
     *
     * @param likedTargetId
     */
    void decrementLikedCount(String likedTargetId);

    /**
     * 查询案例的点赞数量
     *
     * @param likedTargetId
     * @return
     */
    Integer queryCountByTargetId(String likedTargetId);

//    /**
//     * 获取Redis中存储的所有点赞数据
//     *
//     * @return
//     */
//    List<UserLike> getLikedDataFromRedis();
//

    /**
     * 获取Redis中存储的所有点赞数量
     *
     * @return
     */
    List<LikedCountDTO> getLikedCountFromRedis();

    /**
     * 清除redis里面的点赞计数缓存，持久化到数据库后操作
     *
     * @param ids
     */
    void clearLikedCountFromRedis(List<String> ids);
}
