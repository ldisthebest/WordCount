import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class WordCount {
	private int instructionType;
	private String[] keyWord;
	private String fileWord;
	public WordCount(int type){
		instructionType = type;
		keyWord = new String[instructionType];
		for(int p = 0;p<instructionType;p++){    //初始化数组
			keyWord[p] = "";
		}
		fileWord = "";
	}
	
	public static void main(String[] args) throws IOException{
		WordCount wc = new WordCount(3);
		if(!wc.InputInstruction()){
			return;
		}
		
		switch(wc.keyWord[2]){
		
		}
		
		
	}
	Boolean InputInstruction() throws IOException{
		
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
			//System.out.print(wc.keyWord[i]+" ");
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
		if(keyWord[0] != "WordCount.exe"||!IsSecondInstru(keyWord[0])){
			InstruError();
			return false;
		}
		
		return true;
	}
	Boolean IsSecondInstru(String s){
		if(s == "-c"||s == "-w"||s == "-l"||s == "-o")
			return true;
		return false;
	}
	void InstruError(){
		System.out.println("指令格式不正确");
	}
	void GetFile() throws IOException{
		InputStream file = new FileInputStream(keyWord[3]);
		char ch = (char)file.read();
		
	}
	void FindCharacterNum(){
		
	}

}
