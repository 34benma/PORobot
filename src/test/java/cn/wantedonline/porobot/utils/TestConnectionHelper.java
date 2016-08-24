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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.mysql.jdbc.Connection;

/**
 * @author wangcheng
 * @Date 2016年8月24日 上午11:18:49
 * @Desc:
 *
 */
public class TestConnectionHelper {
	private List<Connection> connections = null;
	
	@Test
	public void testGetConnections() {
		connections = ConnectionHelper.getConnections();
		assertNotNull(connections);
		assertEquals(1, connections.size());
	}
	
	@After
	public void tearDown() {
		ConnectionHelper.closeConnection(connections);
	}

}
