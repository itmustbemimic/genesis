package io.neond.genesis.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
public class Bingo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userUuid;
    private boolean field1;
    private boolean field2;
    private boolean field3;
    private boolean field4;
    private boolean field5;
    private boolean field6;
    private boolean field7;
    private boolean field8;
    private boolean field9;
    private boolean field10;
    private boolean field11;
    private boolean field12;
    private boolean field13;
    private boolean field14;
    private boolean field15;
    private boolean field16;
}