package com.strive.industry;

import java.util.List;

import lombok.Data;

@Data
public class IndustryRequest 
{
	private String fileName;
	
	private List<Industry> ind;

}
