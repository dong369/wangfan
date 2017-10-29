package com.wangfanpinche.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Car;
import com.wangfanpinche.dto.OwnerApprove;
import com.wangfanpinche.dto.Resource;
import com.wangfanpinche.dto.Role;
import com.wangfanpinche.dto.Tag;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.User.StatusEnum;
import com.wangfanpinche.service.InitService;
import com.wangfanpinche.vo.CarVo;
import com.wangfanpinche.vo.OwnerApproveVo;
import com.wangfanpinche.vo.ResourceVo;
import com.wangfanpinche.vo.RoleVo;
import com.wangfanpinche.vo.TagVo;
import com.wangfanpinche.vo.UserVo;


@Service
public class InitServiceImpl implements InitService {
	
	@Autowired
	private BaseDao baseDao;	
				
	@Override
	public void initData() throws Exception{
		
		//初始化资源数据
		initResources();
		//初始化角色数据
		initRole();
		//初始化用户数据
		initUser();
		//初始化标签数据
		initTag();
		//初始化车主认证数据
		initOwnerApprove();
		//初始化车主车辆数据
		initCar();
					
	}
	
	/**
	 * 初始化车主车辆数据
	 * @throws Exception
	 */
	private void initCar() throws Exception {
		String lines = readFileToString("car");
		List<CarVo> cars = JSON.parseArray(lines, CarVo.class);
		for (CarVo vo : cars) {
			if (vo.getId() == null) {
				continue;
			}
			Car car = new Car();
			car.setId(vo.getId());
			car.setCarInfoId(vo.getCarInfoId());
			car.setCarColor(vo.getCarColor());
			car.setCarNumber(vo.getCarNumber());
			car.setStatus(vo.getStatus());
			car.setUserId(vo.getUserId());
			baseDao.saveOrUpdate(car);
		}
	}
	
	/**
	 * 初始化车主认证数据
	 * @throws Exception
	 */
	private void initOwnerApprove() throws Exception {
		String lines = readFileToString("ownerapprove");
		List<OwnerApproveVo> ownerapproves = JSON.parseArray(lines, OwnerApproveVo.class);
		for (OwnerApproveVo vo : ownerapproves) {
			if (vo.getId() == null) {
				continue;
			}
			OwnerApprove ownerapprove = new OwnerApprove();
			ownerapprove.setId(vo.getId());
			ownerapprove.setOwnerName(vo.getOwnerName());
			ownerapprove.setOwnerIdNumber(vo.getOwnerIdNumber());
			ownerapprove.setDrivPhoto(vo.getDrivPhoto());
			ownerapprove.setIdenPhoto(vo.getIdenPhoto());
			ownerapprove.setStatus(vo.getStatus());
			ownerapprove.setUserId(vo.getUserId());
			baseDao.saveOrUpdate(ownerapprove);
		}
	}
	
	/**
	 * 初始化标签数据
	 * @throws Exception
	 */
	private void initTag() throws Exception {
		String lines = readFileToString("tag");
		List<TagVo> tags = JSON.parseArray(lines, TagVo.class);
		for (TagVo vo : tags) {
			if (vo.getId() == null) {
				continue;
			}
			Tag tag = new Tag();
			tag.setId(vo.getId());
			tag.setTagName(vo.getTagName());
			tag.setSeq(vo.getSeq());
			baseDao.saveOrUpdate(tag);
		}
	}
	
	/**
	 * 初始化用户数据
	 * @throws Exception
	 */
	private void initUser() throws Exception {
		String lines = readFileToString("user");
		List<UserVo> users = JSON.parseArray(lines, UserVo.class);
		for (UserVo vo : users) {
			if (vo.getId() == null) {
				continue;
			}
			User us = new User();			
			us.setId(vo.getId());
			us.setUsername(vo.getUsername());
			us.setMobilePhone(vo.getMobilePhone());
			us.setStatus(vo.getStatus());
			us.setPwd(vo.getPwd());
			us.setBalance(vo.getBalance().setScale(2, RoundingMode.HALF_UP));
			us.setIdNumber(vo.getIdNumber());
			us.setUserIcon(vo.getUserIcon());
			
			for (RoleVo role : vo.getRoles()) {
				if(!StringUtils.hasText(role.getId())){
					continue;
				}
				Role rr = new Role();
				rr.setId(role.getId());
				us.getRoles().add(rr);
			}
			baseDao.saveOrUpdate(us);
		}
	}
	
	/**
	 * 初始化角色数据
	 * @throws Exception
	 */
	private void initRole() throws Exception {
		String lines = readFileToString("role");
		List<RoleVo> roles = JSON.parseArray(lines, RoleVo.class);
		for (RoleVo vo : roles) {
			if (vo.getId() == null) {
				continue;
			}
			Role role = new Role();
			role.setId(vo.getId());
			role.setName(vo.getName());
			role.setRemark(vo.getRemark());
			role.setSeq(vo.getSeq());
			
			//角色拥有的资源
			Set<Resource> l = new LinkedHashSet<>();
			for (ResourceVo resource : vo.getResources()) {
				Resource rr = new Resource();
				rr.setId(resource.getId());
				l.add(rr);
			}
			role.setResources(l);
			baseDao.saveOrUpdate(role);
		}
	}

	/**
	 * 初始化资源数据
	 * @throws Exception 
	 */
	private void initResources() throws Exception {
		String lines = readFileToString("resource");
		List<ResourceVo> resources = JSON.parseArray(lines, ResourceVo.class); 
		for (ResourceVo vo : resources) {
			System.out.println(vo.getId());
			if (vo.getId() == null) {
				continue;
			}
			Resource re = new Resource();
			BeanUtils.copyProperties(vo, re);
			if(StringUtils.hasText(vo.getResourceId())){
				re.setResource(new Resource(vo.getResourceId()));
			}
			baseDao.saveOrUpdate(re);
		}
	}

	private String readFileToString(String name) throws IOException {
		String filePath = System.getProperty("webapp.root") + "WEB-INF/classes/init/db/" + name + ".json";
		String lines = FileUtils.readFileToString(new File(filePath), Charset.forName("UTF-8"));
		return lines;
	}
	
	public static void main(String[] args) throws Exception{
		RoleVo r = new RoleVo();
		r.setId("admin");
		r.setName("管理员");
		r.setSeq(1);
		
		String filePath = "E:\\dev\\code\\wangfan_2.1aaa\\wangfan\\wangfan-web\\src\\main\\resources\\init\\db/resource.json";
		String lines = FileUtils.readFileToString(new File(filePath), Charset.forName("UTF-8"));
		List<ResourceVo> resources = JSON.parseArray(lines, ResourceVo.class);
		for (ResourceVo resourceVo : resources) {
			ResourceVo resourceVo2 = new ResourceVo(resourceVo.getId());
			resourceVo2.setResources(null);
			r.getResources().add(resourceVo2);
		}
		System.out.println(JSON.toJSONString(r));

		UserVo u = new UserVo();
		u.setId("zhangsan");
		u.setUsername("张三");
		u.setMobilePhone("13611111111");
		u.setStatus(StatusEnum.认证通过);
		u.setPwd("123456");
		
		String filePath1 = "E:\\dev\\code\\wangfan_2.1aaa\\wangfan\\wangfan-web\\src\\main\\resources\\init\\db/role.json";
		String lines1 = FileUtils.readFileToString(new File(filePath1), Charset.forName("UTF-8"));
		List<RoleVo> roles = JSON.parseArray(lines1, RoleVo.class);
		for (RoleVo roleVo : roles) {
			RoleVo roleVo2 = new RoleVo(roleVo.getId());
			roleVo2.setResources(null);
			u.getRoles().add(roleVo2);
		}
		System.out.println(JSON.toJSONString(u));
	}
	
	
	
}
