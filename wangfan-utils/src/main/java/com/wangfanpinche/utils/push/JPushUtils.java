

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 极光推送工具类
 * 
 * @author 马凯
 *
 */
public class JPushUtils {
	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("utils/push/jPush");
	
	
	private static final Logger logger = LoggerFactory.getLogger(JPushUtils.class);
	
	/**
	 * 根据别名发送
	 */
	public static void sendByAlias(String content, Map<String, String> params, String userId) {
		userId = userId.replaceAll("-", "");
		ClientConfig clientConfig = ClientConfig.getInstance();
		JPushClient jpushClient = new JPushClient(getMmasterSecret(), getAppKey(), null, clientConfig);
		List<String> users = new ArrayList<>();
		users.add(userId);
		PushPayload payload = buildPushObjectByAlias(content, params, userId);
		buildPushObjectByAlias(content, params, userId);
		try {
			PushResult result = jpushClient.sendPush(payload);
			logger.info("Got result - " + result);

		} catch (APIConnectionException e) {
			logger.error("连接错误. 请过会儿重试. ", e);

		} catch (APIRequestException e) {
			logger.error("Error response from JPush server. Should review and fix it. ", e);
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Code: " + e.getErrorCode());
			logger.info("Error Message: " + e.getErrorMessage());
			logger.info("Msg ID: " + e.getMsgId());
		}
	}

	private static PushPayload buildPushObjectByAlias(String content, Map<String, String> params, String userId) {
		PushPayload.Builder builder = PushPayload.newBuilder();
		builder.setPlatform(Platform.all());
		builder.setAudience(Audience.alias(userId));

		Notification.Builder notificationBuilder = Notification.newBuilder();
		notificationBuilder.setAlert(content);
		notificationBuilder.addPlatformNotification(IosNotification.newBuilder().setAlert(content).setSound("happy").addExtras(params).build());
		notificationBuilder.addPlatformNotification(AndroidNotification.newBuilder().setTitle("系统推送").setAlert(content).addExtras(params).build());

		builder.setNotification(notificationBuilder.build());
		return builder.build();
	}
	
	private static final String getAppKey(){
		return bundle.getString("appKey");
	}
	
	private static final String getMmasterSecret(){
		return bundle.getString("masterSecret");
	}

}
