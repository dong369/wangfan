package com.wangfanpinche.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.PushEntity;
import com.wangfanpinche.dto.PushEntity.PushType;
import com.wangfanpinche.provider.utils.BeanUtils;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.UserPush;
import com.wangfanpinche.service.UserPushService;
import com.wangfanpinche.utils.push.JPushUtils;
import com.wangfanpinche.vo.PushEntityVo;
import com.wangfanpinche.vo.base.PageHelper;

@Service
public class UserPushServiceImpl implements UserPushService{
	
	@Autowired
	private BaseDao baseDao;

	@Override
	public void read(PushEntityVo vo) {
		LocalDateTime now = LocalDateTime.now();		
		String hql = " update UserPush set read = ?, readDateTime = ?, modifyDateTime = ? where id = ? and toUser.id = ? ";
		baseDao.execute(hql, true, now, now, vo.getUserpushId(), vo.getToUserId());
	}

	@Override
	public void sync(String userId) {
		LocalDateTime now = LocalDateTime.now();
		String sql = "select pe.id from t_pushentity pe where pe.type = :type and pe.endDateTime >= :now and pe.id NOT in "
				+ "( "
				+	"select up.pushEntity_ID from t_userpush up "
				+ 	"left join t_pushentity pe1 on pe1.ID = up.pushEntity_ID "
				+	"where up.toUser_ID = :userId and pe1.type = :type and pe1.endDateTime >= :now "
				+ ")  ";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("type", 0);
		params.put("now", now);
		List<PushEntityVo> list = baseDao.findBySql(PushEntityVo.class, sql, params);
		if (list != null && list.size() > 0) {
			for (PushEntityVo p : list) {
				PushEntity pushEntity = baseDao.getById(PushEntity.class, p.getId());
				UserPush up = new UserPush();
				up.setPushEntity(pushEntity);
				up.setToUser(new User(userId));
				up.setRead(false);
				baseDao.save(up);
			}
		}
	}

	@Override
	public void noread(PushEntityVo vo) {
		LocalDateTime now = LocalDateTime.now();
		String hql = " select new com.wangfanpinche.vo.PushEntityVo(p.id, u.id) from UserPush u left join u.pushEntity p where u.deleted = ? and u.toUser.id = ? and u.read = ? and p.type = ? and p.endDateTime > ? ";
		List<PushEntityVo> pList = baseDao.find(PushEntityVo.class, hql, false, vo.getToUserId(), false, PushType.SYSTEM, now);
		if (pList != null && pList.size() > 0) {
			for (PushEntityVo p : pList) {
				
				PushEntity push = baseDao.getById(PushEntity.class, p.getId());				
				UserPush u = baseDao.getById(UserPush.class, p.getUserpushId());				
				
				PushEntityVo pvo = new PushEntityVo();
				BeanUtils.copySomeProperties(push, pvo, "type","title","pageUrl","bannerUrl","startDateTime","endDateTime");
				pvo.setUserpushId(u.getId());
				
				Map<String, String> params = new HashMap<>();
				params.put("entity", JSON.toJSONString(pvo));
				JPushUtils.sendByAlias(push.getTitle(), params, u.getToUser().getId());
			}
		}
	}

	@Override
	public List<PushEntityVo> list(PushEntityVo vo, PageHelper ph) {
		String hql = " select new com.wangfanpinche.vo.PushEntityVo( p.id, p.title, p.pageUrl, p.bannerUrl, p.startDateTime, p.endDateTime, u.id, u.read, u.readDateTime, p.bizNumber) from UserPush u left join u.pushEntity p "
				+ "where u.deleted = ? and u.toUser.id = ? and p.type = ? order by p.endDateTime desc ";
		List<PushEntityVo> pushList = baseDao.find(PushEntityVo.class, hql, ph, false, vo.getToUserId(), PushType.SYSTEM);
		return pushList;
	}

}
