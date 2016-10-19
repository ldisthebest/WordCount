import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;




public class WordCount {
	private int instructionType;
	private String[] keyWord,keyWordOut;
	private String fileWord;
	public WordCount(int type){
		instructionType = type;
		keyWord = new String[instructionType];
		keyWordOut = new String[instructionType];
		for(int p = 0;p<instructionType;p++){    //初始化数组
			keyWord[p] = "";
			keyWordOut[p] = "";
		}
		fileWord = "";
	}
	
	public static void main(String[] args) throws IOException{
		WordCount wc = new WordCount(3);
//		if(!wc.InputBaseInstruction()){
//			return;
//		}
//		
//		wc.GetFile();
//		System.out.print(wc.fileWord);
		wc.InputOutInstruction();
		wc.OutputNumToFile(2,"-w");
		
	}
	Boolean InputBaseInstruction() throws IOException{
		
		//System.out.print((char)f.read());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = br.readLine();
		char strC[] = str.toCharArray();
		int j = 0;
		for(int i = 0;i<instructionType;i++){
			while(j<strC.length&&strC[j] != ' '){
				keyWord[i] += ""+strC[j];
				j++;
			}
			//System.out.print(keyWord[i]+" ");
			j++;
		}
		if(--j < strC.length){
			while(j<strC.length){
				if(strC[j] != ' '){
					InstruError();
					return false;
				}
				else{
					j++;
				}
			}
		}
		if(!keyWord[0].equals("wc.exe")||!IsSencondInstruOfOut(keyWord[1])){
			InstruError();
			return false;
		}
		return true;
	}
	Boolean InputOutInstruction() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = br.readLine();
		char strC[] = str.toCharArray();
		int j = 0;
		for(int i = 0;i<instructionType;i++){
			while(j<strC.length&&strC[j] != ' '){
				keyWordOut[i] += ""+strC[j];
				j++;
			}
			//System.out.print(keyWord[i]+" ");
			j++;
		}
		if(--j < strC.length){
			while(j<strC.length){
				if(strC[j] != ' '){
					InstruError();
					return false;
				}
				else{
					j++;
				}
			}
		}
		if(!keyWordOut[0].equals("wc.exe")||!IsSencondInstruOfOut(keyWordOut[1])){
			InstruError();
			return false;
		}
		return true;
	}
	Boolean IsSecondInstru(String s){
		if(s.equals("-c")||s.equals("-w")||s.equals("-l"))
			return true;
		return false;
	}
	Boolean IsSencondInstruOfOut(String s){
		if(s.equals("-o"))
			return true;
		return false;
	}
	void InstruError(){
		System.out.println("指令格式不正确");
	}
	void GetFile() throws IOException{
		InputStream file = new FileInputStream(keyWord[2]);
		int ch = file.read();
		while(ch != -1){
			fileWord += ""+(char)ch;
			ch = file.read();
		}
		file.close();
		
	}
	void FindCharacterNum(){
		
	}
	void OutputNumToFile(int num,String tip) throws IOException{
		File f = new File(keyWordOut[2]);
		FileWriter file = new FileWriter(f);
		file.write("aaa");
		file.close();
	}

}
