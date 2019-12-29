package net.wano.po.task;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by mrt on 2018/4/4.
 */
@Data
@ToString
@Entity
@Table(name = "wlzx-task")


public class XcTask implements Serializable {

   @TableId
    private String id;

    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "delete_time")
    private Date deleteTime;
    @Column(name = "task_type")
    private String taskType;
    @Column(name = "mq_exchange")
    private String mqExchange;
    @Column(name = "mq_routingkey")
    private String mqRoutingkey;
    @Column(name = "request_body")
    private String requestBody;
    private Integer version;
    private String status;
    private String errormsg;
}
