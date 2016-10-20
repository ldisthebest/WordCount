import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;




public class WordCount {
	private int OutInstruNum;
	private String[] keyWordOut;
	private ArrayList keyWord;
	private String fileWord;
	public WordCount(int instruNum){
		OutInstruNum = instruNum;
		fileWord = "";
		keyWord = new ArrayList<String>();
		keyWordOut = new String[OutInstruNum];
		for(int p = 0;p<OutInstruNum;p++){    //初始化数组
			keyWordOut[p] = "";
		}
		
	}
	
	public static void main(String[] args) throws IOException{
		WordCount wc = new WordCount(3);
		if(!wc.InputBaseInstruction()){
			return;
		}		
		wc.GetFile();
		String result = "";
		for(int num = 1;num < wc.keyWord.size()-1;num++){
			switch((String)wc.keyWord.get(num)){
			case "-c":result += "字符数量为："+wc.FindCharacterNum()+"\n";break;
			case "-w":result += "单词数量为："+wc.FindWordNum()+"\n";break;
			case "-l":result += "总行数为："+wc.FindRowlineNum()+"\n";break;
			}
		}
		wc.OutputNumToFile(result);
		
		//System.out.println(wc.fileWord);
		//System.out.println(wc.FindRowlineNum());
		
//		wc.InputOutInstruction();
//		wc.OutputNumToFile(2,"-w");
		
	}
	Boolean InputBaseInstruction() throws IOException{
		
		//System.out.print((char)f.read());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = br.readLine();
		char strC[] = str.toCharArray();
		for(int j =0;j<strC.length;j++){
			String keyWords = "";
			while(j<strC.length&&strC[j] != ' '){
				keyWords += ""+strC[j];			
				j++;
			}
			keyWord.add(keyWords);
		}
		if((keyWord.size() > 5 ||keyWord.size() < 3)||!((String)keyWord.get(0)).equals("wc.exe")){
			InstruError();
			return false;
		}
		for(int num = 1;num < keyWord.size()-1;num++){
			if(!IsSecondInstru((String)keyWord.get(num))){
				InstruError();
				return false;
			}
		}

		return true;
	}
	Boolean InputOutInstruction() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = br.readLine();
		char strC[] = str.toCharArray();
		int j = 0;
		for(int i = 0;i<OutInstruNum;i++){
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
		InputStream file = new FileInputStream((String)keyWord.get(keyWord.size()-1));
		int ch = file.read();
		while(ch != -1){
			fileWord += ""+(char)ch;
			ch = file.read();
		}
		file.close();
		
	}
	int FindCharacterNum(){
		char character[] = fileWord.toCharArray();
		int num = 0;
		for(int i = 0;i<character.length;i++){
			if(character[i] != '\r')
				num ++;
		}
		return num;
	}
	Boolean IsWordC(char c){
		if(c != ' ' && c != '\n' && c != ',' &&c != '\t'){
			return true;
		}
		return false;
	}
	int FindWordNum(){
		int num = 0;
		char character[] = fileWord.toCharArray();
		for(int i = 0;i<character.length;i++){
			if(IsWordC(character[i])){
				num++;	  
			}
			while(i<character.length && IsWordC(character[i])){
				i++;
			}
		}
		return num;
		
	}
	int FindRowlineNum(){
		char character[] = fileWord.toCharArray();
		int num = 0;
		for(int i = 0;i<character.length;i++){
			if(character[i] == '\n')
				num ++;
		}
		return num+1;
	}
	void OutputNumToFile(String tip) throws IOException{
		File f = new File(keyWordOut[2]);
		FileWriter file = new FileWriter(f);
		file.write(tip);
		file.close();
	}

}
