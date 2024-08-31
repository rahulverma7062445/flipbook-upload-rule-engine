package com.soprabanking.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FlipbookService {
	
	public ResponseEntity<String> processUploadedFiles(MultipartFile[] files) throws IOException;

}
