package com.wanli.common;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BaseEntity {
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer statu;
}
