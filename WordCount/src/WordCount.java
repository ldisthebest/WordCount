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
	private ArrayList keyWord;
	private ArrayList instru;
	private String stopFile,outFile,readFile;
	private String fileWord;
	public WordCount(){
		fileWord = "";
		stopFile = "";
		outFile = "";
		readFile = "";
		keyWord = new ArrayList<String>();
		instru = new ArrayList<String>();

	}
	
	public static void main(String[] args) throws IOException{
		WordCount wc = new WordCount();
		if(!wc.InputBaseInstruction()){
			return;
		}		
		wc.GetFile();
		wc.OutputResultToFile();
		
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
		if(!((String)keyWord.get(0)).equals("wc.exe")){
			InstruError();
			return false;
		}
		for(int i = 1;i<keyWord.size();i++){
			if(IsSpecialInstru((String)keyWord.get(i))&&i!=1){
				InstruError();
				return false;
			}			
			if(IsNormalInstru((String)keyWord.get(i))||IsSpecialInstru((String)keyWord.get(i))){
				instru.add((String)keyWord.get(i));
			}
			else if(IsInstruOfStop((String)keyWord.get(i))){
				instru.add((String)keyWord.get(i));
				if(++i>keyWord.size()-1){
					InstruError();
					return false;
				}
				stopFile = (String)keyWord.get(i);
			}
			else if(IsInstruOfOut((String)keyWord.get(i))){
				instru.add((String)keyWord.get(i));
				if(++i>keyWord.size()-1){
					InstruError();
					return false;
				}
				outFile = (String)keyWord.get(i);
			}
			else{
				readFile = (String)keyWord.get(i);
			}
		}

		return true;
	}
	Boolean IsNormalInstru(String s){
		if(s.equals("-c")||s.equals("-w")||s.equals("-l")||s.equals("-a"))
			return true;
		return false;
	}
	Boolean IsSpecialInstru(String s){
		if(s.equals("-s"))
			return true;
		return false;
	}
	Boolean IsInstruOfOut(String s){
		if(s.equals("-o"))
			return true;
		return false;
	}
	Boolean IsInstruOfStop(String s){
		if(s.equals("-e"))
			return true;
		return false;
	}
	void InstruError(){
		System.out.println("指令格式不正确");
	}
	void GetFile() throws IOException{
		InputStream file = new FileInputStream(readFile);
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
		if(c != ' ' && c != '\n' && c != ',' &&c != '\t'&&c != '\r'){
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
				while(i<character.length && IsWordC(character[i])){
					i++;
				}
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
	void OutputResultToFile() throws IOException{
		String result = "";
		for(int num = 0;num < instru.size()-1;num++){
			switch((String)instru.get(num)){
			case "-c":result += readFile+",字符数量为："+FindCharacterNum()+"\n";break;
			case "-w":result += readFile+",单词数量为："+FindWordNum()+"\n";break;
			case "-l":result += readFile+",总行数为："+FindRowlineNum()+"\n";break;
			}
		}
		File f = new File(outFile);
		FileWriter file = new FileWriter(f);
		file.write(result);
		file.close();
		System.out.print("输出完毕，可查看指定文件");
	}

}
