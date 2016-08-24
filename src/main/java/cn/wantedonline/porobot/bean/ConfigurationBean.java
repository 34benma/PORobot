/**
 * Copyright [2016-2026] wangcheng(wantedonline@outlook.com)
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package cn.wantedonline.porobot.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import cn.wantedonline.porobot.Constant;

/**
 * @author wangcheng
 * @Date 2016年8月23日 下午4:37:37
 * @Desc:配置文件Bean
 * 配置文件解析校验后生成该bean
 */
public class ConfigurationBean {
	//对应配置文件中的globalUserName配置
	private String globalUserName;
	//对应配置文件中的globalPassword配置
	private String globalPassword;
	
	private boolean isSerialized;
	
	private String poSuffix;
	
	private String poPackage;
	
	private List<JDBCConfigBean> jdbcConfigs;
	
	private String driver;
	
	public String getGlobalUserName() {
		return globalUserName;
	}

	public String getGlobalPassword() {
		return globalPassword;
	}

	public List<JDBCConfigBean> getJdbcConfigs() {
		return jdbcConfigs;
	}

	public String getDriver() {
		return driver;
	}

	public boolean isSerialized() {
		return isSerialized;
	}

	public String getPoSuffix() {
		return poSuffix;
	}

	public String getPoPackage() {
		return poPackage;
	}
	
	public static String convertPackagePath(String poPackage) {
		if (Constant.DEFAULT_PO_PACKAGE.equals(poPackage)) {
			return poPackage;
		}
		String rtnStr = poPackage.replace(".", File.separator+File.separator);
		return rtnStr;
	}

	public ConfigurationBean(Properties configProperties) {
		this.driver = configProperties.getProperty(Constant.DRIVER);
		this.globalUserName = configProperties.getProperty(Constant.GLOBAL_USERNAME,"");
		this.globalPassword = configProperties.getProperty(Constant.GLOBAL_PASSWORD, "");
		this.poPackage = configProperties.getProperty(Constant.PO_PACKAGE, Constant.DEFAULT_PO_PACKAGE);
		this.poSuffix = configProperties.getProperty(Constant.PO_SUFFIX, Constant.DEFAULT_PO_SUFFIX);
		this.isSerialized = new Boolean(configProperties.getProperty(Constant.SERIALIZE, "true"));
		this.jdbcConfigs = getJDBCConfigBeans(configProperties);
	}
	
	//================================Private Method Area===============================
	
	private static List<JDBCConfigBean> getJDBCConfigBeans(Properties configProperties) {
		Set<String> keys = configProperties.stringPropertyNames();
		List<JDBCConfigBean> jdbcBeans = new ArrayList<>(4);
		for (String key : keys) {
			if (isJDBCUrl(key)) {
				String domain = key.substring(Constant.JDBC_URL_PREFIX.length());
				String url = configProperties.getProperty(key);
				String userName = configProperties.getProperty(Constant.JDBC_USERNAME_PREFIX+domain);
				if (null == userName) {
					userName = configProperties.getProperty(Constant.GLOBAL_USERNAME,"");
				}
				String password = configProperties.getProperty(Constant.JDBC_PASSWORD_PREFIX+domain);
				if (null == password) {
					password = configProperties.getProperty(Constant.GLOBAL_PASSWORD, "");
				}
				jdbcBeans.add(new JDBCConfigBean(url, userName, password));
			}
		}
		return jdbcBeans;
	}
	
	private static boolean isJDBCUrl(String str) {
		return str.startsWith(Constant.JDBC_URL_PREFIX);
	}
	
}
