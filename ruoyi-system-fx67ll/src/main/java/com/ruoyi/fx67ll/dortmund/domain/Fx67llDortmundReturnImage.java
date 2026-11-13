package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 回报组合图片登记对象 fx67ll_dortmund_return_image。
 * <p>
 * 文件系统只负责保存文件，业务归属、生命周期和删除对账以本表为准。
 *
 * @author fx67ll
 * @date 2026-11-12
 */
public class Fx67llDortmundReturnImage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 图片登记主键 */
    private Long imageId;

    /** 相对对象键 */
    private String objectKey;

    /** 上传原始文件名 */
    private String originalName;

    /** 文件MIME类型 */
    private String contentType;

    /** 文件大小（字节） */
    private Long fileSize;

    /** 上传用户 */
    private Long userId;

    /** 关联的回报组合；T状态未绑定时为空，B状态必须指向已采用组合 */
    private Long combinationId;

    /** 图片状态：T-已存储未绑定，B-已绑定已采用组合，D-待删除，Q-隔离 */
    private String imageState;

    /** T状态临时过期时间；B状态为空 */
    private Date expireTime;

    /** 删除尝试次数 */
    private Integer deleteAttempts;

    /** 下次删除重试时间 */
    private Date nextRetryTime;

    /** 最近一次受控错误信息 */
    private String lastError;

    /** 查询开始时间（非表列） */
    private String beginCreateTime;

    /** 查询结束时间（非表列） */
    private String endCreateTime;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(Long combinationId) {
        this.combinationId = combinationId;
    }

    public String getImageState() {
        return imageState;
    }

    public void setImageState(String imageState) {
        this.imageState = imageState;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getDeleteAttempts() {
        return deleteAttempts;
    }

    public void setDeleteAttempts(Integer deleteAttempts) {
        this.deleteAttempts = deleteAttempts;
    }

    public Date getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(Date nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public String getBeginCreateTime() {
        return beginCreateTime;
    }

    public void setBeginCreateTime(String beginCreateTime) {
        this.beginCreateTime = beginCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

}
