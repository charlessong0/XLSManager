import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import DButil.DataBase;

public class CreateExcelFromDB {
	
	public ResultSet readDB(String userid) throws SQLException {
		String prepare1 = "select * from user";
		String prepare = "(select * from ((select userid, groupid from usergrouprelation where groupid in " +
				"(select groupid from usergrouprelation where userid = ?))a0 inner join " +
				"(select userID, blogscore, gitscore, finalescore from score)b0 on a0.userID = b0.userid ) )";
		ResultSet rs = null;
		
		
		DataBase db = new DataBase();
		PreparedStatement ps = db.getPreparedStatement(prepare);
		ps.setString(1, userid);
		
		rs = ps.executeQuery();
		
		return rs;
	}
	
	public void createExcel(ResultSet rs,String xlsName,String sheetName) throws InterruptedException {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			workbook.setSheetName(0,sheetName);
			HSSFRow row= sheet.createRow((short)0);;
			HSSFCell cell;
			ResultSetMetaData md=rs.getMetaData();
			int nColumn=md.getColumnCount();
			//写入各个字段的名称
			for(int i=1;i<=nColumn;i++) { 
				cell = row.createCell((short)(i-1));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(md.getColumnLabel(i));
			}
	
			int iRow=1;
			//写入各条记录，每条记录对应Excel中的一行
			while(rs.next())
			{row= sheet.createRow((short)iRow);;
			for(int j=1;j<=nColumn;j++)
			{ 
				cell = row.createCell((short)(j-1));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(rs.getObject(j).toString());
			}
			iRow++;
			}
			FileOutputStream fOut = new FileOutputStream(xlsName);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		
		}
		catch (Exception e) {
			
		}
		//Thread.sleep(3000);
		System.out.println("导出数据成功！");
	}
	
	public void testXLS() {
		try {
			String xlsFile="D:\\test.xls"; //产生的Excel文件的名称
			
			HSSFWorkbook workbook = new HSSFWorkbook(); //产生工作簿对象
			HSSFSheet sheet = workbook.createSheet(); //产生工作表对象
			//设置第一个工作表的名称为firstSheet
			//为了工作表能支持中文，设置字符编码为UTF_16
			workbook.setSheetName(0,"firstSheet");
			//产生一行
			HSSFRow row = sheet.createRow((short)0);
			//产生第一个单元格
			HSSFCell cell = row.createCell((short) 0);
			//设置单元格内容为字符串型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			//为了能在单元格中写入中文，设置字符编码为UTF_16。
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			//往第一个单元格中写入信息
			cell.setCellValue("测试成功2");
			FileOutputStream fOut = new FileOutputStream(xlsFile);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
			System.out.println("文件生成...");
			//以下语句读取生成的Excel文件内容
			FileInputStream fIn=new FileInputStream(xlsFile);
			HSSFWorkbook readWorkBook= new HSSFWorkbook(fIn);
			HSSFSheet readSheet= readWorkBook.getSheet("firstSheet");
			HSSFRow readRow =readSheet.getRow(0);
			HSSFCell readCell = readRow.getCell((short)0);
			System.out.println("第一个单元是：" + readCell.getStringCellValue()); 
			}
			catch (Exception e) {
				e.toString();
				System.out.println(e);
			}
	}
	
	
	public static void main(String args[]) throws SQLException, InterruptedException {
		CreateExcelFromDB createXLS = new CreateExcelFromDB();
		
		ResultSet rs = createXLS.readDB("12212010004");
		createXLS.createExcel(rs, "D:\\test3.xls", "sheet1");
		
		//createXLS.testXLS();
	}
}
