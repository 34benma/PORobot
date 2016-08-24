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

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;

import cn.wantedonline.porobot.Constant;
import cn.wantedonline.porobot.bean.ConfigurationBean;
import cn.wantedonline.porobot.bean.JDBCConfigBean;

/**
 * @author wangcheng
 * @Date 2016年8月24日 上午11:04:40
 * @Desc:
 *
 */
public class ConnectionHelper {
	public static ConfigurationBean configBean = null;
	
	static {
		configBean = ConfigParser.getConfigurationBean(Constant.CONFIGPATH);
	}
	
	public static List<Connection> getConnections() {
		List<JDBCConfigBean> jdbcBeans = configBean.getJdbcConfigs();
		if (jdbcBeans.size() > 0) {
			List<Connection> rtnList = new ArrayList<>(jdbcBeans.size());
			for (JDBCConfigBean jdbcBean : jdbcBeans) {
				try {
					Connection connection = getConnection(jdbcBean);
					rtnList.add(connection);
				} catch (ClassNotFoundException e) {
					handleConfigError("驱动文件异常,退出...");
				} catch (SQLException e) {
					System.out.println("获取连接失败，jdbcConfigInfo: url" + jdbcBean.getUrl() + ", userName: " + jdbcBean.getUserName());
				}
			}
			return rtnList;
		}
		return null;
	}
	
	public static void closeConnection(List<Connection> connections) {
		for (Connection connection : connections) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("连接关闭异常... connection:" + connection.getHost());
			}
		}
	}
	
	//==============================================Private Method Area=========================
	
	/**
	 * 获取一个连接
	 * @param bean
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static Connection getConnection(JDBCConfigBean bean) throws ClassNotFoundException, SQLException {
		Class.forName(configBean.getDriver());
		return (Connection) DriverManager.getConnection(bean.getUrl(), bean.getUserName(),bean.getPassword());
	}
	
	private static void handleConfigError(String errorMsg) {
		System.out.println(errorMsg);
		System.exit(0);
	}
	
}
