package JoS.JAVAProjects.Reporting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordDocumentManager {
	private final String filePath;
	private final String fileName;

	public WordDocumentManager(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}

	public void createDocument() {
		XWPFDocument document = new XWPFDocument();

		writeIntoFile(document);
	}

	private void writeIntoFile(XWPFDocument document) {
		String absoluteFilePath = this.filePath + Util.getFileSeparator()
				+ this.fileName + ".docx";
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified file \""
					+ absoluteFilePath + "\" does not exist!");
		}
		try {
			//FileOutputStream fileOutputStream;
			document.write(fileOutputStream);
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while writing into the specified Word document \""
							+ absoluteFilePath + "\"");
		}
	}

	public void addPicture(File pictureFile) {
		CustomXWPFDocument document = openFileForReading();
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);

		XWPFRun run = paragraph.createRun();
		run.setText(pictureFile.getName());
		try {
			String id = null;
			try {
				id = document.addPictureData(new FileInputStream(pictureFile), 6);
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedImage image = ImageIO.read(pictureFile);
			try {
				document.createPicture(id, document.getNextPicNameNumber(6),
						image.getWidth(), image.getHeight());
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Exception thrown while adding a picture file to a Word document");
		}
		String id;
		paragraph = document.createParagraph();
		run = paragraph.createRun();
		run.addBreak(BreakType.PAGE);

		writeIntoFile(document);
	}

	private CustomXWPFDocument openFileForReading() {
		String absoluteFilePath = this.filePath + Util.getFileSeparator()
				+ this.fileName + ".docx";
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified file \""
					+ absoluteFilePath + "\" does not exist!");
		}
		CustomXWPFDocument document;
		try {
			//FileInputStream fileInputStream;
			document = new CustomXWPFDocument(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while opening the specified Word document \""
							+ absoluteFilePath + "\"");
		}
		//CustomXWPFDocument document;
		return document;
	}
}