package com.nevexis.report;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Report {

	private static Font font;
	private static Image img;

	static {
		font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
		try {
			img = Image.getInstance(
					Paths.get(ClassLoader.getSystemResource("thumbnail.png").toURI()).toAbsolutePath().toString());
		} catch (BadElementException | IOException | URISyntaxException e) {
			throw new Error(e);
		}
	}

	public static void getTXT(Object[] allObjects, OutputStream response) throws IOException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		validList(allObjects);
		
		try (PrintStream writer = new PrintStream(response)) {
			writer.println(String.format("Total %s Count : %d", allObjects[0].getClass().getName() ,allObjects.length));
			for (Object object : allObjects) {
				Field[] fields = object.getClass().getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					writer.print(field.get(object) + " ");
				}
				writer.println();
			}
		}
	}

	public static void getPDF(Object[] allObjects, OutputStream response) throws DocumentException, URISyntaxException,
			MalformedURLException, IOException, IllegalArgumentException, IllegalAccessException {
	
		validList(allObjects);
		
		Document document = new Document();

		img.scalePercent(
				((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / img.getWidth())
						* 100);
		Chunk chunk = new Chunk(String.format("Total %s: %d", allObjects[0].getClass().getSimpleName(), allObjects.length), font);
		PdfPTable table = new PdfPTable(allObjects[0].getClass().getDeclaredFields().length);
		addTableHeader(allObjects, table);
		addRows(allObjects, table);

		PdfWriter.getInstance(document, new BufferedOutputStream(response, 1024 * 1024 * 3));
		document.open();
		document.add(img);
		document.add(chunk);
		document.add(table);
		document.close();
	}

	public static void getXLSX(Object[] allObjects, OutputStream response)
			throws IOException, IllegalArgumentException, IllegalAccessException {
		
		validList(allObjects);
		
		try (Workbook workbook = new XSSFWorkbook();) {

			Sheet sheet = workbook.createSheet(allObjects[0].getClass().getSimpleName());
			sheet.setColumnWidth(0, 4000);
			sheet.setColumnWidth(1, 6000);

			Row objectsCount = sheet.createRow(0);
			Cell cell = objectsCount.createCell(0);
			cell.setCellValue(String.format("Total %s", allObjects[0].getClass().getName()));
			cell = objectsCount.createCell(1);
			cell.setCellValue(allObjects.length);

			Row header = sheet.createRow(1);

			Field[] fields = allObjects.getClass().getDeclaredFields();
			int cellCount = 0;
			for (Field field : fields) {
				Cell headerCell = header.createCell(cellCount);
				headerCell.setCellValue(field.getName());
				cellCount++;
			}
			for (int i = 0; i < allObjects.length; i++) {
				Field[] objectFields = allObjects[i].getClass().getDeclaredFields();
				Row row = sheet.createRow(i + 2);

				int index = 0;
				for (Field field : objectFields) {
					field.setAccessible(true);
					Cell newCell = row.createCell(index);
					newCell.setCellValue(String.valueOf(field.get(allObjects[i])));
					index++;
				}
			}
			workbook.write(response);
		}
	}

	public static void getZIP(Object[] allObjects, OutputStream response)
			throws IOException, DocumentException, URISyntaxException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {

		try (ZipOutputStream zipOut = new ZipOutputStream(response);) {

			ByteArrayOutputStream fileOutput = new ByteArrayOutputStream();
			getPDF(allObjects, fileOutput);
			zipOut.putNextEntry(new ZipEntry(String.format("%s.pdf", allObjects[0].getClass().getSimpleName())));
			zipOut.write(fileOutput.toByteArray());
			
			fileOutput.reset();
			getXLSX(allObjects, fileOutput);
			zipOut.putNextEntry(new ZipEntry(String.format("%s.xlsx", allObjects[0].getClass().getSimpleName())));
			zipOut.write(fileOutput.toByteArray());
			
			fileOutput.reset();
			getTXT(allObjects, fileOutput);
			zipOut.putNextEntry(new ZipEntry(String.format("%s.txt", allObjects[0].getClass().getSimpleName())));
			zipOut.write(fileOutput.toByteArray());
			
			zipOut.close();
		}
	}

	private static void addTableHeader(Object[] allObjects, PdfPTable table) {
		for (Field field : allObjects[0].getClass().getDeclaredFields()) {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(field.getName()));
			table.addCell(header);
		}
	}

	private static void addRows(Object[] allObjects, PdfPTable table)
			throws IllegalArgumentException, IllegalAccessException {
		for (Object object : allObjects) {
			Field[] fields = object.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				table.addCell(String.valueOf(field.get(object)));
			}
		}
	}
	
	private static void validList(Object[] allObjects) {
		if(allObjects.length <= 0 || allObjects == null) {
			throw new Error("Empty list");
		}
	}
}