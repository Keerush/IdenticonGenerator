package identicon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Identicon {
	private static String generateMD5Hash(String input) throws NoSuchAlgorithmException {
		// Convert string to list of bytes.
		byte[] byteInput = input.getBytes();

		// Created MD5 hash.
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(byteInput);

		// Convert hash to string
		BigInteger bigInt = new BigInteger(1, digest);
		return (bigInt.toString(16).length() >= 32 ? bigInt.toString(16) : "0" + bigInt.toString(16)).toUpperCase();
	}

	private static Color getColour(String hash) {
		// Get colour from first six characters.
		return Color.decode("#" + hash.substring(0, 6));
	}

	private static boolean[][] getArray(String hash) {
		boolean[][] pixels = new boolean[5][5];

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				// Set true of hex value in hash is divisible by 2.
				pixels[i][j] = (int) hash.charAt((i * 5) + j + 6) % 2 == 0;
			}
		}

		return pixels;
	}

	public static BufferedImage generateIdenticon(String input, int size) {
		// Create empty image.
		BufferedImage bi = new BufferedImage(size * 2, size * 2, BufferedImage.TYPE_INT_RGB);
		byte WHITE = (byte) 255;

		try {
			String hash = generateMD5Hash(input);

			int COLOUR = getColour(hash).getRGB();
			boolean[][] pixels = getArray(hash);

			for (int x = 0; x < size; x++)
				for (int y = 0; y < size; y++) {
					int colour = pixels[x / (size / pixels.length)][y / (size / pixels.length)] ? COLOUR : WHITE;
					// first quadrant
					bi.setRGB(x, y, colour);

					// second quadrant
					bi.setRGB(bi.getWidth() - 1 - x, y, colour);

					// third quadrant
					bi.setRGB(bi.getWidth() - 1 - x, bi.getHeight() - 1 - y, colour);

					// fourth quadrant
					bi.setRGB(x, bi.getHeight() - 1 - y, colour);
				}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return bi;
	}
}
