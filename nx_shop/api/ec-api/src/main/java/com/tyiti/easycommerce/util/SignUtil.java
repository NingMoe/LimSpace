package com.tyiti.easycommerce.util;


import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.cfca.util.pki.PKIException;
import com.cfca.util.pki.api.CertUtil;
import com.cfca.util.pki.api.KeyUtil;
import com.cfca.util.pki.api.SignatureUtil;
import com.cfca.util.pki.cert.X509Cert;
import com.cfca.util.pki.cipher.JCrypto;
import com.cfca.util.pki.cipher.JKey;
import com.cfca.util.pki.cipher.Session;
import com.tyiti.easycommerce.service.impl.CreditCardPayServiceImpl;

public class SignUtil {
	private Session session = null;
	private JCrypto jcrypto = null;
	private static Log logger = LogFactory.getLog(CreditCardPayServiceImpl.class);
	

	public SignUtil() {
		try {
			// 初始化加密库，获得会话session
			// 多线程的应用可以共享一个session,不需要重复
			// 初始化加密库并获得session。
			// 系统退出后要jcrypto.finalize()，释放加密库
			jcrypto = JCrypto.getInstance();
			jcrypto.initialize(JCrypto.JSOFT_LIB, null);
			session = jcrypto.openSession(JCrypto.JSOFT_LIB);

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	/**
	 * P7消息非分离式 签名 包含证书
	 */
	public String sign(String srcMessage, String pfxPath) {
		try {
			String pwd = "1";//保护证书私钥密码

			// 从私钥证书中获得签名者私钥
			// 也可以用getPriKey(byte[] pfxData, String pfxPWD)
			logger.info("电子证书路径为："+pfxPath);
			JKey priKey = KeyUtil.getPriKey(pfxPath, pwd);
			X509Cert cert = CertUtil.getCert(pfxPath, pwd);
			X509Cert[] certs = new X509Cert[1];
			certs[0] = cert;

			SignatureUtil signUtil = new SignatureUtil();
			// 对消息签名
			byte[] b64SignData = signUtil
					.p7SignMessage(true, srcMessage.getBytes("GBK"),
							SignatureUtil.SHA1_RSA, priKey, certs, session);

			return new String(b64SignData);
		} catch (PKIException ex) {
			logger.error("签名生成失败，失败原因为："+ex.toString());
			System.out.println(ex.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return null;
	}

	/**
	 * P7消息非分离式 验签 签名数据包含证书
	 */
	public String veryfy(String signedMessage) {
		try {
			SignatureUtil signUtil = new SignatureUtil();
			// 验证签名
			boolean verify = signUtil.p7VerifySignMessage(signedMessage
					.getBytes(), session);
			if (verify) {
			
				// 获得签名数据中的原文
				byte[] srcData = signUtil.getSignedContent();
				String result = new String(srcData);
				logger.info("验证消息(非分离式)签名成功,原文为:" + result);
				return new String(srcData);
			} else {
				logger.info("验证消息(非分离式)签名失败");
			}
		} catch (PKIException ex) {
			logger.error("验证消息签名失败，失败原因为："+ex.toString());
			System.out.println(ex.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return null;
	}
	
	
	public static void main(String[] args) throws Exception {
		SignUtil su = new SignUtil();

		/** ********签名接口调用************ */
		System.out.println("sign message testing ...");
		String signedMsg = su.sign("123qwe中文","");
		System.out.println("sign message testing ...ok");

		/** ********验签接口调用************ */
		System.out.println("verify signature testing ...");
		su.veryfy(signedMsg);
		System.out.println("verify signature testing ...ok");

	}

	 /**
	  * <p>功能描述：新东方推送接口加密。</p>	
	  * @param readySignStr
	  * @param key
	  * @param string
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月17日 下午4:57:01。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	public static  String sign(String readySignStr, String key, String input_charset) {
		readySignStr = readySignStr + key;
        return DigestUtils.md5Hex(getContentBytes(readySignStr, input_charset));
	}

	 /**
	  * <p>功能描述：。</p>	
	  * @param readySignStr
	  * @param input_charset
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月17日 下午4:58:41。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	private static byte[] getContentBytes(String readySignStr, String input_charset) {
		 if (StringUtils.isEmpty(input_charset)) {
	            return readySignStr.getBytes();
	        }
	        try {
	            return readySignStr.getBytes(input_charset);
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + input_charset);
	        }
	}
	/**
	  * <p>功能描述：。</p>	
	  * @param readySignStr
	  * @param sign
	  * @param input_charset
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月17日 下午5:01:20。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
    public  boolean verify(String readySignStr, String sign, String input_charset) {
    	readySignStr = readySignStr + sign;
        String mysign = DigestUtils.md5Hex(getContentBytes(readySignStr, input_charset));
        if(mysign.equals(sign)) {
            return true;
        }
        else {
            return false;
        }
    }
}