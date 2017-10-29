package com.wangfanpinche.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 收款信息
 *
 */
@Entity
@Table(name = "t_payment")
@DynamicInsert
@DynamicUpdate
public class Payment extends BaseEntity {

}
