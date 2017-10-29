package com.wangfanpinche.utils.id;

import java.util.ResourceBundle;

/**
 * 基于Twitter的snowFlake算法的ID生成器
 * 
 * @author 马凯
 * @Date 2016-12-02
 */
public class SnowFlakeIdGenerator {

	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("utils/id/snowflake");

	private static SnowFlakeIdGenerator snowFlakeIdGenerator;

	private long workerId;
	private long datacenterId;

	private long twepoch = 1288834974657L;

	private long workerIdBits = 5L;
	private long datacenterIdBits = 5L;
	private long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	private long sequenceBits = 12L;

	private long workerIdShift = sequenceBits;
	private long datacenterIdShift = sequenceBits + workerIdBits;
	private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private long sequenceMask = -1L ^ (-1L << sequenceBits);

	private volatile long sequence = 0L;
	private volatile long lastTimestamp = -1L;

	private SnowFlakeIdGenerator() {
		long workerId = Long.parseLong(bundle.getString("workerId"));
		long datacenterId = Long.parseLong(bundle.getString("datacenterId"));
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("节点ID必须在 0 和 %d 之间", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("数据中心ID必须在 0 和 %d 之间 ", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public static SnowFlakeIdGenerator getInstance() {
		if (snowFlakeIdGenerator == null)
			snowFlakeIdGenerator = new SnowFlakeIdGenerator();
		return snowFlakeIdGenerator;
	}

	public synchronized long generateLongId() {
		long timestamp = System.currentTimeMillis();

		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format("时钟竟然倒退了，如何生成ID。老大你在逗我玩吗？拒绝为 %d 秒生成ID", lastTimestamp - timestamp));
		}

		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}
}