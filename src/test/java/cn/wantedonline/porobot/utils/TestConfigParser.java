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
package cn.wantedonline.porobot.utils;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import cn.wantedonline.porobot.bean.ConfigurationBean;

/**
 * @author wangcheng
 * @Date 2016年8月24日 上午10:39:15
 * @Desc:
 *
 */
public class TestConfigParser {
	private static String configPath = null;
	@BeforeClass
	public static void init() {
		configPath = "datasource.properties";
	}
	
	@Test
	public void testGetConfigurationBean() {
		ConfigurationBean configBean = ConfigParser.getConfigurationBean(configPath);
		assertNotNull(configBean);
		assertEquals("com.mysql.jdbc.Driver", configBean.getDriver());
		assertEquals(1, configBean.getJdbcConfigs().size());
		assertEquals("root", configBean.getJdbcConfigs().get(0).getUserName());
	}

}
