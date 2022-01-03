package com.sdlh.demo.gc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    /* 时间字符串格式为: 2019/12/13 11:58:34.934 */
    private String time;
    private double data;
    private double depth;
    private double winchValue;
    private double loadValue;
    private String location;
    private int index;
}
