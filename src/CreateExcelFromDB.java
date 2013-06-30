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
	
	public ResultSet readDB() throws SQLException {
		String prepare = "select * from user";
		ResultSet rs = null;
		
		
		DataBase db = new DataBase();
		PreparedStatement ps = db.getPreparedStatement(prepare);
		
		rs = ps.executeQuery();
		
		return rs;
	}
	
	public void createExcel(ResultSet rs,String xlsName,String sheetName) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			workbook.setSheetName(0,sheetName);
			HSSFRow row= sheet.createRow((short)0);;
			HSSFCell cell;
			ResultSetMetaData md=rs.getMetaData();
			int nColumn=md.getColumnCount();
			//д������ֶε�����
			for(int i=1;i<=nColumn;i++) { 
				cell = row.createCell((short)(i-1));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(md.getColumnLabel(i));
			}
	
			int iRow=1;
			//д�������¼��ÿ����¼��ӦExcel�е�һ��
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
		
		System.out.println("�������ݳɹ���");
	}
	
	public void testXLS() {
		try {
			String xlsFile="D:\\test.xls"; //������Excel�ļ�������
			
			HSSFWorkbook workbook = new HSSFWorkbook(); //��������������
			HSSFSheet sheet = workbook.createSheet(); //�������������
			//���õ�һ�������������ΪfirstSheet
			//Ϊ�˹�������֧�����ģ������ַ�����ΪUTF_16
			workbook.setSheetName(0,"firstSheet");
			//����һ��
			HSSFRow row = sheet.createRow((short)0);
			//������һ����Ԫ��
			HSSFCell cell = row.createCell((short) 0);
			//���õ�Ԫ������Ϊ�ַ�����
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			//Ϊ�����ڵ�Ԫ����д�����ģ������ַ�����ΪUTF_16��
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			//����һ����Ԫ����д����Ϣ
			cell.setCellValue("���Գɹ�2");
			FileOutputStream fOut = new FileOutputStream(xlsFile);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
			System.out.println("�ļ�����...");
			//��������ȡ���ɵ�Excel�ļ�����
			FileInputStream fIn=new FileInputStream(xlsFile);
			HSSFWorkbook readWorkBook= new HSSFWorkbook(fIn);
			HSSFSheet readSheet= readWorkBook.getSheet("firstSheet");
			HSSFRow readRow =readSheet.getRow(0);
			HSSFCell readCell = readRow.getCell((short)0);
			System.out.println("��һ����Ԫ�ǣ�" + readCell.getStringCellValue()); 
			}
			catch (Exception e) {
				e.toString();
				System.out.println(e);
			}
	}
	
	
	public static void main(String args[]) throws SQLException {
		CreateExcelFromDB createXLS = new CreateExcelFromDB();
		
		ResultSet rs = createXLS.readDB();
		createXLS.createExcel(rs, "D:\\test3.xls", "sheet1");
		
		//createXLS.testXLS();
	}
}
