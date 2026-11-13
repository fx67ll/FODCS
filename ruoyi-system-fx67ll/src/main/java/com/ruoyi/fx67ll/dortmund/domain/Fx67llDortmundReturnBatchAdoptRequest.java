package com.ruoyi.fx67ll.dortmund.domain;

import java.util.List;

/**
 * 回报组合批量采用请求。
 *
 * @author fx67ll
 * @date 2026-11-12
 */
public class Fx67llDortmundReturnBatchAdoptRequest {
    /** 待采用的回报组合主键集合 */
    private List<Long> combinationIds;

    public List<Long> getCombinationIds() {
        return combinationIds;
    }

    public void setCombinationIds(List<Long> combinationIds) {
        this.combinationIds = combinationIds;
    }
}
