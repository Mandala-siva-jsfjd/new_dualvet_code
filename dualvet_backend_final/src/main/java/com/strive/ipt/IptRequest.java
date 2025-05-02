package com.strive.ipt;

import java.util.List;
import lombok.Data;

@Data
public class IptRequest 
{
	private String fileName;
	private List<InPlantTraining> ipt;

}
