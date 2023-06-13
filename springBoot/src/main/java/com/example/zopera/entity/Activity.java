package com.example.zopera.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @TableName activity
 */
@TableName(value = "activity")
@Data
public class Activity implements Serializable {
    /**
     * 活动 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 活动内容
     */
    private String detail;

    /**
     * 活动时间
     */
//    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDate startTime;
//     @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
     private LocalDate endTime;
    private int state;
    private String stateInfo;

    /**
     * 点击量
     */
    private Integer count;

    /**
     * 活动发布时间
     */
    private Date publishTime;

    /**
     * 活动发起人
     */
    private String sponsor;

    /**
     * 活动封面图片
     */
    private String coverUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Activity other = (Activity)that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getLocation() == null ? other.getLocation() == null
                : this.getLocation().equals(other.getLocation()))
            && (this.getStartTime() == null ? other.getStartTime() == null
                : this.getStartTime().equals(other.getStartTime()))
            && (this.getCount() == null ? other.getCount() == null : this.getCount().equals(other.getCount()))
            && (this.getPublishTime() == null ? other.getPublishTime() == null
                : this.getPublishTime().equals(other.getPublishTime()))
            && (this.getSponsor() == null ? other.getSponsor() == null : this.getSponsor().equals(other.getSponsor()))
            && (this.getCoverUrl() == null ? other.getCoverUrl() == null
                : this.getCoverUrl().equals(other.getCoverUrl()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getLocation() == null) ? 0 : getLocation().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getCount() == null) ? 0 : getCount().hashCode());
        result = prime * result + ((getPublishTime() == null) ? 0 : getPublishTime().hashCode());
        result = prime * result + ((getSponsor() == null) ? 0 : getSponsor().hashCode());
        result = prime * result + ((getCoverUrl() == null) ? 0 : getCoverUrl().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", content=").append(content);
        sb.append(", location=").append(location);
        sb.append(", startTime=").append(startTime);
        sb.append(", count=").append(count);
        sb.append(", publishTime=").append(publishTime);
        sb.append(", sponsor=").append(sponsor);
        sb.append(", coverUrl=").append(coverUrl);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}