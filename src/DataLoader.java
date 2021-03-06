

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxBox;
import com.cycling74.max.MaxObject;
import com.cycling74.max.MaxPatcher;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLCell;

public class DataLoader extends MaxObject {
	MaxBox myPoly;
	MaxBox polyLoopOnOffSend;
	MaxBox polyFilePathSend;
	MaxBox sendViewManager;
	
	
	
	
	public DataLoader (){
		declareOutlets(new int[]{ DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL ,DataTypes.ALL});
		MaxPatcher p = this.getParentPatcher();
		
//		MaxBox audioPbatcher = p.getNamedBox("audioBpatcherViews");
//		MaxPatcher audioPatcher  = audioPbatcher.getSubPatcher();
//		polyFilePathSend =  audioPatcher.getNamedBox("polyFilePathSend");
//		polyLoopOnOffSend  = audioPatcher.getNamedBox("polyLoopOnOffSend");
//		myPoly =  audioPatcher.getNamedBox("myPoly");
		
		
	}
	
	public void loadFolderData2(String dirName){
		System.out.println("loadFolderData2() was called");
		
		
		resetViews();
		resetSonoArea();
		resetBasket();
		
		Collection <File> filePathColl = getWAVCollection(dirName);
		
		
		sendFilePathInfoToPoly(filePathColl);
		System.out.println("Loading WAVs to poly has finished");
		
		setPolyLookUp(filePathColl);
		System.out.println("The polyAdressLookUp List was set");
		
		
		Collection <File> viewDataColl =  getViewDataCollection(dirName);
		
		for(File file : viewDataColl){
//			try {
//				post("ViewData: "+file.getCanonicalPath());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
			
			
			Atom [] atomArray = null;
			String filePath = null;
			String viewName = null;
			
			try {
				filePath = file.getCanonicalPath();
				viewName = file.getName().replace(".mat", "");
				viewName = viewName.replace("view", "");
				post("This is the filePath: "+ filePath);
				post("This is the viewName: "+ viewName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			atomArray = readViewDataFromMatFile(filePath, viewName);
			
			
			outlet(0,"setViewData", atomArray);
			
			
			
			
		}
		
		
		
		
		// load featureData in ViewsManagement
		outlet(0,"loadFolderFeatureData", Atom.newAtom(dirName));
		
		

		
//		select the fist view
		Atom [] atomArrayViewID = new Atom[1];
		atomArrayViewID[0] = Atom.newAtom(0);
		outlet(0,"setSelectedView", atomArrayViewID);
//		send view data to sono area
		outlet(0,"sendSelectedViewDataToSonoArea", atomArrayViewID);
		

		
		
		

		
		
	}
	


//	public void loadFolderData(String dirName){
//		resetViews();
//		resetSonoArea();
//		resetBasket();
//		
//		Collection <File> filePathColl = getWAVCollection(dirName);
//		
//		
//		sendFilePathInfoToPoly(filePathColl);
//		System.out.println("Loading WAVs to poly has finished");
//		
//		setPolyLookUp(filePathColl);
//		System.out.println("The polyAdressLookUp List was set");
//		
////		send View data to ViewsManagement
//		String viewName =  null;
//		Collection <File> txtPathColl =  getTxtCollection(dirName);
//		for(File file : txtPathColl){
//			String filePath = null;
//			
//			Atom [] atomArray = null;
//			try {
//				filePath = file.getCanonicalPath();
//				viewName = file.getName().replace(".txt", "");
////				post("This is the view name: "+ viewName);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				atomArray = readFromTxtFile(filePath, viewName);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			outlet(0,"setViewData", atomArray);
//		}
//		
//		
//		
//		
//		// load featureData in ViewsManagement
//		outlet(0,"loadFolderFeatureData", Atom.newAtom(dirName));
//		
//		
//
//		
////		select the fist view
//		Atom [] atomArrayViewID = new Atom[1];
//		atomArrayViewID[0] = Atom.newAtom(0);
//		outlet(0,"setSelectedView", atomArrayViewID);
////		send view data to sono area
//		outlet(0,"sendSelectedViewDataToSonoArea", atomArrayViewID);
//		
//	}
	
	
	
	
	
	
//	public Atom [] readFromMatFile(String filePath, String viewName){
//		MatFileReader mfr = null;
//		try {
//			mfr = new MatFileReader(filePath);
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	
//		Map<String, MLArray> content = mfr.getContent();
////		System.out.println(content);
//		MLCell featureData = (MLCell) content.get("featureData");
//		int numRows = featureData.getDimensions()[0];
//		int numColumns = featureData.getDimensions()[1];
//		int numFeatures = numColumns -1;
//		
//		
//		
//		
//		Atom [] returnAtomArray = new Atom[(numRows*numColumns)+2];
//		returnAtomArray[0]= Atom.newAtom(viewName);
//		returnAtomArray[1]= Atom.newAtom(numFeatures);
//		
//		int atomLoopCounter = 2;
//		for(int i = 0; i<numRows;i++){
//			returnAtomArray[atomLoopCounter]=Atom.newAtom(featureData.get(i, 0).contentToString());//filepath
//			for(int k = 1; k< numColumns;k++){
//				Double.parseDouble(featureData.get(i, k).contentToString());
//			}
//			atomLoopCounter = atomLoopCounter+numColumns;
//		}
//		
//		
//		System.out.println(featureData.get(0, 0).toString());
//		System.out.println(featureData.get(0, 0).contentToString());
//		System.out.println(featureData.get(1, 0).contentToString());
//		System.out.println(featureData.get(0, 1).contentToString());
//		
//		
//		
//		
//		
//		return null;
//		
//	}
	
	

	
	public Atom [] readFromTxtFile(String filePathTxt, String viewName) throws IOException{
		
		BufferedReader in = new BufferedReader(new FileReader(filePathTxt));
		
		 int numSamples = 0;
		 String line;
		 ArrayList<String> splittStringList = new ArrayList<String>();
		
		while((line = in.readLine()) != null)
		{
			String [] stringArray = line.split("\\$");
			//post("size of stringArray: "+stringArray.length);
			if(stringArray.length == 4){
				
				String x = stringArray[0];
				String y = stringArray[1];
				String filePath = stringArray[2];
				String fileName = stringArray[3];
			
//				if(!x.startsWith("-")){
//					String plus = "+";
//					x = plus.concat(x);
//				}
//				
//				if(!y.startsWith("-")){
//					String plus = "+";
//					y = plus.concat(y);
//				}
				
//				post("filePath: " +filePath);
//				post("filename: " +fileName);
//				post("x: "+x);
//				post("y: "+y);
				
				splittStringList.add(filePath);
				splittStringList.add(fileName);
				splittStringList.add(x);
				splittStringList.add(y);
				numSamples++;
				//post(line);
			}
		}
		in.close();
		
		 Atom [] viewAtomArray =  new Atom [4*numSamples+2];
		 viewAtomArray[0]=Atom.newAtom("setSampleData");
		 viewAtomArray[1]=Atom.newAtom(viewName);
		 
		 post("numSamples: "+ numSamples);
		 post("size stringSplittList: "+ splittStringList.size());
	
		
		 for(int i = 0; i< splittStringList.size(); i=i+4){
			 viewAtomArray[i+2] = Atom.newAtom(splittStringList.get(i));
			 viewAtomArray[i+3] = Atom.newAtom(splittStringList.get(i+1));
			 viewAtomArray[i+4] = Atom.newAtom(Double.valueOf(splittStringList.get(i+2)));
			 viewAtomArray[i+5] = Atom.newAtom(Double.valueOf(splittStringList.get(i+3)));
			 
			 
		 }
		return viewAtomArray;
		 
		 
		 

		
		
		
	}
	
	protected Atom [] readViewDataFromMatFile(String filePath, String viewName){
		
		
		Map<String, MLArray> content = null; 
		MatFileReader mfr = null;
		try {
			mfr = new MatFileReader(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		content = mfr.getContent();
//		System.out.println(content);
		MLCell featureDataMLCell = (MLCell) content.get("viewData");
		int numSamples = featureDataMLCell.getDimensions()[0];
//		int numColumns = featureDataMLCell.getDimensions()[1];
	
//		post("numSamples: "+numSamples);
//		post("numColumns: "+numColumns);
		

		 Atom [] viewAtomArray =  new Atom [4*numSamples+2];
		 viewAtomArray[0]=Atom.newAtom("setSampleData");
		 viewAtomArray[1]=Atom.newAtom(viewName);
		 
		 int atomArrayCounter = 2;
		
		for(int i = 0; i<numSamples;i++){
			
//			String sampleFilePath = featureDataMLCell.get(i, 2).contentToString();
//			String sampleFileName = featureDataMLCell.get(i, 3).contentToString();
			String sampleFilePath = featureDataMLCell.get(i, 2).contentToString().substring(7).replaceAll("'", "").trim();//filepath
			String sampleFileName = featureDataMLCell.get(i, 3).contentToString().substring(7).replaceAll("'", "").trim();//fileName
			double x = new Double (featureDataMLCell.get(i, 0).contentToString());
			double y = new Double (featureDataMLCell.get(i, 1).contentToString());
			
//			System.out.println("sampleFilePath: "+sampleFilePath);
//			System.out.println("sampleFileName: "+sampleFileName);
//			System.out.println("sample x: "+x);
//			System.out.println("sample y: "+y);
			
			viewAtomArray[atomArrayCounter] = Atom.newAtom(sampleFilePath);
			viewAtomArray[atomArrayCounter+1] = Atom.newAtom(sampleFileName);
			viewAtomArray[atomArrayCounter+2] = Atom.newAtom(x);
			viewAtomArray[atomArrayCounter+3] = Atom.newAtom(y);
			
			atomArrayCounter = atomArrayCounter +4;
		}
		
		
		

		 return viewAtomArray;
		
	}


	private void resetViews(){
		System.out.println("resetViews() method  was called");
		outlet(0,"resetAllViews" );

}
	
	private void resetSonoArea(){
		System.out.println("resetSonoArea() method  was called");
		outlet(1,"clearSonoArea" );
	}
	
	private void resetBasket(){
		outlet(5,"removeAllSamples");
	}
	
//	protected Collection <File> getMATCollection(String dirName){
//		File dir = new File(dirName);
//		String[] extensions = new String[] {"mat" };
//		Collection<File> files =   FileUtils.listFiles(dir, extensions, true);
//		System.out.println("There are "+files.size()+" mat files in this folder");
//		return files;
//		
//	}
	
	protected Collection<File> getWAVCollection(String dirName) {
		File dir = new File(dirName);
		String[] extensions = new String[] {"wav" , "WAV" };
		Collection<File> files =   FileUtils.listFiles(dir, extensions, true);
		System.out.println("There are "+files.size()+" samples in this folder");
		return files;
	}
	
	protected Collection <File> getTxtCollection(String dirName){
		File dir = new File(dirName);
		String[] extensions = new String[] { "txt" };
		Collection<File> files =   FileUtils.listFiles(dir, extensions, true);
		System.out.println("There are "+files.size()+" txt files in this folder");
		return files;
	}
	
	protected Collection <File> getViewDataCollection(String dirName) {
		File dir = new File(dirName);
		String[] extensions = new String[] { "mat" };
		Collection<File> files =   FileUtils.listFiles(dir, extensions, true);
//		System.out.println("There are "+files.size()+" mat files in this folder");
		Collection <File> returnColl =  new ArrayList <File>();
		
//		get the mat files starting with "view": these are the viewData files
		for(File file : files){
			String filePath = null;
			String fileName = null;
			
			try {
				filePath = file.getCanonicalPath();
				fileName = file.getName().replace(".mat", "");
//				post("This is the filePath: "+ filePath);
//				post("This is the fileName: "+ fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(fileName.startsWith("view")){
				returnColl.add(file);
			}
			
			
		}
		
		
		
		return returnColl;
		
		
	}
	
	private void  sendFilePathInfoToPoly(Collection <File> filePathColl) {
	    post("sendFilePathInfoToPoly..");
	 
	  
		
//		myPoly.send("voices", new Atom []{Atom.newAtom(filePathColl.size())});
		
		
		outlet(4, "voices", new Atom []{Atom.newAtom(filePathColl.size()) });
		
		int loopCounter = 1;
		for(File file : filePathColl){
			
			Atom[] polyNumber = new Atom [] {Atom.newAtom(loopCounter)};
			String filePath = null;
			try {
				filePath = file.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			filePath = filePath.replace("\\", "/");
			//post("filePath: "+filePath);
			
//			sets the filepath for the poly in the audioPatcher
			outlet(2, "target", polyNumber);
			outlet(2, filePath);
//			sets loop to ON(=1) for the poly in the audioPatcher
			outlet(3,"target", polyNumber);
			outlet(3, 1);
			
//			polyFilePathSend.send("target", polyNumber);
//			polyFilePathSend.send(filePath, null);
//			
//			polyLoopOnOffSend.send("target", polyNumber);
//			polyLoopOnOffSend.send(1);
			
			
		
			
			
			
			loopCounter++;
		}
		
	
	}
	
 	private void setPolyLookUp(Collection <File> filePathColl){
	
		
		Atom [] polyNoFilePathArray = new Atom [filePathColl.size()*2];
		int polyNumCounter = 1;
		int arrayCounter = 0;
		for(File file : filePathColl){
			Atom polyNumAtom = Atom.newAtom(polyNumCounter);
			String filePath = null;
			try {
				filePath = file.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			post("setPolyLookUp :"+filePath);
			
			Atom filePathAtom =  Atom.newAtom(filePath);
			
			polyNoFilePathArray[arrayCounter]= polyNumAtom;
			polyNoFilePathArray[arrayCounter+1]= filePathAtom;
			
			polyNumCounter++;
			arrayCounter = arrayCounter+2;
		}
		
//		sendViewManager.send("setPolyLookUp", polyNoFilePathArray);
		outlet(0,"setPolyLookUp", polyNoFilePathArray );
	}



}
