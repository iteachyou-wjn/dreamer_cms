package cc.iteachyou.cms.thread;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import cc.iteachyou.cms.dao.DatabaseMapper;
import cc.iteachyou.cms.dao.SystemMapper;
import cc.iteachyou.cms.utils.FileConfiguration;
import cc.iteachyou.cms.utils.StringUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class BackupThread implements Runnable {
	private String LINE_CHARACTER = "\r\n";
	private FileConfiguration fileConfiguration;
	private SystemMapper systemMapper;
	private DatabaseMapper databaseMapper;
	private String[] tableNames;

	@Override
	public void run() {
		log.info("准备备份数据库...");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -3);
		String expiredFilePath = fileConfiguration.getResourceDir() + "backups/" + sdf.format(calendar.getTime());
		File expiredFile = new File(expiredFilePath);
		if(expiredFile.exists()) {
			expiredFile.delete();
		}
		
		
		for (int i = 0; i < tableNames.length; i++) {
			String tableName = tableNames[i];
			log.info("准备备份数据表["+ tableName +"]...");
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("/*").append(LINE_CHARACTER);
			sb.append("Dreamer CMS 数据库备份工具 时间：" + sdf.format(new Date())).append(LINE_CHARACTER);
			sb.append("*/").append(LINE_CHARACTER);
			
			sb.append(LINE_CHARACTER);
			
			sb.append("DROP TABLE IF EXISTS `" + tableName + "`;").append(LINE_CHARACTER);
			
			sb.append(LINE_CHARACTER);
			
			sb.append("/*").append(LINE_CHARACTER);
			sb.append("Table structure for table `" + tableName + "`").append(LINE_CHARACTER);
			sb.append("*/").append(LINE_CHARACTER);
			//查询数据库创建表语句
			Map<String, String> structMap = databaseMapper.showTableStruct(tableName);
			sb.append(structMap.get("Create Table")).append(";").append(LINE_CHARACTER);
			
			String database = databaseMapper.selectDatabase();
			List<Map<String,Object>> columns = databaseMapper.showTableColumns(database, tableName);
			
			List<Map<String,Object>> datas = databaseMapper.showAllDatas(database, tableName);
			
			sb.append(LINE_CHARACTER);
			sb.append("/*").append(LINE_CHARACTER);
			sb.append("Data for table `" + tableName + "`").append(LINE_CHARACTER);
			sb.append("*/").append(LINE_CHARACTER);
			
			for(int j = 0;j < datas.size();j++) {
				Map<String, Object> map = datas.get(j);
				sb.append("INSERT INTO `" + tableName + "`(");
				for(int c = 0;c < columns.size();c++) {
					sb.append(columns.get(c).get("COLUMN_NAME").toString());
					if(c != columns.size() - 1) {
						sb.append(",");
					}
				}
				sb.append(") VALUES (");
				
				for(int c = 0;c < columns.size();c++) {
					Map<String, Object> column = columns.get(c);
					String columnName = column.get("COLUMN_NAME").toString();
					String dataType = column.get("DATA_TYPE").toString();
					//文本型
					if("CHAR".equalsIgnoreCase(dataType)
						|| "VARCHAR".equalsIgnoreCase(dataType)
						|| "TINYBLOB".equalsIgnoreCase(dataType)
						|| "TINYTEXT".equalsIgnoreCase(dataType)
						|| "BLOB".equalsIgnoreCase(dataType)
						|| "TEXT".equalsIgnoreCase(dataType)
						|| "MEDIUMBLOB".equalsIgnoreCase(dataType)
						|| "MEDIUMTEXT".equalsIgnoreCase(dataType)
						|| "LOGNGBLOB".equalsIgnoreCase(dataType)
						|| "LONGTEXT".equalsIgnoreCase(dataType)) {
						sb.append("'").append(StringUtil.isBlank(map.get(columnName)) ? "" : map.get(columnName).toString()).append("'");
					}else if("TINYINT".equalsIgnoreCase(dataType)//整数型
						|| "SMALLINT".equalsIgnoreCase(dataType)
						|| "MEDIUMINT".equalsIgnoreCase(dataType)
						|| "INT".equalsIgnoreCase(dataType)
						|| "INTEGER".equalsIgnoreCase(dataType)
						|| "BIGINT".equalsIgnoreCase(dataType)) {
						sb.append(StringUtil.isBlank(map.get(columnName)) ? "" : map.get(columnName));
					}else if("FLOAT".equalsIgnoreCase(dataType)
						|| "DOUBLE".equalsIgnoreCase(dataType)
						|| "DECIMAL".equalsIgnoreCase(dataType)) {
						sb.append(StringUtil.isBlank(map.get(columnName)) ? "" : map.get(columnName));
					}else if("DATE".equalsIgnoreCase(dataType)
							|| "TIME".equalsIgnoreCase(dataType)
							|| "YEAR".equalsIgnoreCase(dataType)
							|| "DATETIME".equalsIgnoreCase(dataType)
							|| "TIMESTAMP".equalsIgnoreCase(dataType)) {
						if(StringUtil.isNotBlank(map.get(columnName))) {
							Date date = (Date)map.get(columnName);
							if(date != null) {
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String dateVal = simpleDateFormat.format(date);
								sb.append("STR_TO_DATE('" + dateVal + "','%Y-%m-%d %H:%i:%s')");
							}
						}
					}
					if(c != columns.size() - 1) {
						sb.append(",");
					}
				}
				
				sb.append(");").append(LINE_CHARACTER);
			}
			
			String fileName = sdf.format(new Date()) + "_" + tableName + ".sql";
			
			try {
				FileUtils.writeStringToFile(new File(fileConfiguration.getResourceDir() + "backups/" + sdf.format(new Date()) + "/" + fileName), sb.toString(), "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.info("数据表["+ tableName +"]备份完成...");
			
		}
	}

}
