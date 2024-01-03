package com.c8n.userservice.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_record")
public class User {
    @Id
    @Column("id")
    private UUID id;
    @Column("username")
    private String username;
    @Column("name")
    private String name;
    @Column("surname")
    private String surname;
    @Column("identifier")
    private String identifier;
    @Column("password")
    private String password;
    @Column("createdate")
    private Timestamp createDate;
    @Column("updatedate")
    private Timestamp updateDate;
    @Column("deleted")
    private boolean deleted;
}
