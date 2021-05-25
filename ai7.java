package komiwojazer;

import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;

public class ai7{

    public static int[] generuj(int[] x, int miastoPocz) {
    	x[0] = miastoPocz;
        for(int i=0; i < x.length; i++)
        	x[i] = (i+1);
        int pom;
        for(int i=0; i<x.length; i++) {
        	if(x[i] == miastoPocz) {
        		pom = x[0];
        		x[0] = x[i];
        		x[i] = pom;
        	}
        }
        return x;
    }
    
    public static int[] pomieszaj(int[] x) {
		int x1, x2, pom;
		Random rng = new Random();
		for(int i=1;i<100;i++){
			x1 =  rng.nextInt(19)+1;
			x2 =  rng.nextInt(19)+1;
			pom = x[x1];
			x[x1] = x[x2];
			x[x2] = pom;
		}
		return x;
	}
    
    public static int[] mutacjaInwersja(int[] x) {
    	Random rng = new Random();
    	int p1 = rng.nextInt(x.length);
    	int p2 = rng.nextInt(x.length);
    	while(p1 >= p2 || p1 == 0) {
    		p1 = rng.nextInt(x.length);
    		p2 = rng.nextInt(x.length);
    	}
    	int srodek = p1 + ((p2 + 1) - p1) / 2;
    	int koniec = p2;
    	for(int i=p1; i < srodek; i++) {
    		int tmp = x[i];
			x[i] = x[koniec];
			x[koniec] = tmp;
			koniec--;
    	}
    	return x;
    }

    public static void main(String[] args) throws IOException {
        int populacja; // wielkość populacji
        int miastoPocz; // poczatkowe miasto
        int ilGeneracji; // ilosc generacji

        try {
            File plik = new File("dane_wejsciowe.txt");
            Scanner czytnik = new Scanner(plik);
            int[] daneWE = new int[3];
            int licz = 0;
            while (czytnik.hasNextLine()) {
              String linia = czytnik.nextLine();
              if(licz < daneWE.length)
            	  daneWE[licz++] = Integer.parseInt(linia);
            }
            czytnik.close();
            populacja = daneWE[0];
            miastoPocz = daneWE[1];
            ilGeneracji = daneWE[2];
            System.out.println("Używane są dane z pliku: \nWielkość populacji: "+populacja+"\nMiasto początkowe: "+miastoPocz+"\nIlość generacji: "+ilGeneracji);
          }
        catch (FileNotFoundException e) {
            populacja = 100;
            miastoPocz = 1;
            ilGeneracji = 1000;
            System.out.println("Brak pliku. Używane są dane domyślne: "+"\nWielkość populacji: "+populacja+"\nMiasto początkowe: "+miastoPocz+"\nIlość generacji: "+ilGeneracji);
          }
        
        int iloscMiast = 20; // ilosc miast do odwiedzenia
        Random ruletka = new Random();
        int trasa = 0, miasto;
        int trasaMin = Integer.MAX_VALUE;
        int[][] tab= new int[populacja][iloscMiast]; // P(1)
        int[][] tmp = new int[populacja][iloscMiast]; // przemieszana populacja
        int[] wstawionyGen = new int[populacja]; // test czy już był przemieszany
        int[] najlepszaTrasa = new int[iloscMiast];
        int[] pomodnt = new int[20];
        int[] odleglosciNT = new int[20];

		FileWriter pisacz2 = new FileWriter("dane_wyjsciowe2.txt");

        int[][] graf = {
				{0, 953, 168, 252, 390, 568, 361, 811, 231, 588, 411, 216, 143, 299, 840, 33, 96, 539, 602, 50},
				{953, 0, 809, 386, 314, 622, 890, 444, 137, 971, 311, 203, 653, 942, 591, 713, 667, 158, 576, 728},
				{168, 809, 0, 918, 613, 384, 539, 626, 952, 602, 174, 816, 391, 948, 951, 870, 528, 921, 880, 577},
				{252, 386, 918, 0, 542, 759, 852, 674, 375, 575, 791, 402, 644, 565, 491, 915, 977, 443, 916, 13},
				{390, 314, 613, 542, 0, 4, 742, 365, 386, 626, 980, 612, 727, 373, 329, 200, 847, 362, 125, 179},
				{568, 622, 384, 759, 4, 0, 629, 646, 3, 250, 821, 633, 880, 113, 504, 683, 742, 763, 211, 922},
				{361, 890, 539, 852, 742, 629, 0, 301, 248, 563, 582, 237, 999, 935, 123, 402, 530, 593, 190, 964},
				{811, 444, 626, 674, 365, 646, 301, 0, 649, 82, 179, 209, 655, 441, 203, 522, 449, 392, 195, 367},
				{231, 137, 952, 375, 386, 3, 248, 649, 0, 205, 68, 487, 7, 79, 919, 325, 969, 246, 555, 910},
				{588, 971, 602, 575, 626, 250, 563, 82, 205, 0, 448, 765, 68, 816, 494, 837, 403, 470, 184, 895},
				{411, 311, 174, 791, 980, 821, 582, 179, 68, 448, 0, 339, 857, 874, 960, 242, 927, 991, 558, 614},
				{216, 203, 816, 402, 612, 633, 237, 209, 487, 765, 339, 0, 722, 870, 719, 945, 588, 556, 240, 714},
				{143, 653, 391, 644, 727, 880, 999, 655, 7, 68, 857, 722, 0, 158, 67, 171, 385, 816, 844, 938},
				{299, 942, 948, 565, 373, 113, 935, 441, 79, 816, 874, 870, 158, 0, 543, 434, 206, 639, 376, 297},
				{840, 591, 951, 491, 329, 504, 123, 203, 919, 494, 960, 719, 67, 543, 0, 130, 526, 670, 70, 826},
				{33, 713, 870, 915, 200, 683, 402, 522, 325, 837, 242, 945, 171, 434, 130, 0, 722, 259, 568, 574},
				{96, 667, 528, 977, 847, 742, 530, 449, 969, 403, 927, 588, 385, 206, 526, 722, 0, 4, 242, 546},
				{539, 158, 921, 443, 362, 763, 593, 392, 246, 470, 991, 556, 816, 639, 670, 259, 4, 0, 827, 771},
				{602, 576, 880, 916, 125, 211, 190, 195, 555, 184, 558, 240, 844, 376, 70, 568, 242, 827, 0, 819},
				{50, 728, 577, 13, 179, 922, 964, 367, 910, 895, 614, 714, 938, 297, 826, 574, 546, 771, 819, 0}

        };
		long start = System.currentTimeMillis();
	        for(int p=0;p<ilGeneracji;p++) { // iteracja 1000 razy (#ev = 1000)
	            for (int i = 0; i < populacja; i++)
	                wstawionyGen[i] = 1; // zerowanie wstawionych genow
	
	            for (int i = 0; i < populacja; i++) {
	                generuj(tab[i], miastoPocz); // generacja populacji
	            }
	            for (int i = 0; i < populacja; i++) {
	                pomieszaj(tab[i]); // mieszanie populacji
	            }
	
	            int wybor = ruletka.nextInt(populacja); // losowanie
	            for (int i = 0; i < populacja; i++) { // mieszanie
	                while (wstawionyGen[wybor] == 0)
	                    wybor = ruletka.nextInt(populacja);
	
	                if (ruletka.nextFloat() <= 0.6){ // szansa na losowanie
	                	if (ruletka.nextFloat() <= 0.02) // mutacja
	                		tmp[i] = mutacjaInwersja(tab[wybor]);
	                	else
	                		tmp[i] = tab[wybor];
	                }
	                else{
	                	if (ruletka.nextFloat() <= 0.02)// mutacja
	                		tmp[i] = mutacjaInwersja(tab[i]);
	                	else
	                		tmp[i] = tab[i];
	                }
	                wstawionyGen[wybor] = 0;
	            }
	            
	            int miasto1 = 0;
	            int miasto2 = 0;
	            for(int i=0; i<populacja; i++) {
	            	trasa = 0;
		            for(int j=0; j<iloscMiast; j++) {
		            	miasto1 = (tab[i][j])-1;
		            	if((j+1) < iloscMiast)
		            		miasto2 = (tab[i][j+1])-1;
		            	else
		            		miasto2 = miastoPocz-1;
		            	trasa += graf[miasto1][miasto2];
		            	pomodnt[j] = graf[miasto1][miasto2];
		            }
		            if(trasa < trasaMin) {
		            	trasaMin = trasa;
		            	for(int ii=0; ii<najlepszaTrasa.length; ii++)
		            		najlepszaTrasa[ii] = tab[i][ii];
		            	for(int ii=0; ii<odleglosciNT.length; ii++)
		            		odleglosciNT[ii] = pomodnt[ii];
		            }
	            }
	            
	            miasto = 0;
	            for(int i=0; i<populacja; i++) {
	            	trasa = 0;
		            for(int j=0; j<iloscMiast; j++) {
		            	miasto1 = (tmp[i][j])-1;
		            	if((j+1) < iloscMiast)
		            		miasto2 = (tmp[i][j+1])-1;
		            	else
		            		miasto2 = miastoPocz-1;
		            	trasa += graf[miasto1][miasto2];
		            	pomodnt[j] = graf[miasto1][miasto2];
		            }
		            if(trasa < trasaMin) {
		            	trasaMin = trasa;
		            	najlepszaTrasa = tmp[i];
		            	for(int ii=0; ii<odleglosciNT.length; ii++)
		            		odleglosciNT[ii] = pomodnt[ii];
		            }
	            }
	            
	           System.out.println("Generacja nr "+(p+1)+" odleglosc = "+trasaMin);
				try {
					pisacz2.write(trasaMin+"\n");
				}
				catch (IOException e) {
					e.printStackTrace();
				}
	        }
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;

		pisacz2.close();

		System.out.println();
            
            System.out.println("Liczba odwiedzonych miast: "+najlepszaTrasa.length);
            System.out.print("Najlepsza trasa: ");
            for(int i=0; i<najlepszaTrasa.length; i++)
            	System.out.print(najlepszaTrasa[i]+"; ");
            System.out.println("\nDługość najlepszej trasy: "+trasaMin);
            System.out.print("Odleglosci: ");
            for(int i=0; i<odleglosciNT.length; i++)
            	System.out.print(odleglosciNT[i]+"; ");
            int narast = 0;
            System.out.print("\nOdległość narastająco: ");
            for(int i=0; i<odleglosciNT.length; i++) {
            	narast += odleglosciNT[i];
            	System.out.print(narast + "; ");
            }
            System.out.print("\nCzas wykonania: "+timeElapsed + " [ms]");
            
            try {
                FileWriter pisacz = new FileWriter("dane_wyjsciowe.txt");
                pisacz.write(najlepszaTrasa.length+"\n");
                for(int i=0; i<najlepszaTrasa.length; i++)
				pisacz.write(najlepszaTrasa[i]+"	");
                pisacz.write(trasaMin+"\n");
                for(int i=0; i<odleglosciNT.length; i++)
						pisacz.write(odleglosciNT[i]+"	");
                narast = 0;
                pisacz.write("\n");
                for(int i=0; i<odleglosciNT.length; i++) {
                	narast += odleglosciNT[i];
                	pisacz.write(narast + "	");
                }
				pisacz.write("\n"+timeElapsed+"\n");
                pisacz.close();
              } 
            catch (IOException e) {
                e.printStackTrace();
              }
    }
}
