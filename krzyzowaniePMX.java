package komiwojazer;

import java.util.Random;

public class krzyzowaniePMX {
	private static boolean wystepuje(int x, int[] y) {
		for(int i=0; i<y.length; i++) {
			if(y[i] == x)
				return true;
		}
		return false;
	}
	private static int zwrocIndex(int x, int[] y) {
		for(int i=0; i<y.length; i++) {
			if(y[i] == x)
				return i;
		}
		return -1;
	}
	private static void funkcjaA0(int[] r1, int[] r2, int x, int pkt1, int[] p1) {
		int pom = zwrocIndex(x, r2);
		int pom2 = r1[pom];
		if(zwrocIndex(pom2, r2) < pkt1) {
			funkcjaA0(r1, r2, pom2, pkt1, p1);
		}
		else {
			p1[zwrocIndex(pom2, r2)] = x;
		}
	}
	private static void funkcjaA1(int[] r1, int[] r2, int x, int pkt1, int pkt2, int[] p1) {
		int pom = zwrocIndex(x, r2);
		int pom2 = r1[pom];
		if(pkt1 <= zwrocIndex(pom2, r2) && zwrocIndex(pom2, r2) < pkt2) {
			funkcjaA1(r1, r2, pom2, pkt1, pkt2, p1);
		}
		else {
			p1[zwrocIndex(pom2, r2)] = x;
		}
	}
	private static void funkcjaA2(int[] r1, int[] r2, int x, int pkt2, int[] p1) {
		int pom = zwrocIndex(x, r2);
		int pom2 = r1[pom];
		if(pkt2 <= zwrocIndex(pom2, r2) && zwrocIndex(pom2, r2) <= 8) {
			funkcjaA2(r1, r2, pom2, pkt2, p1);
		}
		else {
			p1[zwrocIndex(pom2, r2)] = x;
		}
	}
	
	public static void pmx(int[] rodzic1, int[] rodzic2, int[] potomek1, int[] potomek2) {
		Random rng = new Random();
		
		int pom;
		int pkt1 = rng.nextInt(19)+1;
		int pkt2 = rng.nextInt(19)+1;
		while(pkt1 == pkt2){
			 pkt1 = rng.nextInt(8)+1;
			 pkt2 = rng.nextInt(8)+1;
		}
		if(pkt1 > pkt2){
			pom = pkt1;
			pkt1 = pkt2;
			pkt2 = pom;
		}
		
		int[] s1r1 = new int[pkt1]; //segment z lewej
		int[] s3r1 = new int[rodzic1.length-pkt2]; ///segment z prawej
		int[] s2r1 = new int[rodzic1.length-s1r1.length-s3r1.length]; //segment środkowy
		
		int[] s1r2 = new int[pkt1]; //segment z lewej
		int[] s3r2 = new int[rodzic2.length-pkt2]; ///segment z prawej
		int[] s2r2 = new int[rodzic2.length-s1r2.length-s3r2.length]; //segment środkowy
		
		for(int i=0; i < s1r1.length; i++) {
				s1r1[i] = rodzic1[i];
		}
		pom = s1r1.length;
		for(int i=0; i < s2r1.length; i++) {
			s2r1[i] = rodzic1[pom];
			pom++;
		}
		pom = s2r1.length+s1r1.length;
		for(int i=0; i < s3r1.length; i++) {
			s3r1[i] = rodzic1[pom];
			pom++;
		}
		
		for(int i=0; i < s1r2.length; i++) {
			s1r2[i] = rodzic2[i];
		}
		pom = s1r2.length;
		for(int i=0; i < s2r2.length; i++) {
			s2r2[i] = rodzic2[pom];
			pom++;
		}
		pom = s2r2.length+s1r2.length;
		for(int i=0; i < s3r2.length; i++) {
			s3r2[i] = rodzic2[pom];
			pom++;
		}
		
		int seg = rng.nextInt(3);
		
		//dziecko 1
		switch(seg) {
			case 0:
				for(int i=0; i<s1r1.length; i++)
					potomek1[i] = s1r1[i];
				for(int i=0; i<s1r2.length; i++){
					if(!wystepuje(s1r2[i], s1r1)) {
						funkcjaA0(rodzic1, rodzic2, s1r2[i], pkt1, potomek1);
					}
				}
				for(int i=0; i<potomek1.length; i++) {
					if(potomek1[i] == 0)
						potomek1[i] = rodzic2[i];
				}
				break;
			case 1:
				pom = pkt1;
				for(int i=0; i<s2r1.length; i++) {
					potomek1[pom] = s2r1[i];
					pom++;
				}
				for(int i=0; i<s2r2.length; i++){
					if(!wystepuje(s2r2[i], s2r1)) {
						funkcjaA1(rodzic1, rodzic2, s2r2[i], pkt1, pkt2, potomek1);
					}
				}
				for(int i=0; i<potomek1.length; i++) {
					if(potomek1[i] == 0)
						potomek1[i] = rodzic2[i];
				}
				break;
			case 2:
				pom = pkt2;
				for(int i=0; i<s3r1.length; i++) {
					potomek1[pom] = s3r1[i];
					pom++;
				}
				for(int i=0; i<s3r2.length; i++){
					if(!wystepuje(s3r2[i], s3r1)) {
						funkcjaA2(rodzic1, rodzic2, s3r2[i], pkt2, potomek1);
					}
				}
				for(int i=0; i<potomek1.length; i++) {
					if(potomek1[i] == 0)
						potomek1[i] = rodzic2[i];
				}
				break;
		}

		//dziecko 2
		switch(seg) {
			case 0:
				for(int i=0; i<s1r2.length; i++)
					potomek2[i] = s1r2[i];
				for(int i=0; i<s1r1.length; i++){
					if(!wystepuje(s1r1[i], s1r2)) {
						funkcjaA0(rodzic2, rodzic1, s1r1[i], pkt1, potomek2);
					}
				}
				for(int i=0; i<potomek2.length; i++) {
					if(potomek2[i] == 0)
						potomek2[i] = rodzic1[i];
				}
				break;
			case 1:
				pom = pkt1;
				for(int i=0; i<s2r2.length; i++) {
					potomek2[pom] = s2r2[i];
					pom++;
				}
				for(int i=0; i<s2r1.length; i++){
					if(!wystepuje(s2r1[i], s2r2)) {
						funkcjaA1(rodzic2, rodzic1, s2r1[i], pkt1, pkt2, potomek2);
					}
				}
				for(int i=0; i<potomek2.length; i++) {
					if(potomek2[i] == 0)
						potomek2[i] = rodzic1[i];
				}
				break;
			case 2:
				pom = pkt2;
				for(int i=0; i<s3r2.length; i++) {
					potomek2[pom] = s3r2[i];
					pom++;
				}
				for(int i=0; i<s3r1.length; i++){
					if(!wystepuje(s3r1[i], s3r2)) {
						funkcjaA2(rodzic2, rodzic1, s3r1[i], pkt2, potomek2);
					}
				}
				for(int i=0; i<potomek2.length; i++) {
					if(potomek2[i] == 0)
						potomek2[i] = rodzic1[i];
				}
				break;
		}
	}
}
