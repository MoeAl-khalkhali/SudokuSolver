/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;

/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver {
	// TODO: Add attributes as needed.
	int[] input;
	int board[][];
	int dimension;
	boolean ass =false;
	public BackTrackingSolver() {
		// TODO: any initialisation you want to implement.
	} // end of BackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid arr) {
		 input= arr.getValidInput();
		 board= arr.getGrid();
		 dimension = arr.getDimension();
		 return solveGrid(arr);
	} // end of solve()
	
	public boolean solveGrid(SudokuGrid arr) {
//		iterate over all values
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				
//				find an empty spot
				if (board[i][j] == -1) {
//					try all values
					for (int k = 0; k < input.length; k++) {
						board[i][j] =  input[k];
//						validate and call recursively
						if (arr.validatePartial() && solve(arr)) {
							return true;
						}
//						backtrack
						board[i][j] = -1;
					}
					return false;
				}
			}
		}
		return true;
	}

} // end of class BackTrackingSolver()
