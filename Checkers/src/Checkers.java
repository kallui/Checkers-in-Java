
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Checkers {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner userInput = new Scanner(System.in);
		int input;
		System.out.println("==============================================================");
		System.out.println("=========================CHECKERS=============================");
		System.out.println("==============================================================");

		do {
			System.out.println();
			System.out.println("1. Developer");
			System.out.println("2. Instruction on how to play");
			System.out.println("3. Play");
			System.out.println("4. Show Winners");
			System.out.println("5. Exit");
			System.out.println();
			System.out.print("[1-5]: ");
			input = userInput.nextInt();
			
			switch (input) {
			case 1 : developer();
					break;
			case 2 : instructions();
					break;
			case 3 :play();
					break;
			case 4: showWinners();
					break;
			}
		}while (input != 5);
	}
	
	
	public static void showWinners() throws FileNotFoundException {
		File f = new File("theData.txt");
		Scanner sc = new Scanner(f);
		System.out.println();
		System.out.println("===================================LIST OF WINNERS=====================================");
		while (sc.hasNextLine()) {
			System.out.println(sc.nextLine());
		}
	}
	
	public static void developer() {
		System.out.println("===================================DEVELOPER=====================================");
		System.out.println("Developer : Nicholas Januar (100347148) - CPSC 1150 Sec 003" );
	}
	
	public static void instructions() {
		System.out.println("===================================INSTRUCTIONS ON HOW TO PLAY=====================================");
		System.out.println("**FORCE JUMP : OFF**");
		System.out.println("**MULTIPLE JUMPS : ON**");
		System.out.println();
		System.out.println("**X == NORMAL TOKEN, x == KING**");
		System.out.println("**O == NORMAL TOKEN, o == KING**");
		System.out.println();
		System.out.println("“The goal of Checkers, or \"Draughts\", is to remove all your opponent's pieces from the board. Use coordinates (row and column) to move your pieces around the board. Your pieces can only move forward one tile diagonally.\r\n" + 
				"\r\n" + 
				"To capture an opponent's piece and remove it from the board, you need to \"jump\" over their piece with one of yours. Jumping is (NOT) mandatory, which means you (don't) have to jump with one of your pieces if you are able to. \r\n" + 
				"\r\n" + 
				"If one of your pieces gets to the opposite side of the board (your opponent's back row), it will turn into a King. Kings can move and jump diagonally in any direction (remember, your regular pieces can only move forward). Kings can even combine jumps forward and backward on the same turn!\r\n" + 
				"\r\n" + 
				"You win by removing all your opponent's pieces from the board, or if your opponent can't make a move. “ - Reference site: https://www.coolmathgames.com/0-checkers\r\n" + 
				"");
	}
	
	public static void play() {
		//gets the start time================
		SimpleDateFormat formatterTime1 = new SimpleDateFormat("HH:mm");
		Date d1 = new Date();
		String s1 = formatterTime1.format(d1);
		
		Scanner userInput = new Scanner(System.in);
		char [][]board = initboard(8);
		char []marker = {'X','O'};
		boolean done = false;
		boolean multiple = false;
		int turn = 0;
		String playername = "";
		
		System.out.println("Enter player 1 name: ");
		String player1 = userInput.nextLine();
		System.out.println("Enter player 2 name: ");
		String player2 = userInput.nextLine();

		display(board);
		do {
			if (turn == 0)
				playername = player1;
			else if (turn == 1)
				playername = player2;
			
			System.out.println();
			System.out.println(playername + " (" + marker[turn] + ") " + " move.");
			String test;
			do {
			System.out.print("Enter coordinate of the token you want to move (e.g. A1 / B5 / e8): ");
			test = userInput.nextLine().toUpperCase();
			}while (test.length() != 2 && test.charAt(0) >= 'A' && test.charAt(0) <= 'H' && test.charAt(1) >= '1' && test.charAt(1) <= '8');
			
			String coordinateToken = test;
			int rowToken = coordinateToken.charAt(0) - 65;
			int colToken = coordinateToken.charAt(1) - 49;
			
			do {
			System.out.println("Enter coordinate of where you want the token to go (e.g. A1 / B5 / e8): ");
			test = userInput.nextLine().toUpperCase();
			}while (test.length() != 2 && test.charAt(0) >= 'A' && test.charAt(0) <= 'H' && test.charAt(1) >= '1' && test.charAt(1) <= '8');
			
			String coordinateMove = test;
			int rowMove = coordinateMove.charAt(0) - 65;
			int colMove = coordinateMove.charAt(1) - 49;
			
			if ((rowToken <= 7 && rowToken >= 0)	 && 	(colToken <= 7	&&	colToken >= 0)
				&& (rowMove <= 7 && rowMove >= 0)	 && 	(colMove <= 7	&&	colMove >= 0))
			{
				if ((rowMove == rowToken -1 || rowMove == rowToken + 1) && multiple == false) {			//move
					if(move(board,rowToken,colToken,rowMove,colMove)) {
						if(checkWinner(board,turn))
							done = true;
						else {
						turn ^= 1;
						}
					}
				}
				else if (rowMove == rowToken -2 || rowMove == rowToken +2) {		// eat
					if(eat(board,rowToken,colToken,rowMove,colMove)) {
						multiple = false;
						if(checkWinner(board,turn)) {
							done = true;
						}
						else if (checkMultipleJump(board,rowMove,colMove)) {
							multiple = true;
						}
						else
							turn ^=1;
					}
				}
			}
			changeKingMarker(board);
			display(board);
			
		}while(done != true );
		
		System.out.println(playername + " ("+ marker[turn] + ")  wins ");
		
		//gets the finish time
		SimpleDateFormat formatterTime2 = new SimpleDateFormat("HH:mm");
		Date d2 = new Date(); 
		String s2 = formatterTime2.format(d2);
		
		WinnersData(player1,player2,turn,s1,s2);	// WRITES DATA IN THE .txt file
	}
	
	
	public static void WinnersData(String p1, String p2, int turnWinner, String s1, String s2) {
		File f = new File("theData.txt");
		
		try {
			FileWriter fw = new FileWriter(f,true); // to append to the file
			
			//DATE
			SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy ");
			Date d = new Date();
			String s = formatter.format(d);
			
			if (turnWinner == 0)
				fw.append(String.format("%s - %1s (Won) vs %1s - %s to %s\n", s, p1,p2,s1,s2));
			else if (turnWinner == 1)
				fw.append(String.format("%s - %1s vs %1s (Won) - %s to %s\n", s, p1,p2,s1,s2));
			fw.close();
			
		}catch (IOException e) {
			System.out.println("error");
		} 

		f = null;
	}
	
	
	private static boolean checkMultipleJump(char[][] board, int rowMove, int colMove) {
		boolean isValid = false;
		//X,x,o check multiple
		
		if (board[rowMove][colMove] == 'X' ||board[rowMove][colMove] == 'x' || board[rowMove][colMove] == 'o' ) {
			
			//left
			if (colMove != 0 && colMove != 1 && rowMove != 0 && rowMove != 1) {
				
				if(board[rowMove-2][colMove-2]!= 'O' && board[rowMove-2][colMove-2] != 'X' && (board[rowMove-2][colMove-2]!= 'o' && board[rowMove-2][colMove-2] != 'x' )) 
				{
					//X / x
					if ((board[rowMove][colMove] == 'X' || board[rowMove][colMove] == 'x' ) && ((board[rowMove-1][colMove-1] == 'O' || board[rowMove-1][colMove-1] == 'o'))){
						isValid = true;
					}
					//o
					else if ((board[rowMove][colMove] == 'o') && ((board[rowMove-1][colMove-1] == 'X' || board[rowMove-1][colMove-1] == 'x'))){
						isValid = true;
					}
				}
			}
			
			//right 
			else if (colMove != 7 && colMove != 6 && rowMove != 0 && rowMove != 1) {
				
				if(board[rowMove-2][colMove+2]!= 'O' && board[rowMove-2][colMove+2] != 'X' &&board[rowMove-2][colMove+2]!= 'o' && board[rowMove-2][colMove+2] != 'x' ) 
				{
					//X / x
					if ((board[rowMove][colMove] == 'X' || board[rowMove][colMove] == 'x' ) && ((board[rowMove-1][colMove+1] == 'O' || board[rowMove-1][colMove+1] == 'o'))){
						isValid = true;
					}
					//o
					else if ((board[rowMove][colMove] == 'o') && ((board[rowMove-1][colMove+1] == 'X' || board[rowMove-1][colMove+1] == 'x'))){
						isValid = true;
					}
				}
			}
		}
		
		//O,o,x check multiple ====================================================
		else if (board[rowMove][colMove]== 'O' || board[rowMove][colMove]== 'o' || board[rowMove][colMove]== 'x') {		
				//left
				if (colMove != 0 && colMove != 1 && rowMove != 7 && rowMove != 6) {
					
					if(board[rowMove+2][colMove-2]!= 'O' && board[rowMove+2][colMove-2] != 'X' && (board[rowMove+2][colMove-2]!= 'o' && board[rowMove+2][colMove-2] != 'x' )) 
					{
						//O/ o
						if ((board[rowMove][colMove] == 'O' || board[rowMove][colMove] == 'o' ) && ((board[rowMove+1][colMove-1] == 'X' || board[rowMove+1][colMove-1] == 'x'))){
							isValid = true;
						}
						//x
						else if ((board[rowMove][colMove] == 'x') && ((board[rowMove+1][colMove-1] == 'O' || board[rowMove+1][colMove-1] == 'o'))){
							isValid = true;
						}
					}
				}
				//right 
				else if (colMove != 7 && colMove != 6 && rowMove != 7 && rowMove != 6) {
					
					if(board[rowMove+2][colMove+2]!= 'O' && board[rowMove+2][colMove+2] != 'X' &&board[rowMove+2][colMove+2]!= 'o' && board[rowMove+2][colMove+2] != 'x' ) 
					{
						//O/ o
						if ((board[rowMove][colMove] == 'O' || board[rowMove][colMove] == 'o' ) && ((board[rowMove+1][colMove+1] == 'X' || board[rowMove+1][colMove+1] == 'x'))){
							isValid = true;
						}
						//x
						else if ((board[rowMove][colMove] == 'x') && ((board[rowMove+1][colMove+1] == 'O' || board[rowMove+1][colMove+1] == 'o'))){
							isValid = true;
						}
					}
				}
		}
		return isValid;
	}
	
	
	public static boolean changeKingMarker(final char[][] board ) {
		boolean isValid = false;
		// X
		for (int x = 0 ; x < 8 ; x++) {
			if (board[0][x] == 'X') {
				isValid = true;
				board[0][x] = 'x';	
			}
		}
		// O
		for (int x = 0 ; x < 8 ; x++) {
			if (board[7][x] == 'O') {
				isValid = true;
				board[7][x] = 'o';
			}
		}
		return isValid;
	}
	
	
	public static boolean move(final char[][] board , int rowToken, int colToken,int rowMove, int colMove) {
		boolean isValid = false;
		// X ,x ,o MOVE
		if ((rowMove == rowToken- 1) &&(board[rowToken][colToken] == 'X' || board[rowToken][colToken] == 'o' || board[rowToken][colToken] == 'x' ) && board[rowMove][colMove] != 'X' && board[rowMove][colMove] != 'O'&& 
				board[rowMove][colMove] != 'x' && board[rowMove][colMove] != 'o') {
			if (rowMove == rowToken-1  && ( colMove == colToken-1 || colMove == colToken+1 ) ) {// MOVE to a blank space 
				if(board[rowToken][colToken] == 'X') {
					board[rowMove][colMove] = 'X';
					board[rowToken][colToken] = ' ';
					isValid = true;
				}
				else if (board[rowToken][colToken] == 'o') {
					board[rowMove][colMove] = 'o';
					board[rowToken][colToken] = ' ';
					isValid = true;
				}
				else if (board[rowToken][colToken] == 'x') {
					board[rowMove][colMove] = 'x';
					board[rowToken][colToken] = ' ';
					isValid = true;
				}
			}
		}
		// O , o, x  MOVE 
		else if (((rowMove == rowToken + 1 ))&&(board[rowToken][colToken] == 'O' || board[rowToken][colToken] == 'x' ||board[rowToken][colToken] == 'o') && board[rowMove][colMove] != 'X' && board[rowMove][colMove] != 'O' 
				&& board[rowMove][colMove] != 'x' && board[rowMove][colMove] != 'o') {
			if (rowMove == rowToken+1  && ( colMove == colToken-1 || colMove == colToken+1 )) {	// MOVE to a blank space
				
				if(board[rowToken][colToken] == 'O') {
					board[rowMove][colMove] = 'O';
					board[rowToken][colToken] = ' ';
					isValid = true;
				}
				else if (board[rowToken][colToken] == 'o') {
					board[rowMove][colMove] = 'o';
					board[rowToken][colToken] = ' ';
					isValid = true;
				}
				else if (board[rowToken][colToken] == 'x') {
					board[rowMove][colMove] = 'x';
					board[rowToken][colToken] = ' ';
					isValid = true;
				}
			}
		}
		return isValid;
	}
	
	public static boolean eat(char[][] board , int rowToken, int colToken,int rowMove, int colMove) {
		boolean isValid = false;
		//X,x, o
		if ((rowMove == rowToken -2 ) && (board[rowToken][colToken] == 'X' || board[rowToken][colToken] == 'o' || board[rowToken][colToken] == 'x')) {
			if (rowMove == rowToken-2 && board[rowMove][colMove] != 'X' && board[rowMove][colMove] != 'O' && 
					board[rowMove][colMove] != 'x' && board[rowMove][colMove] != 'o' )	{	// EAT X
				
				if((colMove == colToken-2 )){	//LEFT
					
					if (board[rowToken][colToken] == 'X' && (board[rowToken-1][colToken-1] == 'O' || board[rowToken-1][colToken-1] == 'o' )) {// X eats O/o
						board[rowMove][colMove] = 'X';
						isValid = true;
						board[rowToken-1][colToken-1] = ' ';
						board[rowToken][colToken] = ' ';
					}
					else if (board[rowToken][colToken] == 'x' && (board[rowToken-1][colToken-1] == 'O' || board[rowToken-1][colToken-1] == 'o' )) {// x eats O/o
						board[rowMove][colMove] = 'x';
						isValid = true;
						board[rowToken-1][colToken-1] = ' ';
						board[rowToken][colToken] = ' ';
					}
					else if (board[rowToken][colToken] == 'o' && (board[rowToken-1][colToken-1] == 'x' ||board[rowToken-1][colToken-1] == 'X')) {// o eats X/x
						board[rowMove][colMove] = 'o';
						isValid = true;
						board[rowToken-1][colToken-1] = ' ';
						board[rowToken][colToken] = ' ';
					}	
				}
				
				else if (colMove == colToken+2 ) { //RIGHT
				
					if (board[rowToken][colToken] == 'X'&& (board[rowToken-1][colToken+1] == 'O' || board[rowToken-1][colToken+1] == 'o') ) { // X eats O/o
						board[rowMove][colMove] = 'X';
						isValid = true;
						board[rowToken-1][colToken+1] = ' ';
						board[rowToken][colToken] = ' ';
					}
					else if (board[rowToken][colToken] == 'x' && (board[rowToken-1][colToken+1] == 'O' || board[rowToken-1][colToken+1] == 'o') ) { // x eats O/o
						board[rowMove][colMove] = 'x';
						isValid = true;
						board[rowToken-1][colToken+1] = ' ';
						board[rowToken][colToken] = ' ';
					}
					else if (board[rowToken][colToken] == 'o' && (board[rowToken-1][colToken+1] == 'x' ||  board[rowToken-1][colToken+1] == 'X')) { // o eats X/x
						board[rowMove][colMove] = 'o';
						isValid = true;
						board[rowToken-1][colToken+1] = ' ';
						board[rowToken][colToken] = ' ';
					}
				}
			}
		}
		// O,o,  x
		else if ((rowMove == rowToken +2) && (board[rowToken][colToken]  == 'O' || board[rowToken][colToken]  == 'x' || board[rowToken][colToken] == 'o')) {
			
			if (rowMove == rowToken+2 && board[rowMove][colMove] != 'X' && board[rowMove][colMove] != 'O'
					&& board[rowMove][colMove] != 'x' && board[rowMove][colMove] != 'o')	{	// EAT O

				if(colMove == colToken-2){	//LEFT
					
					if (board[rowToken][colToken] == 'O' && board[rowToken+1][colToken-1] == 'X' || board[rowToken+1][colToken-1] == 'x') { // O eats X/x
						board[rowMove][colMove] = 'O';
						isValid = true;
						board[rowToken+1][colToken-1] = ' ';
						board[rowToken][colToken] = ' ';
					}
					else if (board[rowToken][colToken] == 'o' && board[rowToken+1][colToken-1] == 'X' || board[rowToken+1][colToken-1] == 'x') { // o eats X/x
						board[rowMove][colMove] = 'o';
						isValid = true;
						board[rowToken+1][colToken-1] = ' ';
						board[rowToken][colToken] = ' ';
					}
					else if (board[rowToken][colToken] == 'x' && (board[rowToken+1][colToken-1] == 'O' ||board[rowToken+1][colToken-1] == 'o')) {//x eats O/o
						board[rowMove][colMove] = 'x';
						isValid = true;
						board[rowToken+1][colToken-1] = ' ';
						board[rowToken][colToken] = ' ';
					}
				}
				else if (colMove == colToken+2 ) { //RIGHT
					
					if (board[rowToken][colToken] == 'O' && board[rowToken+1][colToken+1] == 'X' || board[rowToken+1][colToken+1] == 'x') { // O eats X/x
						board[rowMove][colMove] = 'O';
						isValid = true;
						board[rowToken+1][colToken+1] = ' ';
						board[rowToken][colToken] = ' ';
					}
					else if (board[rowToken][colToken] == 'o' && board[rowToken+1][colToken+1] == 'X' || board[rowToken+1][colToken+1] == 'x') { // O eats X/x
						board[rowMove][colMove] = 'o';
						isValid = true;
						board[rowToken+1][colToken+1] = ' ';
						board[rowToken][colToken] = ' ';
					}
					else if (board[rowToken][colToken] == 'x' && (board[rowToken+1][colToken+1] == 'O' ||board[rowToken+1][colToken+1] == 'o')) {//x eats O/o
						board[rowMove][colMove] = 'x';
						isValid = true;
						board[rowToken+1][colToken+1] = ' ';
						board[rowToken][colToken] = ' ';
					}
				}
			}
		
		}
		return isValid;
	}
	
	public static char [][]initboard(int size){
		char [][]board = new char[size][size];
		for (int x = 0 ; x < size ; x++) {
			for (int y = 0 ; y < size ; y++	) {
				if ((x+1) % 2 != 0) {	//odd rows
					if ((y+1) % 2 == 0 ) {	//even cols
						if ((x + 1) < 4)
							board[x][y] = 'O';
						 if ((x + 1) > 5)
							board[x][y] = 'X';
					}
				}
				else if ((x+1) % 2 == 0) {	//even rows
					if ((y+1) % 2 != 0) {	//odd rows
						if ((x + 1) < 4)
							board[x][y] = 'O';
						if ((x + 1) > 5)
							board[x][y] = 'X';
					}
				}
			}
		}
		return board;
	}
	
	
	public static void display(char[][] board) {
		System.out.println();
		System.out.printf("%s", "  1  2  3  4  5  6  7  8");
		System.out.println();
		for (int x = 0 ; x < 8 ; x++) {
			System.out.print((char)('A' + x));
			for (int y = 0 ; y < 8 ; y++) {
				System.out.printf("|%1c|", board[x][y]);
			}
			System.out.println();
		}
	}
	public static boolean checkWinner(char [][]board, int turn) {
		boolean isValid = false;
		//1. all tokens dead
		int xcount = 0;
		int ocount = 0;
		
		for(int x = 0 ; x < 8 ; x++	) {
			for(int y = 0 ; y < 8 ; y++) {
				if(board[x][y] == 'X' || board[x][y] == 'x'	)
					xcount++;
				else if(board[x][y] == 'O' || board[x][y] == 'o')
					ocount++;
			}
		}
		if (xcount == 0)
			isValid = true;
		else if (ocount == 0)
			isValid = true;

		//2. no possible moves
		// loop check for every slot, IF a token can MOVE, immediately break the loop and isValid is still false.
		
		for (int x = 0; x < 8 ; x++) { 
			for(int y = 0 ; y < 8 ; y++) {
				
				if(board[x][y] == 'X' ||board[x][y]== 'x') {// X , x forward=====================
					if(x!=0 &&  y!= 0 && board[x-1][y-1] != 'X'&& board[x-1][y-1] != 'O' && board[x-1][y-1] != 'o' && board[x-1][y-1] != 'x') {//left move
						break;
					}
					else if (x!=0 && y!=7 && board[x-1][y+1] != 'X'&& board[x-1][y+1] != 'O' && board[x-1][y+1] != 'o' && board[x-1][y+1] != 'x') {
						break;
					}//right move
					else if (x!=0 && x!= 1 && y!=0 && y!=1 && board[x-2][y-2] != 'X'&& board[x-2][y-2] != 'O' && board[x-2][y-2] != 'o' && board[x-2][y-2] != 'x'
							&&( board[x-1][y-1] == 'O' || board[x-1][y-1] == 'o')) 	{
						break;
					}													//left eat
					else if (x!=0 && x!= 1 && y!=6 && y!=7 && board[x-2][y+2] != 'X'&& board[x-2][y+2] != 'O' && board[x-2][y+2] != 'o' && board[x-2][y+2] != 'x'
							&& (board[x-1][y+1] == 'O'|| board[x-1][y+1] == 'o')) {
						break;
					}		//right eat

				}
				else if (board[x][y] == 'o') {			  // o forward==============================
					if(x!=0 &&  y!= 0 && board[x-1][y-1] != 'X'&& board[x-1][y-1] != 'O' && board[x-1][y-1] != 'o' && board[x-1][y-1] != 'x') {
						break;
					}//left move
					else if (x!=0 && y!=7 && board[x-1][y+1] != 'X'&& board[x-1][y+1] != 'O' && board[x-1][y+1] != 'o' && board[x-1][y+1] != 'x') {
						break;
					}//right move
				
					else if (x!=0 && x!= 1 && y!=0 && y!=1 && board[x-2][y-2] != 'X'&& board[x-2][y-2] != 'O' && board[x-2][y-2] != 'o' && board[x-2][y-2] != 'x'
							&&( board[x-1][y-1] == 'X' || board[x-1][y-1] == 'x')) 			{
						break;
					}											//left eat
						
					else if (x!=0 && x!= 1 && y!=6 && y!=7 && board[x-2][y+2] != 'X'&& board[x-2][y+2] != 'O' && board[x-2][y+2] != 'o' && board[x-2][y+2] != 'x'
							&& (board[x-1][y+1] == 'X'|| board[x-1][y+1] == 'x')) {
						break;
					}	//right eat

				}
				else if (board[x][y] == 'O' || board[x][y] == 'o') {	//O , o backwards===============================
					if(x!=7 &&  y!= 0 && board[x+1][y-1] != 'X'&& board[x+1][y-1] != 'O' && board[x+1][y-1] != 'o' && board[x+1][y-1] != 'x') {
						break;
					} //left move
					
					else if (x!=7 && y!=7 && board[x+1][y+1] != 'X'&& board[x+1][y+1] != 'O' && board[x+1][y+1] != 'o' && board[x+1][y+1] != 'x') {
						break;
					}//right move
					
					else if (x!= 7 && x!= 6 && y != 0 && y!= 1 && board[x+2][y-2] != 'X'&& board[x+2][y-2] != 'O' && board[x+2][y-2] != 'o' && board[x+2][y-2] != 'x'
							&& (board[x+1][y-1] == 'X'|| board[x+1][y-1] == 'x')) {
						break;
					} //left eat
						
					else if (x!= 7 && x!= 6 && y != 7 && y!= 6 && board[x+2][y+2] != 'X'&& board[x+2][y+2] != 'O' && board[x+2][y+2] != 'o' && board[x+2][y+2] != 'x'
							&& (board[x+1][y+1] == 'X'|| board[x+1][y+1] == 'x')) {
						break;
					} //right eat

				}
				else if (board[x][y] == 'x') { // x backwards=====================================================
					if(x!=7 &&  y!= 0 && board[x+1][y-1] != 'X'&& board[x+1][y-1] != 'O' && board[x+1][y-1] != 'o' && board[x+1][y-1] != 'x') {
						break;
					} //left move
					else if (x!=7 && y!=7 && board[x+1][y+1] != 'X'&& board[x+1][y+1] != 'O' && board[x+1][y+1] != 'o' && board[x+1][y+1] != 'x') {
						break;
					}//right move
					
					else if (x!= 7 && x!= 6 && y != 0 && y!= 1 && board[x+2][y-2] != 'X'&& board[x+2][y-2] != 'O' && board[x+2][y-2] != 'o' && board[x+2][y-2] != 'x'
							&& (board[x+1][y-1] == 'O'|| board[x+1][y-1] == 'o')) {
						break;
					} //left eat
	
					else if (x!= 7 && x!= 6 && y != 7 && y!= 6 && board[x+2][y+2] != 'X'&& board[x+2][y+2] != 'O' && board[x+2][y+2] != 'o' && board[x+2][y+2] != 'x'
							&& (board[x+1][y+1] == 'O'|| board[x+1][y+1] == 'o')) {
						break;
					}  //right eat	
					
					
					else if (board[x][y] != 'X'&& board[x][y] != 'O' && board[x][y] != 'o' && board[x][y] != 'x')
						continue;
					else 
						isValid = true;
				
				}
			}
		}
		return isValid;
	}
}
