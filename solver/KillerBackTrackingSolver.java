/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;

/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver {
	// TODO: Add attributes as needed.

	public KillerBackTrackingSolver() {
		// TODO: any initialisation you want to implement.
	} // end of KillerBackTrackingSolver()

	@Override
    public boolean solve(SudokuGrid arr) {
		
//		initalises board
    	int input[] = arr.getValidInput();
		int board[][] = arr.getGrid();
		int dimension = arr.getDimension();
		
//		iterates over all values
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				
//				if empty spot
				if (board[i][j] == -1) {
					
//					iterate over all potential values
					for (int k = 0; k < input.length; k++) {
						board[i][j] = input[k];
						
//						recursively call
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
        // placeholder
    } // end of solve()

} // end of class KillerBackTrackingSolver()
