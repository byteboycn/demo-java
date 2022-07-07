package cn.byteboy.demo.spring.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hongshaochuan
 */
@Getter
@Setter
@TableName("table_a")
public class TableAEntity {

    private Integer aId;

    private String aName;
}
