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
	private ArrayList listFiles;
	//private ArrayList readFile;
	private String stopFile,outFile,listPath;
	private ArrayList fileWord;
	
	private Boolean isListStyle;
	public WordCount(){
		isListStyle = false;
		stopFile = "";
		outFile = "";
		listPath = "";
		stopWord = new ArrayList<String>();
		keyWord = new ArrayList<String>();
		instru = new ArrayList<String>();
        listFiles = new ArrayList<String>();
       // readFile = new ArrayList<String>();
        fileWord = new ArrayList<String>();
	}
	
	public static void main(String[] args) throws IOException{
		WordCount wc = new WordCount();
		if(!wc.InputBaseInstruction(args)){
			return;
		}
		if(!wc.listPath.equals("")){			
			wc.GetFileList(wc.listPath);
		}

		wc.GetFile();
		wc.OutputResultToFile();


	}
	Boolean InputBaseInstruction(String[] str) throws IOException{
		
		for(int strCount = 0;strCount<str.length;strCount++){
			keyWord.add(str[strCount]);
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
				if(IsSpecialInstru((String)keyWord.get(i))){
					isListStyle = true;
				}
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
				//String filePath = (String)keyWord.get(i);
				for(int index = 0;index<keyWord.get(i).toString().length();index++){
					if(keyWord.get(i).toString().charAt(index) == '*'){
						listPath = GetListPath((String)keyWord.get(i));
						break;
					}
				}	
				if(listPath.equals("")){
					listFiles.add((String)keyWord.get(i));
				}
			}
		}

		return true;
	}
	String GetListPath(String path){
		String str = "";
		for(int index = 0;index<path.length();index++){
			if(path.charAt(index) != '*'){
				str += ""+path.charAt(index);
			}
			else{
				break;
			}
		}
//		String s = "";
//		for(int i = 0;i<str.length()-1;i++){
//			s += ""+str.charAt(i);
//		}
		//str.replace(str.charAt(str.length() - 1),'L');
		return str;
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
		for(int i = 0;i<listFiles.size();i++){	
			String str = "";
			InputStream file = new FileInputStream((String)listFiles.get(i));
			int ch = file.read();
			while(ch != -1){
				str += ""+(char)ch;
				ch = file.read();
			}
			fileWord.add(str);
			file.close();
		}		
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
    String FindCharacterNum(){
    	String str = "";
    	for(int i = 0;i<fileWord.size();i++){
    		char character[] = fileWord.get(i).toString().toCharArray();
    		int num = 0;
    		for(int j = 0;j<character.length;j++){
    			if(character[j] != '\r')
    				num ++;
    		}
    		str += listFiles.get(i).toString()+",字符数:"+num+"\n";
    	}
		
		return str;
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
	String FindWordNum() throws IOException{
		String str = "";
		for(int count = 0;count<fileWord.size();count++){
			int num = 0;
			String word = "";
			char character[] = fileWord.get(count).toString().toCharArray();
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
			str += listFiles.get(count).toString()+",单词数:"+num+"\n";
		}

		return str;
		
	}
	String FindRowlineNum(){
		String str = "";
		for(int count = 0;count<fileWord.size();count++){
			char character[] = fileWord.get(count).toString().toCharArray();
			int num = 0;
			for(int i = 0;i<character.length;i++){
				if(character[i] == '\n')
					num ++;
			}
			str += listFiles.get(count).toString()+",总行数:"+num+"\n";
		}
		
		return str;
	}
	String FindlinesInfo(){
		String str = "";
		for(int count = 0;count<fileWord.size();count++){
			int codeLine = 0,emptyLine = 0,noteLine = 0;
			Boolean isNewLine = true,isNoteAfterCode = false;
			char chr[] = fileWord.get(count).toString().toCharArray();
			int i;
			for(i = 0;i<chr.length;i++){
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
			str += listFiles.get(count).toString()+",代码行/空行/注释行:"+codeLine+"/"+emptyLine+"/"+noteLine+"\n";
		}		
		return str;
	}
	void OutputResultToFile() throws IOException{
		String result = "";
		for(int num = 0;num < instru.size()-1;num++){
			switch((String)instru.get(num)){
			case "-c":result += FindCharacterNum();break;
			case "-w":result += FindWordNum();break;
			case "-l":result += FindRowlineNum();break;
			case "-a":result += FindlinesInfo();break;
			}
		}
		File f = new File(outFile);
		FileWriter file = new FileWriter(f);
		file.write(result);
		file.close();
		System.out.print("输出完毕，可查看指定文件");
	}
	void GetFileList(String path){
		if(!isListStyle && !listPath.equals("")){
			System.out.println("没有-s指令,错误！");
			return;
		}
		File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                //System.out.println("文件夹是空的!");
                return;
            } 
            else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        GetFileList(file2.getAbsolutePath());
                    } 
                    else {
                    	
                    	String tailName = file2.getAbsolutePath().substring(file2.getAbsolutePath().indexOf('.'), file2.getAbsolutePath().length());
                    	if(tailName.equals(/*listPath.substring(listPath.indexOf('.'), listPath.length())*/".c")){
                    		listFiles.add(file2.getAbsolutePath());
                    	}
                    }
                }
            }
        } 
        else {
            System.out.println("文件不存在!");
        }

	}

}
