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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.wantedonline.porobot.Constant;
import cn.wantedonline.porobot.bean.ConfigurationBean;

/**
 * @author wangcheng
 * @Date 2016年8月23日 下午4:44:21
 * @Desc:
 *
 */
public class ConfigParser {
	
	//==================================Private Method Area================================
	private ConfigParser() {}
	
	/**
	 * 根据输入的文件路径获得配置文件
	 * @param filePath
	 * @return
	 */
	private static Properties readConfigFile(String filePath) {
		Properties rtnObject = new Properties();
		try(InputStream inStream = new FileInputStream(new File(filePath))) {
			rtnObject.load(inStream);
		} catch (FileNotFoundException e) {
			System.out.println("无法找到文件,文件路径不对,当前输入的文件路径为" + filePath);
		} catch (IOException e) {
			System.out.println("文件流错误");
		}
		return rtnObject;
	}
	
	/**
	 * 校验配置文件是否合法
	 * 
	 * @param configProperties
	 */
	private static void checkCofigProperties(Properties configProperties) {
		String errorMsg = "";
		if (configProperties.isEmpty()) {
			errorMsg = "配置文件为空";
			handleConfigError(errorMsg);
		}
			
		if (!configProperties.containsKey(Constant.DRIVER)) {
			errorMsg = "没有配置数据库驱动";
			handleConfigError(errorMsg);
		}
	}
	
	/**
	 * 配置文件错误，直接退出虚拟机
	 * @param errorMsg
	 */
	private static void handleConfigError(String errorMsg) {
		System.out.println(errorMsg);
		System.exit(0);
	}
	
	//================================Public Method Area===============================
	
	/**
	 * 从配置文件中获得一个合法的配置Bean
	 * @param configPath
	 * @return
	 */
	public static ConfigurationBean getConfigurationBean(String configPath) {
		Properties configProperties = readConfigFile(configPath);
		checkCofigProperties(configProperties);
		return new ConfigurationBean(configProperties);
	}

}
