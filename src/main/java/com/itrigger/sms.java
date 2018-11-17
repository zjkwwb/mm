package com.itrigger;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.yytp.antf.constant.MyConstants;

/**
 * 
 * @ClassName: AliyunUtil 
 * @Description: 阿里云短信工具类
 * @author tianpengw 
 * @date 2017年9月17日 下午4:02:58 
 *
 */

public class sms {
	 private static Logger log = LogManager.getLogger(AliyunSmsUtil.class);
	    /**
	     * 
	     * @Description: 阿里云发送短信验证码
	     * @author tianpengw 
	     * @return void
	     */
	    public static void sendValidCodeMsg(String phone, String validCode){

	        try {
	            //设置超时时间-可自行调整
	            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
	            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
	            //初始化ascClient需要的几个参数
	            final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
	            final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
	            //替换成你的AK
	            final String accessKeyId = MyConstants.accessKeyId;//你的accessKeyId,参考本文档步骤2
	            final String accessKeySecret = MyConstants.accessKeySecret;//你的accessKeySecret，参考本文档步骤2
	            //初始化ascClient,暂时不支持多region
	            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
	            accessKeySecret);
	            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
	            IAcsClient acsClient = new DefaultAcsClient(profile);
	            //组装请求对象
	            SendSmsRequest request = new SendSmsRequest();
	            //使用post提交
	            request.setMethod(MethodType.POST);
	            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
	            request.setPhoneNumbers(phone);
	            //必填:短信签名-可在短信控制台中找到
	            request.setSignName(MyConstants.sms_signName);
	            //必填:短信模板-可在短信控制台中找到
	            request.setTemplateCode(MyConstants.sms_templateCode);
	            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
	            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
	            request.setTemplateParam("{\""+MyConstants.sms_contentParam+"\":\""+validCode+"\"}");
	            //可选-上行短信扩展码(无特殊需求用户请忽略此字段)
	            //request.setSmsUpExtendCode("90997");
	            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
	            request.setOutId("yourOutId");
	            //请求失败这里会抛ClientException异常
	            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
	            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
	                //请求成功
	                log.info("向手机号：" + phone + "，发送验证码：" + validCode + "成功！MessageId：" + sendSmsResponse.getRequestId());
	            }
	        } catch (ServerException e1) {
	            e1.printStackTrace();
	            log.warn("向手机号：" + phone + "，发送验证码：" + validCode + "失败！errCode：" + e1.getErrCode());
	        } catch (ClientException e1) {
	            e1.printStackTrace();
	            log.warn("向手机号：" + phone + "，发送验证码：" + validCode + "失败！errCode：" + e1.getErrCode());
	        } catch(Exception e){
	            log.warn("向手机号：" + phone + "，发送验证码：" + validCode + "失败！errMsg：" + MyStringUtils.getStackTrace(e));
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        AliyunSmsUtil.sendValidCodeMsg("18XXXXXXXX", "2256");
	    }
	
}

