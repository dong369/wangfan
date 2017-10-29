package com.wangfanpinche.service;

import java.util.List;
import java.util.Map;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.vo.CarVo;
import com.wangfanpinche.vo.JournalVo;
import com.wangfanpinche.vo.ResourceVo;
import com.wangfanpinche.vo.UserVo;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

public interface UserService {

	/**
	 * 通过用户ID，拿到所有的资源
	 * @param id
	 * @return
	 */
	public List<String> getResourceList(String id);
	
	/**
	 * 手机端-通过value获取User对象
	 * 判断是否登录或登录超时
	 * @param value
	 * @return
	 */
	public User getUserByPhoneToken(String phoneToken);
	
	/**
	 * 通过手机号和密码获取用户
	 * @param vo
	 * @return
	 */
	public UserVo getByPhoneAndPwd(UserVo vo);
	
	/**
	 * 获取用户菜单
	 * @param id
	 * @return
	 */
	public List<ResourceVo> getMenuList(String id);	
	
	/**
	 * 后台用户列表（分页）
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(UserVo vo, PageHelper ph);

	/**
	 * 根据手机号，发送手机验证码
	 * @author Kevin
	 * @date 2016年11月16日
	 */
	public void sendVerify(UserVo vo);

	/**
	 * 通过手机号和验证码登陆
	 * @author Kevin
	 * @date 2016年11月16日
	 */
	public UserVo validPhoneAndVerify(UserVo vo);

	/**
	 * 系统里是否有这个手机号
	 * @author Kevin
	 * @date 2016年11月16日
	 */
	public Boolean hasPhone(UserVo vo);

	/**
	 * 创建用户
	 * @author Kevin
	 * @date 2016年11月16日
	 */
	public String add(UserVo vo);

	/**
	 * 查看个人资料
	 * @param userId
	 * @return
	 */
	public UserVo getUserInfo(String userId);
	
	/**
	 * 查看个人详细资料
	 * @param userId
	 * @return
	 */
	public UserVo getUserDetail(String userId);

	/**
	 * 编辑个人资料
	 * @param vo
	 */
	public void edit(UserVo vo);
	
	/**
	 * 是否实名认证过
	 * @param vo
	 * @return
	 */
	public Boolean hasUserApprove(UserVo vo);
	
	/**
	 * 实名认证
	 * @param vo
	 */
	public void userApprove(UserVo vo);
	
	
	/**
	 * 根据用户id返回车辆信息
	 * @param userId
	 * @return
	 */
	public CarVo getCarByUserId(String userId);
	
	/**
	 * 根据carInfoId查询品牌车系
	 * @param carInfoId
	 * @return
	 */
	public CarVo getCarByCarInfoId(String carInfoId);
	
	/**
	 * 添加紧急联系人
	 * @param vo
	 */
	public void addSos(UserVo vo);
	
	/**
	 * 查询紧急联系人
	 * @param vo
	 * @return
	 */
	public UserVo getSos(UserVo vo);
	
	/**
	 * 禁用
	 * @param id
	 */
	public void enableUser(String id);
	
	/**
	 * 启用
	 * @param id
	 */
	public void ableUser(String id);
	
	/**
	 * 支付宝充值
	 * @param vo
	 * @return
	 */
	public String alipayCashInInfo(UserVo vo);
	
	/**
	 * 支付宝充值
	 * @return
	 */
	public void alipayCashIn(Map<String, String> map);
	
	/**
	 * 微信充值
	 * @param vo
	 * @return
	 */
	public String wechatPayCashInInfo(UserVo vo);
	
	/**
	 * 微信充值
	 * @return
	 */
	public void wechatPayCashIn(WechatPayNotify notify);
	
	/**
	 * 提现提交到后台进行审核
	 * @param vo
	 */
	public void cashOutInfo(UserVo vo);
	
	/**
	 * 后台需要审核的提现信息列表
	 * @param ph
	 * @return
	 */
	public BootstrapTable tableOut(PageHelper ph);
	
	/**
	 * 提现审核通过
	 * @param vo
	 */
	public void cashOutSucc(JournalVo vo);

	/**
	 * 忘记密码
	 * @param vo
	 */
	public void userForget(UserVo vo);
	
	
}
