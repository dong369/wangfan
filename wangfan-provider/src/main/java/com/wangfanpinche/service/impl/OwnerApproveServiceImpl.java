package com.wangfanpinche.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.Car;
import com.wangfanpinche.dto.OwnerApprove;
import com.wangfanpinche.dto.Car.CarStatusEnum;
import com.wangfanpinche.dto.OwnerApprove.OwnStatusEnum;
import com.wangfanpinche.service.OwnerApproveService;
import com.wangfanpinche.vo.OwnerApproveVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class OwnerApproveServiceImpl implements OwnerApproveService{

	@Autowired
	private BaseDao baseDao;
	
	@Override
	public Boolean hasOwnerApprove(OwnerApproveVo vo) {
		String hql = "select count(id) from OwnerApprove where deleted = ? and userId = ? and status = ? ";
		Long count = baseDao.count(hql, false, vo.getUserId(), OwnStatusEnum.认证通过);
		if (count == 0) {
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void OwnerToApprove(OwnerApproveVo vo) {
		OwnerApprove owner = baseDao.get(OwnerApprove.class, " from OwnerApprove where deleted = ? and userId = ? ", false,vo.getUserId());
		if (owner.getStatus().equals(OwnStatusEnum.认证中)) {
			throw new RuntimeException("您的车主认证信息正在后台进行审核，请等待!");
		}
		String hql = "update OwnerApprove set ownerName = ?, ownerIdNumber = ?, idenPhoto = ?, drivPhoto = ?, status = ?, firstDateTime = ?, effectiveStartDateTime = ?, effectiveEndDateTime = ?, recordDateTime = ?, modifyDateTime = ? where deleted = ? and userId = ?  ";
		baseDao.execute(hql, vo.getOwnerName(), vo.getOwnerIdNumber(), vo.getIdenPhoto(), vo.getDrivPhoto(), OwnStatusEnum.认证中, vo.getFirstDateTime(), vo.getEffectiveStartDateTime(), vo.getEffectiveEndDateTime(), vo.getRecordDateTime(), LocalDateTime.now(), false, vo.getUserId());
		
		Car car = new Car();
		com.wangfanpinche.provider.utils.BeanUtils.copySomeProperties(vo, car, "carInfoId","carNumber","carColor");
		car.setUserId(vo.getUserId());
		car.setCarUserName(vo.getCarUserName());
		car.setStatus(CarStatusEnum.认证中);
		baseDao.save(car);
	}

	@Override
	public BootstrapTable table(OwnerApproveVo vo, PageHelper ph) {
		BootstrapTable table = new BootstrapTable();		
		String hql = "select new com.wangfanpinche.vo.OwnerApproveVo(o.id, o.userId, o.ownerName, o.ownerIdNumber, o.idenPhoto, o.drivPhoto, ci.carmodel, c.carNumber, c.carColor, o.status) from OwnerApprove o "
				+ "left join Car c on o.userId = c.userId left join CarInfo ci on c.carInfoId = ci.id where o.deleted = :deleted ";
		Map<String, Object> param = new HashMap<>();
		param.put("deleted", false);
		table.setRows(baseDao.find(OwnerApproveVo.class, hql + " order by o.modifyDateTime desc ", param, ph));
		table.setTotal(baseDao.count("select count(id) from OwnerApprove where deleted = :deleted ", param));
		return table;
	}

	@Override
	public void updateOwnerApprove(OwnerApproveVo vo) {
		LocalDateTime now = LocalDateTime.now();
		String hql = "update OwnerApprove set status = ?,modifyDateTime = ? where userId = ? and deleted = ?　";
		baseDao.execute(hql, OwnStatusEnum.认证通过, now, vo.getId(), false);
		String chql = "update Car set status = ?,modifyDateTime = ? where userId = ? and deleted = ? ";
		baseDao.execute(chql, CarStatusEnum.认证通过, now, vo.getId(), false);
		
	}
}
