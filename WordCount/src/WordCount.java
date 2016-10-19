import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class WordCount {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = br.readLine();
		char strC[] = str.toCharArray();
		String[] keyWord = new String[3]; //声明字符串数组
		for(int p = 0;p<3;p++){    //初始化数组
			keyWord[p] = "";
		}
		int j = 0;
		for(int i = 0;i<3;i++){
			while(j<strC.length&&strC[j] != ' '){
				keyWord[i] += ""+strC[j];
				j++;
			}
			System.out.print(keyWord[i]+" ");
			j++;
		}
		
	}
	

}
