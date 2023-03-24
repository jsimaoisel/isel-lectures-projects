package pg.graphicenviroment;

import java.util.Random;

public class RandomNumbers {
	public static Random random = new Random();
	public static int generate(int min, int max) {
		return random.nextInt(max-min)+min;
	}
}
