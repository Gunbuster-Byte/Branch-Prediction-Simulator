import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

//By Jason Rodriguez
//EEL 4768	

public class Sim {
	public static void main(String args[]) throws FileNotFoundException {
		//M
		int GPB = Integer.parseInt(args[0]);
		//N
		int RB = Integer.parseInt(args[1]);
		
		File Trace = new File(args[2]);
		Scanner Trace_file = new Scanner(Trace);	
		int entries = (int)Math.pow(2, GPB);
		
		int table[] = new int[entries];
		for(int i = 0; i < entries; i++) {
				table[i] = 2;
		}
		BigInteger GBH = new BigInteger("0");
		BigInteger div = new BigInteger("4");
		int N = 0;
		int accesses = 0;
	
		while(Trace_file.hasNextLine()) {
			String line = Trace_file.nextLine();
			String[] tokens = line.split(" ");
			String PC = tokens[0];
			char state = tokens[1].charAt(0);
			String convert1 = new BigInteger(PC, 16).toString(2);
			
			BigInteger current = new BigInteger(convert1, 2).divide(div);
			
			convert1 = convert1.substring(current.toString(2).length()-GPB, current.toString(2).length());
			
			current = new BigInteger(convert1, 2);
			BigInteger Result;
			if(RB > 0)
				Result = current.xor(GBH.shiftLeft(GPB-RB));
			else
				Result = current;
			int entry = Result.intValue();
			if(table[entry] >= 2) {
					if(state == 't') {
						if(table[entry] < 3)
						table[entry]++;
						if(RB > 0)
							GBH = GBH.shiftRight(1).setBit(RB-1);

					}
					else if(state == 'n') {
						table[entry]--;
						N++;
						if(RB > 0)
							GBH = GBH.shiftRight(1);
					}
			}	
			else {
				if(state == 't') {
					table[entry]++;
					N++;
					if(RB > 0)
						GBH = GBH.shiftRight(1).setBit(RB-1);
				}
				else if(state == 'n') {
					if(table[entry] > 0)
						table[entry]--;
					if(RB > 0)
						GBH = GBH.shiftRight(1);
				}
			}
			accesses++;
		}
		//Misprediction Rate
		double rate = (double)N/accesses*100;
		System.out.print(GPB + " " + RB + " ");
		System.out.printf("%.2f", rate);
	}
}
