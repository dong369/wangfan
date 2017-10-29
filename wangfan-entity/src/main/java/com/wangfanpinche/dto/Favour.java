package com.wangfanpinche.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 付款信息
 *
 */
@Entity
@Table(name = "t_favour")
@DynamicInsert
@DynamicUpdate
public class Favour extends BaseEntity {

}
