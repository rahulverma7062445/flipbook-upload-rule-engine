package com.flipbook.pojo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FlipbookData {
	

	String imagePath;
	List<String> imageNameAl = new ArrayList<String>();
	int totalPageCount;
	String magazineName;
	String fliphtml;

	String pptxName;

	String bgImageName;

	String logoName;
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public List<String> getImageNameAl() {
		return imageNameAl;
	}
	public void setImageNameAl(List<String> imageNameAl) {
		this.imageNameAl = imageNameAl;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public String getMagazineName() {
		return magazineName;
	}
	public void setMagazineName(String magazineName) {
		this.magazineName = magazineName;
	}
	public String getFliphtml() {
		return fliphtml;
	}
	public void setFliphtml(String fliphtml) {
		this.fliphtml = fliphtml;
	}
	public String getBgImageName() {
		return bgImageName;
	}
	public void setBgImageName(String bgImageName) {
		this.bgImageName = bgImageName;
	}

	public String getLogoName() {
		return logoName;
	}
	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getPptxName() {
		return pptxName;
	}
	public void setPptxName(String pptxName) {
		this.pptxName = pptxName;
	}
}
