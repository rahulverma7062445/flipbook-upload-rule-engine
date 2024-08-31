package com.soprabanking.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.soprabanking.service.FlipbookService;

import static com.soprabanking.utility.Const.LOG_PREFIX;

@RestController
@CrossOrigin
public class FlipbookController {

	Logger logger = LoggerFactory.getLogger(FlipbookController.class);
	
	@Autowired
	private FlipbookService flipbookService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFlipbookData(@RequestParam(value = "files") MultipartFile[] files) throws IOException {
		logger.info(LOG_PREFIX+"in uploadFlipbookData");
		logger.info(LOG_PREFIX+"out uploadFlipbookData");
		return flipbookService.processUploadedFiles(files);
	}

}
