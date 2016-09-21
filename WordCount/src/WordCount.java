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
	private ArrayList stopWord;
	private String stopFile,outFile,readFile;
	private String fileWord;
	
	public WordCount(){
		fileWord = "";
		stopFile = "";
		outFile = "";
		readFile = "";
		stopWord = new ArrayList<String>();
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
		for(int i = 0;i<wc.stopWord.size();i++){
			System.out.println(wc.stopWord.get(i));
		}
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
	void GetStopListWords() throws IOException{
		InputStream file = new FileInputStream(stopFile);
		int ch = file.read();
		String word = "";
		while(ch != -1){
			if(((char)ch == ' ' || (char)ch == ',' ||(char)ch == '\r'||(char)ch == '\n')&&(!word.equals(""))){
				stopWord.add(word);
				word = ""; //清空缓存
				while((char)ch == ' ' || (char)ch == ','||(char)ch == '\r'||(char)ch == '\n'){
					ch = file.read();
				}
			}
			if(ch != -1)
			word += ""+(char)ch;
			ch = file.read();
		}
		if(!word.equals("")){
			stopWord.add(word);
			word = ""; //清空缓存
		}
		file.close();
		System.out.println(stopFile);
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
	Boolean IsWordInStopList(String word) throws IOException{
		for(int i = 0;i<stopWord.size();i++){
			if(stopWord.get(i).equals(word)){
				return true;
			}
		}
		return false;
	}
	int FindWordNum() throws IOException{
		int num = 0;
		String word = "";
		char character[] = fileWord.toCharArray();
		if(!stopFile.equals("")){
			GetStopListWords();
		}
		for(int i = 0;i<character.length;i++){
			if(IsWordC(character[i])){
				word += ""+character[i];
				num++;i++;
				while(i<character.length && IsWordC(character[i])){
					word += ""+character[i];
					i++;
				}
				if(IsWordInStopList(word)){
					num --;
				}
				word = "";
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
	String FindlinesInfo(){
		String str = "";
		int codeLine = 0,emptyLine = 0,noteLine = 0;
		Boolean isNewLine = true,isNoteAfterCode = false;
		char chr[] = fileWord.toCharArray();
		int i;
		for(i = 0;i<chr.length;i++){
			//System.out.print((int)chr[i]+" ");
			if(i+1<chr.length&&chr[i] == '/' && chr[i+1] == '/'){
				if(isNewLine)
				noteLine ++;
				while(i<chr.length&&chr[i] != '\n'){
					i++;
				}
				isNewLine = true;
			}
			else if(i+1<chr.length&&chr[i] == '/' && chr[i+1] == '*'){
				i += 2;
				while(i<chr.length&&(chr[i] != '*' || chr[i+1] != '/')){
					if(chr[i] == '\n'){
						if(isNoteAfterCode){
							isNoteAfterCode = false;
						}
						else
						noteLine ++;
						isNewLine = true;
					}
					i++;
				}
				i++;
				if(1+i<chr.length&&chr[i+1] != '\r'){
					isNewLine = false;
				}
				else{
					noteLine++;
					i += 2;
					isNewLine = true;
				}
					
			}
			else if(i<chr.length-1&&chr[i] != ' ' && chr[i+1] != ' ' && chr[i] != '\r' && chr[i+1] != '\r'){
				if(!isNoteAfterCode)
				codeLine++;
				while(i<chr.length&&chr[i] != '\n'){					
					if(i<chr.length-1&&chr[i] == '/'&&chr[i+1] == '*'){
						isNoteAfterCode = true;
						i--;
						break;
					}
					i++;
				}
				if(i<chr.length&&chr[i] == '\n')
				isNewLine = true;
			}
			else{
                if(i<chr.length-1&&chr[i] != '\r'&&chr[i+1] != '\r'){
					i += 2;
				}
				else if(i<chr.length&&chr[i] != '\r'){
					i++;
				}
				while(i<chr.length&&chr[i] == ' '){
					i++;
				}
				if(i<chr.length&&chr[i] == '\r'){
					if(!isNewLine){
						noteLine++;
					}
					else
						emptyLine++;
					isNewLine =true;
					i++;
				}
				else if( i == chr.length){
					if(!isNewLine){
						noteLine++;
					}
					else
						emptyLine++;
				}
			}
			
		}
		if(i == chr.length && chr[i - 1] == '\n'){
			emptyLine++;
		}
		str += "代码行/空行/注释行:"+codeLine+"/"+emptyLine+"/"+noteLine+"/";
		return str;
	}
	void OutputResultToFile() throws IOException{
		String result = "";
		for(int num = 0;num < instru.size()-1;num++){
			switch((String)instru.get(num)){
			case "-c":result += readFile+",字符数量为："+FindCharacterNum()+"\n";break;
			case "-w":result += readFile+",单词数量为："+FindWordNum()+"\n";break;
			case "-l":result += readFile+",总行数为："+FindRowlineNum()+"\n";break;
			case "-a":result += readFile+","+FindlinesInfo()+"\n";break;
			}
		}
		File f = new File(outFile);
		FileWriter file = new FileWriter(f);
		file.write(result);
		file.close();
		System.out.print("输出完毕，可查看指定文件");
	}

}
