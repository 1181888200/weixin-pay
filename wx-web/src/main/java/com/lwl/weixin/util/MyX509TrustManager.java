/**
 *  证书信任管理器
 *  2014-07-25
 */
package com.lwl.weixin.util;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 
 */
public class MyX509TrustManager implements X509TrustManager {
	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
			throws CertificateException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
			throws CertificateException {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}
}