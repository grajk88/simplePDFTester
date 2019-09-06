package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test {

	public static void main(String[] args) {

		try {

			String fileName = System.getProperty("user.dir") + "/DataSheet.xlsx";

			XSSFWorkbook workbook = new XSSFWorkbook(fileName);

			XSSFSheet sheet = workbook.getSheet("Sheet1"); // Get data as per sheet name

			for (Row row : sheet) { // For each Row.

				Cell cell = row.getCell(0); // Get the Cell at the Index / Column you want.

				if (cell.getStringCellValue().equalsIgnoreCase("Invoice_2")) {

					System.out.println(cell.getRow().getLastCellNum());

					for (int i = 0; i <= cell.getRow().getLastCellNum() - 1; i++) {
						
						String columnName = cell.getSheet().getRow(0).getCell(i).getRichStringCellValue().toString();

						System.out.println(cell.getSheet().getRow(0).getCell(i).getRichStringCellValue().toString());

						System.out.println(cell.getRow().getCell(i));

						System.out.println("************");
						
						try (InputStream input = new FileInputStream(System.getProperty("user.dir") + "/Locators.properties")) {

							Properties prop = new Properties();

							prop.load(input);
							
							String coordinates = prop.getProperty(columnName);
							
							

							// prop.forEach((key, value) -> System.out.println(key + " : " + value));

						} catch (IOException ex) {
							ex.printStackTrace();
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
}