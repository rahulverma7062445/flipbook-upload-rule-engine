package com.soprabanking.processor;

import java.awt.image.BufferedImage;
import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jodconverter.JodConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.jodconverter.office.OfficeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import com.soprabanking.pojo.FlipbookData;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import static com.soprabanking.utility.Const.LOG_PREFIX;

@Component
public class FlipbookProcessor {

	Logger logger = LoggerFactory.getLogger(FlipbookProcessor.class);
	@Autowired
	private FileOperations fileOperations ;

	@Value("${extension}")
	private String fileExtension;

	@Value("${sourceDirectory}")
	String sourceDirectory;
	@Value("${targetDirectory}")
	String targetDirectory; 

	@Value("${destinationDir}")
	private String destinationDir;
	
	String deleteServerDir;
	String editDir;
	String copyImagesFromDir;
	String copyImagesToDir;
	String fileCopyDst;
	String totalPageCount;

	String copyFileFrom;

	String copyFileTo;

	FlipbookData flipbookData;
	
	public ResponseEntity<String> configurePages(FlipbookData flipbookData) throws IOException {
		logger.info(LOG_PREFIX+" in configurePages");
		List imageNameAl = flipbookData.getImageNameAl();
		
		
		String fliphtml = "fliphtml5_pages: [";

        for (int i = 0; i <flipbookData.getTotalPageCount(); ++i) {
        	
        	fliphtml += "\r\n" + "{" + "\r\n" + "n: [" + '"'+ imageNameAl.get(i)+'"'+"]"+","+"\r\n"+"t: " + '"'+ "./files/large/" + imageNameAl.get(i)+'"'+","+"\r\n"+"},";
        }
        fliphtml += "\r\n" +"],";
		flipbookData.setFliphtml(fliphtml);
		processFlipbookData(flipbookData,fliphtml);
		logger.info(LOG_PREFIX+" out processFlipbookData");
		System.out.println("magazine name: "+flipbookData.getMagazineName());
		return new ResponseEntity<String>(flipbookData.getMagazineName(),HttpStatus.CREATED);
	}
	
	public void  processFlipbookData(FlipbookData magazineData, String fliphtml) throws IOException {
		logger.info(LOG_PREFIX+" in processFlipbookData");
		deleteServerDir = targetDirectory+magazineData.getMagazineName()+"/";
		copyFileFrom = deleteServerDir+"files/large/";
		System.out.println(copyFileFrom);
		editDir = targetDirectory+magazineData.getMagazineName()+"/"+"config.js";
		copyImagesFromDir = destinationDir+magazineData.getMagazineName()+"/";
		copyImagesToDir= targetDirectory+magazineData.getMagazineName()+"/"+"files/large/";
		fileCopyDst = targetDirectory+magazineData.getMagazineName()+"/";
	    totalPageCount="totalPageCount: "+magazineData.getTotalPageCount()+",";
		copyFileTo = deleteServerDir+"/files/images/";
		
		
		File deleteFile = new File(deleteServerDir);
		if(deleteFile.listFiles()!=null) {
			fileOperations.deleteDirectory(deleteFile);
			logger.info(LOG_PREFIX+"out deleteDirectory");
		}

		try {
			fileOperations.copyDirectory(sourceDirectory, fileCopyDst);
			logger.info(LOG_PREFIX+"out copyDirectory");
		} catch (IOException e) {
			e.printStackTrace();
		}

		fileOperations.replaceText(editDir,"totalPageCount: 8,",totalPageCount);
		logger.info(LOG_PREFIX+"out replaceText");
		fileOperations.replaceText(editDir,"fliphtml5_pages:",magazineData.getFliphtml());
		logger.info(LOG_PREFIX+"out replaceText");

		try {
			fileOperations.copyDirectory(copyImagesFromDir,copyImagesToDir);
			logger.info(LOG_PREFIX+"out copyDirectory");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(magazineData.getBgImageName()!=null) {
			System.out.println("this is bgimages: "+magazineData.getBgImageName());
			fileOperations.replaceText(editDir,"backGroundImgURL: \"files/images/background.jfif\",","backGroundImgURL: \"files/images/"+magazineData.getBgImageName()+"\",");
			logger.info(LOG_PREFIX+"out replaceText");
			fileOperations.deleteFile(copyFileFrom+magazineData.getBgImageName(), copyFileTo+"background.jfif");
			logger.info(LOG_PREFIX+"out deleteFile");
			fileOperations.copyFile(copyFileFrom+magazineData.getBgImageName(), copyFileTo+magazineData.getBgImageName());
			logger.info(LOG_PREFIX+"out copyFile");
		}
		if(magazineData.getLogoName()!=null) {
			System.out.println("this is logoName: "+magazineData.getLogoName());
			fileOperations.replaceText(editDir,"appLogoIcon: \"files/images/sopra-logo.svg\",","appLogoIcon: \"files/images/"+magazineData.getLogoName()+"\",");
			logger.info(LOG_PREFIX+"out replaceText");
			fileOperations.deleteFile(copyFileFrom+magazineData.getLogoName(), copyFileTo+"sopra-logo.svg");
			logger.info(LOG_PREFIX+"out deleteFile");
			fileOperations.copyFile(copyFileFrom+magazineData.getLogoName(), copyFileTo+magazineData.getLogoName());
			logger.info(LOG_PREFIX+"out copyFile");
		}
		File file = new File(destinationDir+"UploadedFiles/");
		if(file.listFiles()!=null) {
			fileOperations.deleteDirectory(file);
			logger.info(LOG_PREFIX+"out deleteDirectory");
		}
		File images = new File(copyImagesFromDir);
		if(images.listFiles()!=null) {
			fileOperations.deleteDirectory(images);
			logger.info(LOG_PREFIX+"out deleteDirectory");
		}


	}

	public void processPdfFile(File file){
		List generatedImageAl=new ArrayList<String>();
		if(file.getName().contains(".pdf")) {
			System.out.println("pdf file name is :"+file.getName());
			try {
				if ((file).exists()) {
					PDDocument document = PDDocument.load(file);
					PDFRenderer pdfRenderer = new PDFRenderer(document);
					System.out.println("source name is: "+file.getName());
					String pdfFileName = file.getName().replace(".pdf", "");
					pdfFileName = pdfFileName.replace(" ", "_");
					flipbookData.setMagazineName(pdfFileName);
					flipbookData.setImagePath(destinationDir);

					File destinationFile = new File(destinationDir+pdfFileName+"/");
					if (!destinationFile.exists()) {
						destinationFile.mkdir();
						logger.info("Folder Created -> "+ destinationFile.getAbsolutePath());
						System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
					}

					int numberOfPages = document.getNumberOfPages();
					flipbookData.setTotalPageCount(numberOfPages);

					logger.info("Total files to be converting -> "+ numberOfPages);
					System.out.println("Total files to be converting -> "+ numberOfPages);

					String fileName = "Slide";

					logger.info("This is the file extension: "+fileExtension);
					System.out.println("This is the file extension: "+fileExtension);

					int dpi = 300;

					for (int i = 0; i < numberOfPages; ++i) {

						File outPutFile = new File(destinationDir +pdfFileName+"/"+fileName + (i+1) +"."+ fileExtension);
						BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
						ImageIO.write(bImage, fileExtension, outPutFile);
						generatedImageAl.add(fileName + (i+1) +"."+ fileExtension);
						flipbookData.setImageNameAl(generatedImageAl);
					}

					document.close();
					System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
					System.out.println("Application closed");
					logger.info("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
				} else {
					logger.error(file.getName() +" File not exists");
					System.err.println(file.getName() +" File not exists");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public File processPptxFile(File pptxFile){

		String pptxFileName = pptxFile.getName().replace(".pptx", "");
		pptxFileName = pptxFileName.replace(" ", "_");
		OfficeManager officeManager = LocalOfficeManager.builder()
				.install()
				.officeHome("C:\\Program Files\\LibreOffice")
				.build();
		File outputFile = new File(destinationDir+pptxFileName+".pdf"+"/");
		try {

			// Start an office process and connect to the started instance (on port 2002).
			officeManager.start();

			// Convert
			JodConverter
					.convert(pptxFile)
					.to(outputFile)
					.execute();

		} catch (OfficeException e) {
			throw new RuntimeException(e);
		} finally {
			// Stop the office process
			OfficeUtils.stopQuietly(officeManager);
		}
		return outputFile;
	}

	public void processSvgFile(File logoFile){
			try {
				int random_number_1 = ThreadLocalRandom.current().nextInt();
				BufferedImage logo = ImageIO.read(logoFile);
				File logofile = new File(destinationDir+flipbookData.getMagazineName()+"/sopra-logo"+random_number_1+".png");
				File serchLogoDir = new File(destinationDir+flipbookData.getMagazineName());
				for (File f : serchLogoDir.listFiles()) {
					if (f.getName().startsWith("sopra-logo")) {
						f.delete();
					}
				}
				ImageIO.write(logo, "png", logofile);
				flipbookData.setLogoName(logofile.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(logoFile.getName());
	}

	public void processBgImageFile(File sourceFile){
		try {
			int random_number = ThreadLocalRandom.current().nextInt();
			BufferedImage bi = ImageIO.read(sourceFile);
			File outputfile = new File(destinationDir+flipbookData.getMagazineName()+"/background"+random_number+".jfif");
			File serchDir = new File(destinationDir+flipbookData.getMagazineName());
			for (File f : serchDir.listFiles()) {
				if (f.getName().startsWith("background")) {
					f.delete();
				}
			}
			ImageIO.write(bi, "png", outputfile);
			flipbookData.setBgImageName(outputfile.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(sourceFile.getName());
	}


	public FlipbookData processUploadedFiles(MultipartFile[] files) {
		flipbookData = new FlipbookData();
		try {
			List<String> fileNames = new ArrayList<>();
			Arrays.asList(files).stream().forEach(file -> {
				if(file.getOriginalFilename().contains(".pdf")) {
					System.out.println("pdf file name is :"+file.getOriginalFilename());
					try {
						System.out.println("file name: "+file.getOriginalFilename());
						File sourceFile = convertMultiPartToFile(file);
						processPdfFile(sourceFile);
						logger.info(LOG_PREFIX+" out convertMultiPartToFile");

					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				else if(file.getOriginalFilename().contains(".pptx")) {
					try {
						System.out.println("file name: "+file.getOriginalFilename());
						File sourceFile = convertMultiPartToFile(file);
						File pdfFile = processPptxFile(sourceFile);
						processPdfFile(pdfFile);
						logger.info(LOG_PREFIX+" out convertMultiPartToFile");

					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				else if(file.getOriginalFilename().contains(".svg")){
					try {
						System.out.println("file name: "+file.getOriginalFilename());
						File sourceFile = convertMultiPartToFile(file);
						processSvgFile(sourceFile);
						logger.info(LOG_PREFIX+" out convertMultiPartToFile");

					} catch (IOException e) {
						throw new RuntimeException(e);
					}

				}
				else{
					try {
						System.out.println("file name: "+file.getOriginalFilename());
						File sourceFile = convertMultiPartToFile(file);
						processBgImageFile(sourceFile);
						logger.info(LOG_PREFIX+" out convertMultiPartToFile");

					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				fileNames.add(file.getOriginalFilename());
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return flipbookData;
	}

	private File convertMultiPartToFile(MultipartFile file ) throws IOException {
		logger.info(LOG_PREFIX+"in convertMultiPartToFile");
		new File(destinationDir+"UploadedFiles/").mkdir();
		if(file.getOriginalFilename()==""){
			File convFile = new File( destinationDir+"UploadedFiles/"+file.getName());
			FileOutputStream fos = new FileOutputStream( convFile );
			fos.write( file.getBytes() );
			fos.close();
			return convFile;
		}
		else {
			File convFile = new File(destinationDir + "UploadedFiles/" + file.getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
			return convFile;
		}
	}
}
