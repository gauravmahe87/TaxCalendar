package com.xid.taxcalendar;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

public class ExcelUpdate {
    public FileInputStream fis = null;
    public  FileOutputStream fileOut =null;
    private Workbook wb;
    private HSSFWorkbook workbook = null;
    private HSSFSheet sheet = null;
    private HSSFRow row   =null;
    private HSSFCell cell = null;

    public ExcelUpdate()
    {
        try {
            File newFile = new File("/Users/gaurav.maheshwari/Desktop/TaxCalendar.xls");
            if (newFile.exists()) {
                wb = WorkbookFactory.create(newFile);
            } else {
                if (newFile.getName().endsWith(".xls")) {
                    wb = new HSSFWorkbook();
                }
                else {
                    throw new IllegalArgumentException("I don't know how to create that kind of new file");
                }
            }
            fis = new FileInputStream(newFile);
            workbook = new HSSFWorkbook(fis);
            sheet = workbook.createSheet(String.valueOf((Calendar.getInstance().get(Calendar.YEAR))));
            //fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getRowCount(){
        if(workbook.getSheet(String.valueOf(sheet))!=null){
            int number=sheet.getLastRowNum()+1;
            return number;
        }
        return 0;
    }

    public boolean setCellData(int rowNum, int colNum, String data){
        try{
            if(rowNum<=0){
                return false;
            }
            int index = workbook.getSheetIndex(sheet);
            if(index==-1){
                return false;
            }

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum);
            if (row == null){
                row = sheet.createRow(rowNum);
            }
            cell = row.getCell(colNum);
            if (cell == null){
                cell = row.createCell(colNum);
            }
            cell.setCellValue(data);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
