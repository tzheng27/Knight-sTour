package KnightsTour;

import java.util.ArrayList;

public class Knight {
	private int currentRow;
	private int currentColumn;
	private int chessBoard[][] = new int[8][8];
	private int counter;
	private int[][] accessibility= {
			{2, 3, 4, 4, 4, 4, 3, 2},
			{3, 4, 6, 6, 6, 6, 4, 3},
			{4, 6, 8, 8, 8, 8, 6, 4},
			{4, 6, 8, 8, 8, 8, 6, 4},
			{4, 6, 8, 8, 8, 8, 6, 4},
			{4, 6, 8, 8, 8, 8, 6, 4},
			{3, 4, 6, 6, 6, 6, 4, 3},
			{2, 3, 4, 4, 4, 4, 3, 2}
	};
	private static int accessHorizontal[] = {1, 1, -2, 2, -1, -1, 2, -2};
	private static int accessVertical[] = {-2, 2, 1, 1, -2, 2, -1, -1};
	private static int horizontal[] = {2, 1, -1, -2, -2, -1, 1, 2};
	private static int vertical[] = {-1, -2, -2, -1, 1, 2, 2, 1};
	private static int moveNumbers[] = {0, 1, 2, 3, 4, 5, 6, 7};
	
	public Knight() {
		super();
		currentRow = -1;
		currentColumn = -1;
	}

	public Knight(int currentRow, int currentColumn) {
		super();
		this.currentRow = currentRow;
		this.currentColumn = currentColumn;
		this.chessBoard[currentRow][currentColumn] = 1;
		this.counter++;
		this.chgAccessibility();
	}
	
	public void showTour() {
		do {
			this.nextMove();
		} while(this.ifMoreMoves() && this.counter < 64);
	}
	
	public void nextMove() {
		int bestMove = this.getBestMove(getPossibleMoves(this.currentRow, this.currentColumn));
		
		this.currentRow += vertical[bestMove];
		this.currentColumn += horizontal[bestMove];
		this.chessBoard[this.currentRow][this.currentColumn] = this.counter + 1;
		this.counter++;
		this.chgAccessibility();
	}
	
	//Get the best next move
	public int getBestMove(ArrayList<Integer> srcMoves) {
		int lowestAcc = 8;
		int choice;
		ArrayList<Integer> bestMoves = new ArrayList<Integer>();

		for (int srcMove : srcMoves) {
			int nextRow = this.currentRow + vertical[srcMove];
			int nextCol = this.currentColumn + horizontal[srcMove];
			if(nextAccessbility(nextRow, nextCol) < lowestAcc) {
				lowestAcc = nextAccessbility(nextRow, nextCol);
			}
		}
		
		for (int srcMove : srcMoves) {
			int nextRow = this.currentRow + vertical[srcMove];
			int nextCol = this.currentColumn + horizontal[srcMove];
			if(nextAccessbility(nextRow, nextCol) == lowestAcc) {
				bestMoves.add(srcMove);
			}
		}
		
		choice = bestMoves.get(0);
		
		if(bestMoves.size() > 1) {
			lowestAcc = 8;
			ArrayList<Integer> finalBestMoves = new ArrayList<Integer>();
			
			for(int bestMove : bestMoves) {
				int nextRow = this.currentRow + vertical[bestMove];
				int nextCol = this.currentColumn + horizontal[bestMove];
				for(int move : getPossibleMoves(nextRow, nextCol)) {
					if(nextAccessbility(nextRow + vertical[move], nextCol + horizontal[move]) < lowestAcc) {
						lowestAcc = nextAccessbility(nextRow + vertical[move], nextCol + horizontal[move]);
					}
				}
			}
			
			for(int bestMove : bestMoves) {
				ArrayList<Integer> allAccessbilities = new ArrayList<Integer>();
				int nextRow = this.currentRow + vertical[bestMove];
				int nextCol = this.currentColumn + horizontal[bestMove];
				for(int move : getPossibleMoves(nextRow, nextCol)) {
					allAccessbilities.add(nextAccessbility(nextRow + vertical[move], nextCol + horizontal[move]));
				}
				if(allAccessbilities.contains(lowestAcc)) {
					finalBestMoves.add(bestMove);
				}
			}
			
			if(!finalBestMoves.isEmpty()) {
				choice = finalBestMoves.get(0);
			}
		}
		return choice;
	}
	
	//Get all the possible moves from a certain position
	public ArrayList<Integer> getPossibleMoves(int row, int col){
		ArrayList<Integer> possibleMoveNums = new ArrayList<Integer>();
		for(int moveNumber : moveNumbers) {
			if(testNextMove(row, col, moveNumber)) {
				possibleMoveNums.add(moveNumber);
			}
		}
		return possibleMoveNums;
	}
	
	
	//Check if the next potential move or the potential access-or is in board
	public boolean ifInBoard(int curRow, int curCol, int moveNum, boolean ifChgAccess) {
		int testRow = curRow + (ifChgAccess ? accessVertical[moveNum] : vertical[moveNum]);
		int testCol = curCol + (ifChgAccess ? accessHorizontal[moveNum] : horizontal[moveNum]);
		return testRow >= 0 && testRow < 8 && testCol >= 0 && testCol < 8;
	}
	
	//Check if the next potential move is in board and not visited
	public boolean testNextMove(int curRow, int curCol, int moveNum) {
		boolean result = ifInBoard(curRow, curCol, moveNum, false);
		if (result) {
			result = chessBoard[curRow + vertical[moveNum]][curCol + horizontal[moveNum]] == 0;
		}
		return result;
	}
	
	//Check if there are more possible moves from the current position
	public boolean ifMoreMoves() {
		boolean result = false;
		for(int moveNumber : moveNumbers) {
			result = result || testNextMove(this.currentRow, this.currentColumn, moveNumber);
		}
		return result;
	}
		
	//Change the accessibility after a move
	public void chgAccessibility() {
		for(int moveNumber : moveNumbers) {
			if(ifInBoard(this.currentRow, this.currentColumn, moveNumber, true)) {
				int accessorRow = this.currentRow + accessVertical[moveNumber];
				int accessorCol = this.currentColumn+ accessHorizontal[moveNumber];
				if(accessibility[accessorRow][accessorCol] > 0) {
					accessibility[accessorRow][accessorCol] -= 1;
				}
			}
		}
	}
	
	//Get the accessibility of the next possible move position
	public int nextAccessbility(int nextRow, int nextCol) {
		return accessibility[nextRow][nextCol];
	}
	
	public int[][] getAccessbility() {
		return this.accessibility;
	}
	
	public int[][] getChessBoard() {
		return this.chessBoard;
	}
	
	public int getRow() {
		return this.currentRow;
	}
	
	public int getCol() {
		return this.currentColumn;
	}
	
	
	public int getCounter() {
		return this.counter;
	}
	
	public void setRow(int srcRow) {
		this.currentRow = srcRow;
	}
	
	public void setCol(int srcCol) {
		this.currentColumn = srcCol;
	}
	
	public void incrementCounter() {
		this.counter++;
	}
	
	public static int[] getHorizontal() {
		return horizontal;
	}

	public static int[] getVertical() {
		return vertical;
	}
}






















