package com.flipbook.processor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.flipbook.utility.Const.LOG_PREFIX;

@Component
public class FileOperations {
	Logger logger = LoggerFactory.getLogger(FileOperations.class);

	public void deleteDirectory(File file) {
		logger.info(LOG_PREFIX+"in deleteDirectory");
		for (File subfile : file.listFiles()) {

			if (subfile.isDirectory()) {
				deleteDirectory(subfile);
			}
			subfile.delete();
		}
		file.delete();
	}

	public void copyDirectory(String sourceDirectory, String targetDirectory) throws IOException {
		logger.info(LOG_PREFIX+"in copyDirectory");
		Path sourceLocation= Paths.get(sourceDirectory);
	    Path targetLocation =Paths.get(targetDirectory);

	    CustomFileVisitor fileVisitor = new CustomFileVisitor(sourceLocation, targetLocation);
	    
	    Files.walkFileTree(sourceLocation, fileVisitor);
		logger.info(LOG_PREFIX+" out CustomFileVisitor");
	    //fileVisitor.copyDir(sourceDirectory, targetDirectory);
        
	}

	public void deleteFile(String copyFileFrom,String copyFileTo){
		logger.info(LOG_PREFIX+"in deleteFile");
		File fileExists = new File(copyFileFrom);
		File deleteFile = new File(copyFileTo);
		if(fileExists.exists()){
			deleteFile.delete();
		}
	}

	public void copyFile(String copyFileFrom,String copyFileTo) throws IOException {
		logger.info(LOG_PREFIX+"in copyFile");
		Files.copy(Path.of(copyFileFrom), Path.of(copyFileTo));
	}

	public void replaceText(String filePath, String toBeReplaced, String replaceWith)
	{
		logger.info(LOG_PREFIX+"in replaceText");
		File fileToBeModified = new File(filePath);

		String oldContent = "";

		BufferedReader reader = null;

		FileWriter writer = null;

		try
		{
			reader = new BufferedReader(new FileReader(fileToBeModified));

			//Reading all the lines of input text file into oldContent

			String line = reader.readLine();

			while (line != null)
			{
				oldContent = oldContent + line + System.lineSeparator();

				line = reader.readLine();
			}

			//Replacing oldString with newString in the oldContent

			String newContent = oldContent.replaceAll(toBeReplaced, replaceWith);

			//Rewriting the input text file with newContent

			writer = new FileWriter(fileToBeModified);

			writer.write(newContent);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				//Closing the resources

				reader.close();

				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
