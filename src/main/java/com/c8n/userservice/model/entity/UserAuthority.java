package com.c8n.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import static com.c8n.userservice.constant.UserServiceConstant.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = TABLE_USER_AUTHORITY)
public class UserAuthority implements Serializable {
    private static final long serialVersionUID = 7665521604528322278L;
    @Id
    @Column(COL_ID)
    @JsonIgnore
    private UUID id;

    @Column(COL_USER_ID)
    @JsonIgnore
    private UUID userId;

    @Column(COL_USER_ROLE_ID)
    private int roleId;

    @Column(COL_AUTHORITY_LIST)
    private String authorityList;

    @Column(COL_DELETED)
    @JsonIgnore
    private boolean deleted;

    @Column(COL_CREATEDATE)
    @JsonIgnore
    private Timestamp createDate;

    @Column(COL_UPDATEDATE)
    @JsonIgnore
    private Timestamp updateDate;
}
