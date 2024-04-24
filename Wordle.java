import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;


public class Wordle {
	
	public static final HashMap<String, String> invalid_wordlength = new HashMap<String, String>();
	public static final HashMap<String, String> invalid_chars = new HashMap<String, String>();
	public static final HashMap<String, String> lose_message = new HashMap<String, String>();
	public static final HashMap<String, String> win5tries_message = new HashMap<String, String>();
	public static final HashMap<String, String> win4tries_message = new HashMap<String, String>();
	public static final HashMap<String, String> win3tries_message = new HashMap<String, String>();
	public static final HashMap<String, String> win2tries_message = new HashMap<String, String>();
	public static final HashMap<String, String> win1tries_message = new HashMap<String, String>();
	public static final HashMap<String, String> win0tries_message = new HashMap<String, String>();
	public static final HashMap<String, String> playagain = new HashMap<String, String>();
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static String[] colorPicker = {"", "\u001B[43m", "\u001B[42m"};
	public static String[] resetPicker = {"", "\u001B[0m", "\u001B[0m"};
	
	public static String WordPicker(String language) {
		String filename = ""; 
		if(language.equals("en")) {
			filename = "wordle.txt";
		} else if(language.equals("de")) {
			filename = "wordle_de.txt";
		}
		String[] wordlist;
		String randomWord = "";
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String str = "", l = "";
			while ((l = br.readLine()) != null) {
				str += " " + l;
			}
			br.close();
			wordlist = str.split("\",\"");
			int i = new Random().nextInt(wordlist.length);
			randomWord = wordlist[i];
		} catch (IOException e) {
			e.printStackTrace();
		}
		return randomWord;
	}
	
	public static int WordChecker(String word) {
		if(word.length() != 5) {
			return -1;  // illegal word length
		}
		for (char c : word.toCharArray()) {
			if(!Character.isLetter(c)) {
				return -2;  // illegal characters
			}
		}
		return 0;
	}
	
	public static void WordleGame() {
		String language = "";
		while (!language.equals("de") && !language.equals("en")) {
			Scanner sc1 = new Scanner(System.in);
			System.out.println("Language selection / Sprachauswahl: EN/DE");
			language = sc1.nextLine().toLowerCase();
			if(!language.equals("de") && !language.equals("en")) {
				System.out.println("Invalid input / Ungültige Eingabe");
			}
		}
		boolean repeat = true;
		while (repeat) {
			String solution = WordPicker(language) + " ";  // space for consistent split results
			System.out.println(solution);
			String attempt = "";
			String current = "";
			int tries = 6;
			boolean solved = false;
			System.out.println("[][][][][]\n[][][][][]\n[][][][][]\n[][][][][]\n[][][][][]\n[][][][][]");
			Scanner sc2 = new Scanner(System.in);
			while (tries > 0 && !solved) {
				String colors = "";
				ArrayList<Character> blacklist = new ArrayList<Character>(5);
				attempt = sc2.nextLine();
				while (WordChecker(attempt) < 0) {
					if(WordChecker(attempt) == -1) {
						System.out.println(invalid_wordlength.get(language));
					}
					if(WordChecker(attempt) == -2) {
						System.out.println(invalid_chars.get(language));
					}
					attempt = sc2.nextLine();
				}
				for (int i = 0; i < 5; i++) {
					if(attempt.charAt(i) == solution.charAt(i)) {  // correct letter
						colors += "2";
						if(solution.split(Character.toString(attempt.charAt(i))).length - 1 == 1) {  // one occurrence
							blacklist.add(attempt.charAt(i));  // don't mark letter again
						}
					} else if(solution.indexOf(attempt.charAt(i)) != -1) {  // letter exists
						if(!blacklist.contains(attempt.charAt(i))) {
							colors += "1";
							if(solution.split(Character.toString(attempt.charAt(i))).length - 1 == 1) {  // one occurrence
								blacklist.add(attempt.charAt(i));  // don't mark letter again
							}
						} else {  // letter already marked
							colors += "0";
						}
					} else {  // letter doesn't exist
						colors += "0";
					}
				}
				current += "\n";
				for (int j = 0; j < 4; j++) {
					current += colorPicker[colors.charAt(j) - '0'] + Character.toUpperCase(attempt.charAt(j)) + resetPicker[colors.charAt(j) - '0'] + " ";
				}
				current += colorPicker[colors.charAt(4) - '0'] + Character.toUpperCase(attempt.charAt(4)) + resetPicker[colors.charAt(4) - '0'];
				if(colors.equals("22222")) {
					solved = true;
				}
				tries -= 1;
				System.out.println("-----------" + current + "\n[][][][][]".repeat(tries));
			}
			solution = solution.substring(0, solution.length() - 1);  // remove trailing space
			if(!solved && tries == 0) {
				System.out.println(lose_message.get(language) + solution.toUpperCase());
			}
			if(solved) {
				if(tries == 5) {
					System.out.println(win5tries_message.get(language) + "\uD83D\uDE0F");
				}
				if(tries == 4) {
					System.out.println(win4tries_message.get(language) + "\uD83E\uDD29");
				}
				if(tries == 3) {
					System.out.println(win3tries_message.get(language) + "\uD83D\uDE01");
				}
				if(tries == 2) {
					System.out.println(win2tries_message.get(language) + "\uD83D\uDE0A");
				}
				if(tries == 1) {
					System.out.println(win1tries_message.get(language) + "\uD83D\uDE42");
				}
				if(tries == 0) {
					System.out.println(win0tries_message.get(language) + "\uD83D\uDE13");
				}
			}
			Scanner sc3 = new Scanner(System.in);
			System.out.println(playagain.get(language));
			String str = sc3.nextLine().toLowerCase() + " ";  // space for consistent split results

			if(str.split("n").length - 1 == 1) {  // contains
				repeat = false;
			}
		}
	}

	public static void main(String[] args) {
		invalid_wordlength.put("en", "Invalid word length! Try again.");
		invalid_wordlength.put("de", "Ungültige Wortlänge! Erneut versuchen.");
		invalid_chars.put("en", "Invalid characters! Try again.");
		invalid_chars.put("de", "Ungültige Zeichen! Erneut versuchen.");
		lose_message.put("de", "You lost! The word was: ");
		lose_message.put("en", "Verloren! Das Wort war: ");
		win5tries_message.put("en", "You lucky bastard!");
		win5tries_message.put("de", "Du Glückskeks!");
		win4tries_message.put("en", "Genius!");
		win4tries_message.put("de", "Genial!");
		win3tries_message.put("en", "Splendid!");
		win3tries_message.put("de", "Großartig!");
		win2tries_message.put("en", "Great!");
		win2tries_message.put("de", "Super!");
		win1tries_message.put("en", "Good!");
		win1tries_message.put("de", "Gut!");
		win0tries_message.put("en", "Phew!");
		win0tries_message.put("de", "Das war knapp!");
		playagain.put("en", "Play again? Y/N");
		playagain.put("de", "Nochmal spielen? J/N");
		WordleGame();
	}

}
