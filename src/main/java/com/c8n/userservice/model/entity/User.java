package com.c8n.userservice.model.entity;

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
@Table(name = TABLE_USER_RECORD)
public class User {
    @Id
    @Column(COL_ID)
    private UUID id;
    @Column(COL_USERNAME)
    private String username;
    @Column(COL_NAME)
    private String name;
    @Column(COL_SURNAME)
    private String surname;
    @Column(COL_IDENTIFIER)
    private String identifier;
    @Column(COL_PASSWORD)
    private String password;
    @Column(COL_CREATEDATE)
    private Timestamp createDate;
    @Column(COL_UPDATEDATE)
    private Timestamp updateDate;
    @Column(COL_DELETED)
    private boolean deleted;
}
