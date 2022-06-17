package app.poi.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class WorkbookGeneratorService {

  public Workbook createExcelFile() {
    Workbook wb = new XSSFWorkbook();

    Sheet sheet = wb.createSheet("My Sheet");
    sheet.setDefaultColumnWidth(30);

    Row row = sheet.createRow(0);
    Cell cell = row.createCell(0);
    cell.setCellValue("Hello World");

    CellStyle style = wb.createCellStyle();
    style.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
    style.setBorderBottom(BorderStyle.THIN);

    Font font = wb.createFont();
    font.setColor(IndexedColors.GREEN.index);
    font.setBold(true);
    style.setFont(font);

    cell.setCellStyle(style);

    return wb;
  }

}
