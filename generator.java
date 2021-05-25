package komiwojazer;

import java.util.Random;

public class generator {

	public static void main (String[] args) {
		Random rng = new Random();
		
		int[][] tab = new int [20][20];
		int pom = 0;
		
		for(int i=0; i<20; i++) {
			tab[i][i] = 0;
			for(int j=i+1; j<20; j++) {
				pom = rng.nextInt(1000);
				tab[i][j] = pom;
				tab[j][i] = pom;
			}
		}
		
		for(int i=0; i<20; i++) {
			System.out.print("{");
			for(int j=0; j<20; j++) {
				if(j<19) System.out.print(tab[i][j] + ", ");
				else System.out.print(tab[i][j]);
			}
			System.out.print("},");
			System.out.println();
		}
	}
}