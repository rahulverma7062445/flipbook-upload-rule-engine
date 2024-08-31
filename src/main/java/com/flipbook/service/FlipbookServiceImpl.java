package com.flipbook.service;


import java.io.IOException;

import com.flipbook.processor.FlipbookProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.flipbook.pojo.FlipbookData;

import static com.flipbook.utility.Const.LOG_PREFIX;

@Service
public class FlipbookServiceImpl implements FlipbookService {
	
	Logger logger = LoggerFactory.getLogger(FlipbookService.class);
	
	@Autowired
	private FlipbookProcessor flipbookProcessor;

	@Override
	public ResponseEntity<String> processUploadedFiles(MultipartFile[] files) throws IOException {
		logger.info(LOG_PREFIX+"in processUploadedFiles");
		FlipbookData flipbookData = flipbookProcessor.processUploadedFiles(files);
		logger.debug(LOG_PREFIX+"in processUploadedFiles totalPageCount ["+flipbookData.getTotalPageCount()+"]");
		if(flipbookData.getTotalPageCount()>0) {
			return flipbookProcessor.configurePages(flipbookData);
			
		}
		else {
			logger.error("No images found");
		}
		logger.info(LOG_PREFIX+"out processUploadedFiles");
        return null;
    }

}
