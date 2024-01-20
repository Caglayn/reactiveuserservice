package com.c8n.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

import static com.c8n.userservice.constant.UserServiceConstant.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = TABLE_USER_ACCESS_LOG)
public class UserAccesslog {
    @Id
    @Column(COL_ID)
    private UUID id;

    @Column(COL_USER_ID)
    private UUID userId;

    @Column(COL_CREATEDATE)
    private Timestamp createDate;

    @Column(COL_IP_ADDRESS)
    private String ipAddress;

    @Column(COL_URL)
    private String url;

    @Column(COL_DETAILS)
    private String details;
}
