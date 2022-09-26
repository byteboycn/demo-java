package cn.byteboy.demo.spring.web.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author hongshaochuan
 */
@Getter
@Setter
@TableName("table_a")
public class TableAEntity {

    @TableId
    private Integer aId;

    private String aName;

    private Date aDate;
}
