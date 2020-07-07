/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.List;
import grid.SudokuGrid;
import util.NodeCo;

/**
 * Your advanced solver for Killer Sudoku.
 */
public class KillerAdvancedSolver extends KillerSudokuSolver {
	int dimension;
	int box;
	int contraint = 4;
	int MIN_VALUE = 0;
	int[][] solvedGrid;
	boolean ass = false;
	NodeCo[] collection;
	List<Integer> list = new ArrayList<Integer>();
	int board[][];

	public KillerAdvancedSolver() {
		box = (int) Math.sqrt(dimension);
	}

	public void setValues(int dimension) {
		this.dimension = dimension;
		box = (int) Math.sqrt(dimension);
	}

	public void inputToList(SudokuGrid arr) {
		int[] input = arr.getValidInput();
		for (int i = 0; i < input.length; i++) {
			list.add(input[i]);
		}
	}

	@Override
	public boolean solve(SudokuGrid arr) {
//		List<Integer> list = new ArrayList<Integer>();
		setCollection(arr);
		board = arr.getGrid();
		dimension = arr.getDimension();
		inputToList(arr);
		return solveMatrix(arr);

	}

	public boolean solveMatrix(SudokuGrid arr) {
		int[] val;
		int total;
		
//		iterate over each cage
		for (NodeCo next : collection) {
			val = next.val;
			total = 0;
			
//			iterate over each coordinate of a cage
			for (int i = 0; i < val.length; i = i + 2) {
				if (board[val[i]][val[i + 1]] == -1) {
					
//					check each potential value
					for (int value : list) {
						
//						if value doesnt cause the total to go higher then expected, assign
						if (value <= next.total-total) {
							board[val[i]][val[i + 1]] = value;
//							add total
							total += value;
							
//							validate and recursively call
							if (arr.validate(val[i], val[i + 1], next) && solveMatrix(arr)) {
								return true;
							}
//							backtrack
							board[val[i]][val[i + 1]] = -1;
							total -=value;

						}else {
//							breaks as rest of the values are too high
							break;
						}
					}
					return false;

				}
			}
		}
		return true;

	}

	public void setCollection(SudokuGrid arr) {
		collection = arr.getCollection();
	}

	public void possible() {

	}
}