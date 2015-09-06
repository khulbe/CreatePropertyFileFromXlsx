package com.propertyfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadFile {

	public static void main(String[] args) throws IOException {
		 
		       File curDir = new File(".");
		     
		        File[] filesList = curDir.listFiles();
		        for(File f : filesList){
		        
		            if(f.isFile()){
		            	
		            	if (f.getName().endsWith("xlsx")){
		            		System.out.println(f.getName());
		            	
		            		String filename=f.getName();
		            		Map<String, String> mapProperty = new HashMap<String, String>();
		            	   readXlsx(filename,mapProperty);
		            		List<PlaceHolderDto> placeHolderlst=new ArrayList<PlaceHolderDto>();
		            		readPlaceholder(filename,placeHolderlst);	
		            		replacePlaceholder(mapProperty,placeHolderlst);
		            	
		            	}
		                
		            }
		        }   
		    
		
		
		
		
		
		
	}

	
	private static void getAllFilse(File curDir) {

        File[] filesList = curDir.listFiles();
        for(File f : filesList){
          /*  if(f.isDirectory())
                getAllFilse(f);*/
            if(f.isFile()){
                System.out.println(f.getName());
            }
        }
	}

	private static void replacePlaceholder(Map<String, String> mapProperty, List<PlaceHolderDto> placeHolderlst) {
		List<String> fileName = placeHolderlst.get(0).getValue();
		for (int i =1; i<fileName.size(); i++){
			Map<String, String> mapPropertyNew= new HashMap<String, String>();
	//	mapPropertyNew = (Map<String, String>) deepClone(mapProperty);
			mapPropertyNew = new HashMap<String, String>(mapProperty); 	
			for(int j=1; j<placeHolderlst.size();j++){
				// loop map values
				
				for (String key : mapPropertyNew.keySet()) {
				String	value = mapPropertyNew.get(key);
				
				if (value.contains(placeHolderlst.get(j).key)){
					value = value.replace(placeHolderlst.get(j).key, placeHolderlst.get(j).value.get(i));
					mapPropertyNew.put(key, value);
				
				}
				
				
				}
				
			}
			fileWrite(mapPropertyNew,fileName.get(i));
			System.out.println(mapPropertyNew);
			
		}
		
	}
	private static void fileWrite(Map<String, String> mapPropertyNew, String filename) {
		try {

			String content="";


			for (String key : mapPropertyNew.keySet()) {
			String	value = mapPropertyNew.get(key);
			content = content + key + "=" +  mapPropertyNew.get(key) + "\n\n";
						
			}
			File file = new File(filename);

			file.createNewFile();
			

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}




	/* *//**
	   * This method makes a "deep clone" of any object it is given.
	   *//*
	  public static Object deepClone(Object object) {
	    try {
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      ObjectOutputStream oos = new ObjectOutputStream(baos);
	      oos.writeObject(object);
	      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	      ObjectInputStream ois = new ObjectInputStream(bais);
	      return ois.readObject();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	  }
	*/



	private static void readPlaceholder(String filename, List<PlaceHolderDto> lst) throws IOException {

		File myFile = new File(filename);
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
       
        // Return first sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(1);
       
          
		for (int i=0;i<mySheet.getPhysicalNumberOfRows();i++){
			PlaceHolderDto dto=new PlaceHolderDto(); 
			for(int j=0;j<mySheet.getRow(i).getPhysicalNumberOfCells();j++){
				dto.setKey(mySheet.getRow(i).getCell(0).toString());
				if (mySheet.getRow(i).getPhysicalNumberOfCells()>0){
					dto.value.add( mySheet.getRow(i).getCell(j).toString());
				}
			}
			lst.add(dto);
		}
	}




	private static void readXlsx(String filename, Map map) throws FileNotFoundException, IOException {
		File myFile = new File(filename);
        FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
       
        // Return first sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
       
        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();
       
        // Traversing over each row of XLSX file
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
          
			String [] keyValue = new String[2];
			keyValue[0]="";
			keyValue[1]="";
            while (cellIterator.hasNext()) {
            	String cellvalue="";
                Cell cell = cellIterator.next();
                
                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                	cellvalue =cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                	cellvalue=""+cell.getNumericCellValue();
                    break;
              
                default :
             
                }
                
                if (keyValue[0].equalsIgnoreCase("")){
                	keyValue[0]=cellvalue;
                }else{
                	keyValue[1]=cellvalue;
                }
            }
            map.put(keyValue[0], keyValue[1]);
           
        }
     
	}

	
	
	
	
}
