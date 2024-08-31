package com.soprabanking.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.soprabanking.service.FlipbookService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.soprabanking.utility.Const.LOG_PREFIX;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

@Component
public class CustomFileVisitor extends SimpleFileVisitor<Path> {

    Logger logger = LoggerFactory.getLogger(CustomFileVisitor.class);

    Path source;
    Path target;
    
    public CustomFileVisitor() {
    	
    }

    public CustomFileVisitor(Path source, Path target) {
        logger.info(LOG_PREFIX+" in CustomFileVisitor");
        this.source = source;
        this.target = target;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException
    {      
        Path newDirectory= target.resolve(source.relativize(dir));
    	//Path newDirectory= target.resolve(source.getFileName());
        try{
            Files.copy(dir,newDirectory);
        	//Files.copy(dir,newDirectory,REPLACE_EXIS);
        }
        catch (FileAlreadyExistsException ioException){
            //log it and move
            return SKIP_SUBTREE; // skip processing
        }
        return FileVisitResult.CONTINUE;
    }
    public void copyDir(String strSource, String strDestination) {
    	Process process;
    	String[] buffer = new String[3];
    	try {
    		String os = System.getProperty("os.name");
    		if(os.equals("Linux"))
    	{
    		buffer[0] = "bash";
    		buffer[1] = "-c";
    		buffer[2] = "cp -R \"" + strSource + "/\" \"" + strDestination + "\"";
    	}
    	else
    	{
    		buffer[0] = "cmd";
    		buffer[1] = "/E/H/C/I";
    		buffer[2] = "Xcopy \"" + strSource + "/\" \"" + strDestination + "\"";
    	}
    		process = Runtime.getRuntime().exec(buffer);
    		BufferedReader br = new BufferedReader(
    				new InputStreamReader(process.getInputStream()));
    		process.waitFor();
    		process.destroy();
    		} 
    	catch (Exception e) {}
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

       Path newFile = target.resolve(source.relativize(file));

        try{
            Files.copy(file,newFile);
        }
        catch (IOException ioException){
            //log it and move
        }

        return FileVisitResult.CONTINUE;

    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {      
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        if (exc instanceof FileSystemLoopException) {
            //log error
        } else {
            //log error
        }
        return CONTINUE;
    }
}