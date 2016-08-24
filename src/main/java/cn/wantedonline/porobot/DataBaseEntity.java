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
package cn.wantedonline.porobot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.mysql.jdbc.Connection;

import cn.wantedonline.porobot.bean.ConfigurationBean;
import cn.wantedonline.porobot.bean.DBTableBean;
import cn.wantedonline.porobot.utils.ConnectionHelper;

/**
 * @author wangcheng
 * @Date 2016年8月24日 上午11:37:41
 * @Desc:
 *
 */
public class DataBaseEntity {
	private static boolean hasDateClass = false;
	private static ConfigurationBean configBean = ConnectionHelper.configBean;
	
	/**
	 * 
	 * @param genDBDir 是否生成对应数据库的数据字典文档
	 */
	public static void genDataBasePOFile(boolean genDBDir) {
		List<Connection> connections = ConnectionHelper.getConnections();

		for (Connection connection : connections) {
			List<String> tableNames = getAllTableName(connection);
			List<DBTableBean> tableBeanList = new ArrayList<>(tableNames.size());
			for (String tableName : tableNames) {
				hasDateClass = false;
				DBTableBean tableBean = getTableBean(tableName, connection);
				tableBeanList.add(tableBean);
				String content = parseTable(tableBean);
				try {
					String outputPath = System.getProperty("user.dir") + File.separator + ConfigurationBean.convertPackagePath(configBean.getPoPackage()) + File.separator;
					new File(outputPath).mkdirs();
					outputPath += tableName+configBean.getPoSuffix()+".java";
					FileWriter fw = new FileWriter(new File(outputPath));
					PrintWriter pw = new PrintWriter(fw);
					pw.println(content);
					pw.flush();
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (genDBDir) {
				try {
					genDBDictionary(tableBeanList);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("生成数组字典时出错...出错数据库: " + tableBeanList.get(0).getDatabaseName());
				}
			}
		}

		ConnectionHelper.closeConnection(connections);
	}
	
	//================================Private Method===========================
	
	/**
	 * @param tableName
	 * @param connection
	 * @return
	 */
	private static DBTableBean getTableBean(String tableName, Connection connection) {
		DBTableBean rtnBean = null;
		try {
			rtnBean = new DBTableBean(tableName);
			String schema = connection.getCatalog();
			rtnBean.setDatabaseName(schema);
			ResultSet resultSet = connection.getMetaData().getColumns(schema, "", tableName, "");
			
			while (resultSet.next()) {
				String columnName = resultSet.getString("COLUMN_NAME");
				String columnType = resultSet.getString("TYPE_NAME");
				if (columnType.equalsIgnoreCase("datetime")) {
					hasDateClass = true;
				}
				String columnComment = resultSet.getString("REMARKS");
				rtnBean.addColumn(columnName, columnType, columnComment);
			}
			
			return rtnBean;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("获取全部列出错... tableName: " + tableName);
		}
		
		return rtnBean;
	}
	
	/**
	 * 
	 * @param connection
	 * @return
	 */
	private static List<String> getAllTableName(Connection connection) {
		try {
			String schema = connection.getCatalog();
			String sql = "select table_name from information_schema.tables where table_schema='" + schema + "' and table_type='base table'";
			Statement stat = connection.createStatement();
			ResultSet resultSet = stat.executeQuery(sql);
			List<String> rtnList = new ArrayList<String>();
			while (resultSet.next()) {
				String tableName = resultSet.getString(1);
				rtnList.add(tableName);
			}
			return rtnList;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("获取全部表名出错...");
		}
		return null;
	}
	
	/**
	 * 功能：获得列的数据类型
	 * @param sqlType
	 * @return
	 */
	private static String sqlType2JavaType(String sqlType) {
		
		if(sqlType.equalsIgnoreCase("bit")){
			return "boolean";
		}else if(sqlType.equalsIgnoreCase("tinyint")){
			return "int";
		}else if(sqlType.equalsIgnoreCase("smallint")){
			return "int";
		}else if(sqlType.equalsIgnoreCase("int")){
			return "int";
		}else if(sqlType.equalsIgnoreCase("bigint")){
			return "long";
		}else if(sqlType.equalsIgnoreCase("BIGINT UNSIGNED")){
			return "long";
		}else if(sqlType.equalsIgnoreCase("float")){
			return "float";
		}else if(sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric") 
				|| sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money") 
				|| sqlType.equalsIgnoreCase("smallmoney")){
			return "double";
		}else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") 
				|| sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar") 
				|| sqlType.equalsIgnoreCase("text")){
			return "String";
		}else if(sqlType.equalsIgnoreCase("datetime")){
			return "Date";
		}else if(sqlType.equalsIgnoreCase("image")){
			return "byte";
		}
		return null;
	}
	
	/**
	 * 功能：生成实体类主体代码
	 * @param colnames
	 * @param colTypes
	 * @param colSizes
	 * @return
	 */
	private static String parseTable(DBTableBean tableBean) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("package " + ConnectionHelper.configBean.getPoPackage() + ";\r\n");
		
		if (hasDateClass) {
			sb.append("import java.util.Date;\r\n");
		}
		
		sb.append("\r\n");
		//注释部分
		sb.append("   /**\r\n");
		sb.append("    * "+tableBean.getTableName()+" 实体类\r\n");
		sb.append("    * "+new Date()+" By PORobot \r\n");
		sb.append("    */ \r\n");
		//实体部分
		sb.append("\r\n\r\npublic class " + initcap(tableBean.getTableName()) + ConnectionHelper.configBean.getPoSuffix() + "{\r\n");
		processAllAttrs(sb, tableBean);//属性
		sb.append("\r\n\r\n");
		processAllMethod(sb, tableBean);//get set方法
		sb.append("}\r\n");
		return sb.toString();
	}
	
	/**
	 * 功能：生成所有属性
	 * @param sb
	 */
	private static void processAllAttrs(StringBuffer sb, DBTableBean tableBean) {
		
		for (int i = 0; i < tableBean.getColumnSize(); i++) {
			sb.append("\t/**" + tableBean.getColumnComment(i) + "*/\r\n");
			sb.append("\tprivate " + sqlType2JavaType(tableBean.getColumnType(i)) + " " + tableBean.getColumnName(i) + ";\r\n\r\n");
		}
		
	}

	/**
	 * 功能：生成所有方法
	 * @param sb
	 */
	private static void processAllMethod(StringBuffer sb, DBTableBean tableBean) {
		
		for (int i = 0; i < tableBean.getColumnSize(); i++) {
			sb.append("\tpublic void set" + initcap(tableBean.getColumnName(i)) + "(" + sqlType2JavaType(tableBean.getColumnType(i)) + " " + 
					tableBean.getColumnName(i) + "){\r\n");
			sb.append("\t\tthis." + tableBean.getColumnName(i) + "=" + tableBean.getColumnName(i) + ";\r\n");
			sb.append("\t}\r\n\r\n");
			sb.append("\tpublic " + sqlType2JavaType(tableBean.getColumnType(i)) + " get" + initcap(tableBean.getColumnName(i)) + "(){\r\n");
			sb.append("\t\treturn " + tableBean.getColumnName(i) + ";\r\n");
			sb.append("\t}\r\n\r\n");
		}
		
	}
	
	/**
	 * 功能：将输入字符串的首字母改成大写
	 * @param str
	 * @return
	 */
	private static String initcap(String str) {
		
		char[] ch = str.toCharArray();
		if(ch[0] >= 'a' && ch[0] <= 'z'){
			ch[0] = (char)(ch[0] - 32);
		}
		
		return new String(ch);
	}
	
	private static void genDBDictionary(List<DBTableBean> tableBeans) throws IOException {
		XWPFDocument doc = new XWPFDocument();
		String databaseName = "default_database";
		for (DBTableBean bean : tableBeans) {
			databaseName = bean.getDatabaseName();
			XWPFParagraph p0 = doc.createParagraph();
			p0.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun r0 = p0.createRun();
			r0.setBold(true);
			r0.setText(databaseName);
			r0.setFontSize(16);
			XWPFParagraph p1 = doc.createParagraph();
			p1.setAlignment(ParagraphAlignment.LEFT);
			XWPFRun r1 = p1.createRun();
			r1.setBold(true);
			r1.setText("Table:" + bean.getTableName());
			XWPFTable table = doc.createTable();
			int pos = 0;
			XWPFTableRow row1 = table.insertNewTableRow(pos++);
			XWPFTableCell cell1 = row1.createCell();
			cell1.setText("Field");
			cell1.setColor("FFE4C4");
			XWPFTableCell cell2 = row1.createCell();
			cell2.setText("Type");
			cell2.setColor("FFE4C4");
			XWPFTableCell cell3 = row1.createCell();
			cell3.setText("Comment");
			cell3.setColor("FFE4C4");
			for (int i = 0; i < bean.getColumnSize(); i++) {
				XWPFTableRow row = table.insertNewTableRow(pos++);
				XWPFTableCell cell11 = row.createCell();
				cell11.setText(bean.getColumnName(i));
				XWPFTableCell cell22 = row.createCell();
				cell22.setText(bean.getColumnType(i));
				XWPFTableCell cell33 = row.createCell();
				cell33.setText(bean.getColumnComment(i));
			}
			table.removeRow(pos);
			XWPFParagraph p2 = doc.createParagraph();
			XWPFRun r2 = p2.createRun();
			r2.addBreak();
		}
		
		FileOutputStream out = new FileOutputStream(databaseName + ".docx");
        doc.write(out);
        out.close();
        doc.close();
		
	}
	
	public static void main(String[] args) {
		genDataBasePOFile(true);
	}
	
}
