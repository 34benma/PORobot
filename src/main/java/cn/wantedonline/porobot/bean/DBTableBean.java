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

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangcheng
 * @Date 2016年8月24日 下午1:01:07
 * @Desc:
 *
 */
public class DBTableBean {
	private String databaseName;
	private String tableName;
	private List<DBColumn> columns = new ArrayList<DBColumn>();
	
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public DBTableBean(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName(int index) {
		return columns.get(index).getColumnName();
	}
	
	public String getColumnType(int index) {
		return columns.get(index).getColumnType();
	}
	
	public String getColumnComment(int index) {
		return columns.get(index).getColumnComment();
	}
	
	public int getColumnSize() {
		return columns.size();
	}

	public void addColumn(String columnName, String columnType, String columnComment) {
		columns.add(new DBColumn(columnName, columnType, columnComment));
	}

	class DBColumn {
		private String columnName;
		private String columnType;
		private String columnComment;
		
		public DBColumn(String columnName, String columnType, String columnComment) {
			this.columnName = columnName;
			this.columnType = columnType;
			this.columnComment = columnComment;
		}
		
		public String getColumnName() {
			return columnName;
		}
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		public String getColumnType() {
			return columnType;
		}
		public void setColumnType(String columnType) {
			this.columnType = columnType;
		}
		public String getColumnComment() {
			return columnComment;
		}
		public void setColumnComment(String columnComment) {
			this.columnComment = columnComment;
		}
		
	}

}
