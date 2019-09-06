package application;

import java.io.File;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Controller {

	@FXML
	private Button uploadPDFButton; // name should be same as the fx:id

	@FXML
	private TextField apikey; // name should be same as the fx:id

	@FXML
	private TextField pdfLocation;

	@FXML
	private TextField dataSheetLocation;

	@FXML
	private CheckBox magicCheck;

	@FXML
	private CheckBox dataDrivenTesting;

	@FXML
	private CheckBox visualCheck;

	@FXML
	private Text message;

	@FXML
	private ImageView loadingGif;

	@FXML
	private Text message1;

	private static ExtentReports extent;

	@FXML
	public void uploadPDFDocument(ActionEvent event) throws Exception {

		try {

			// Extent Reports

			extent = new ExtentReports(System.getProperty("user.dir") + "//Test-Report.html", true);

			// Form Handling

			String apiKeyString = apikey.getText().toString();

			String pdfLocationString = pdfLocation.getText().toString();

			String dataSheetPath = dataSheetLocation.getText().toString();

			// String filepath = System.getProperty("user.dir") + "/pdfs/Invoice_1.pdf";

			String filepath = pdfLocation.getText().toString();

			String fileName = null;

			File file = new File(filepath);

			File[] files = null;

			if (filepath.contains(".pdf")) {

				fileName = file.getName().replaceAll(".pdf", "");

			} else {

				files = file.listFiles();

				for (File f : files) {

					System.out.println(f.getName());
				}

			}

			String dataSheetName = null;

			loadingGif.setVisible(true);
			message1.setVisible(true);

			/*
			 * PDF Stripper Handler For Ad-hoc PDF Validation
			 */

			try {

				if (magicCheck.isSelected()) {

				}

				// If Magic Check is NOT Selected

				else {

					for (File f : files) {
						
						ExtentTest test = extent.startTest(f.getName(), f.getName());
						
						test.log(LogStatus.PASS, "File Name: " +f.getName());
						
						fileName = f.getName().replace(".pdf", "");
						
						// Loading an existing document
						PDDocument document = PDDocument.load(f);

						// Instantiate PDFTextStripper class
						PDFTextStripper pdfStripper = new PDFTextStripper();

						// Retrieving text from PDF document
						String strippedText = pdfStripper.getText(document);

						System.out.println("Stripped Text: " + strippedText);

						// dataSheetName = System.getProperty("user.dir") + "/DataSheet.xlsx";
						
						dataSheetName = dataSheetPath;

						XSSFWorkbook workbook = new XSSFWorkbook(dataSheetName);

						XSSFSheet sheet = workbook.getSheet("Sheet1"); // Get data as per sheet name

						boolean result;

						String textToBeValidated;

						String columnName;

						for (Row row : sheet) { // For each Row.

							Cell cell = row.getCell(0); // Get the Cell at the Index / Column you want.

							if (cell.getStringCellValue().equalsIgnoreCase(fileName)) {

								for (int i = 1; i <= cell.getRow().getLastCellNum() - 1; i++) {

									columnName = cell.getSheet().getRow(0).getCell(i).getRichStringCellValue()
											.toString();

									textToBeValidated = cell.getRow().getCell(i).toString();

									try {
										
										result = strippedText.contains(textToBeValidated);

										if (result == true) {

											test.log(LogStatus.PASS, textToBeValidated + " is available");

										} else {

											test.log(LogStatus.FAIL, "<b>" + textToBeValidated + "</b> is unavailable");

										}

									} catch (Exception e) {

										e.printStackTrace();

									}

								}
							}

						}

						loadingGif.setVisible(false);
						message1.setVisible(false);

						message.setVisible(true);

						message.setText("Test Execution Completed. Check the Reports...");

						// Closing the document

						document.close();
						
						extent.endTest(test);

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				message.setVisible(true);

				message.setText(
						"Test Execution NOT Completed. There was some error. Please contact the Administrator.");
			}

			// Visual Check Begins

			if (visualCheck.isSelected()) {

				try {

					apiKeyString = "H98dL101AYfejk59Dm32uZABni105YI3nVADSMMvMb35CK5Y110";

					String command = String.format("java -jar /Users/giridhar/Downloads/ImageTester.jar -k "
							+ apiKeyString + " -f " + filepath);

					Process process = Runtime.getRuntime().exec(command);

					process.waitFor();

					String stream = IOUtils.toString(process.getInputStream(), "UTF-8");

					if (stream != null && stream.contains("Mismatch")) {

						message.setVisible(true);

						message.setText("Mismatches Found. Navigate to Applitools to view the results...");

					} else if (stream != null && stream.contains("Nothing to test!")) {

						message.setVisible(true);

						message.setText("There is some issue in Uploading the PDF. Can you check the path of the PDF?");

					} else if (stream != null && stream.contains("[New]") || stream.contains("[Passed]")) {

						message.setVisible(true);

						message.setText("Upload Completed...");

					}

				} catch (Exception e) {

					message.setVisible(true);

					message.setText(
							"There is some Problem in uploading the PDF. Try again and if issue persists, contact QE Enablers");

				}

			}

			// Visual Check Ends

			extent.flush();
		}

		catch (

		Exception e) {
			System.out.println(e);
		}

	}

}
