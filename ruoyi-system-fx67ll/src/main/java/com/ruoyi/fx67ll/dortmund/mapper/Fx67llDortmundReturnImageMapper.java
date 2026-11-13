package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImage;
import org.apache.ibatis.annotations.Param;

/**
 * 回报组合图片登记Mapper。
 *
 * @author fx67ll
 * @date 2026-11-12
 */
public interface Fx67llDortmundReturnImageMapper {
    int insertImage(Fx67llDortmundReturnImage image);

    Fx67llDortmundReturnImage selectByImageIdForUpdate(@Param("imageId") Long imageId);

    List<Fx67llDortmundReturnImage> selectActiveImagesByCombinationId(@Param("combinationId") Long combinationId);

    int countActiveImagesByCombinationId(@Param("combinationId") Long combinationId);

    int bindToCombination(@Param("imageId") Long imageId, @Param("combinationId") Long combinationId,
            @Param("userId") Long userId);

    List<Fx67llDortmundReturnImage> selectOrphanImages(Fx67llDortmundReturnImage query);

    List<Long> selectCleanupCandidateIds(@Param("limit") int limit,
            @Param("quarantineRetryLimit") int quarantineRetryLimit);

    int markDeletePending(@Param("imageId") Long imageId, @Param("reason") String reason);

    int markDeleteFailed(@Param("imageId") Long imageId, @Param("error") String error,
            @Param("quarantine") boolean quarantine);

    int deleteImage(@Param("imageId") Long imageId);
}
