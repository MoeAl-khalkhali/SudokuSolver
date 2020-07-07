/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import grid.SudokuGrid;
import java.util.ArrayList; // import the ArrayList class

/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver {
	int dimension;
	int box;
	int contraint = 4;
	int MIN_VALUE = 0;
	ArrayList<Integer> rowsInSolution = new ArrayList<>();
	ArrayList<Integer> rowsDoNotIterate = new ArrayList<>();
	ArrayList<Integer> columnAdded = new ArrayList<>();
	boolean ass = false;
	int[][] solvedGrid;
	Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();

	public AlgorXSolver() {
	}

	public void setValues(int dimension) {
//		sets values 
		this.dimension = dimension;
		box = (int) Math.sqrt(dimension);
	}

	// Building of an empty cover matrix
	public int[][] createCoverMatrix() {
//		initalise cover matrix
		int[][] coverMatrix = new int[dimension * dimension * dimension][dimension * dimension * 4];
		int startPoint = 0;
		
//		create cell constraits
		startPoint = createCellConstraints(coverMatrix, startPoint);
		
//		create row constraints
		startPoint += createRowConstraints(coverMatrix, startPoint);
		
//		constraines column constraints
		startPoint += createColumnConstraints(coverMatrix, startPoint);
		
//		constraints box constraints
		createBoxConstraints(coverMatrix, startPoint);
		return coverMatrix;
	}

	private int createBoxConstraints(int[][] matrix, int startingPoint) {
		int a = startingPoint;
		int c = 0;
		int count = 1;
		int count2 = 0;
		int count3 = 0;
		int temp = startingPoint;
		int temp2 = startingPoint;
		int box = (int) Math.sqrt(dimension);
		for (int i = 0; i < dimension * dimension; i++) {
			
//			if max is reached break
			if (a > dimension * dimension * 4 - 1) {
				break;
			}
			
//			put value 1 for each potential value with an increment of a row and column
			for (int j = 0; j < dimension; j++) {
				matrix[c][a] = 1;
				a++;
				c++;

			}
//			if count is equal to dimension of a box reset counter, and increment second counter
			if (count == box) {
				count2++;
				count = 1;
				temp = a;
			} else {
//			increat first count
				count++;
				a = temp;
			}
//			if the second counter is equal to dimension of a box reset the second counter and the first counter.
			if (count2 == box) {
				count3++;
				count = 1;
				count2 = 0;
				temp = temp2;
				a = temp;
			}
//			if the third counter is equal to dimension of a box reset the second counter and the first counter.
			if (count3 == box) {
				count3 = 0;
				count = 1;
				count2 = 0;
				temp2 = temp + dimension * box;
				temp = temp2;
				a = temp;
			}
		}

		return dimension * dimension;
	}

	private int createColumnConstraints(int[][] matrix, int startingPoint) {
		int a = startingPoint;
		int c = 0;
		int count = 1;
		// for loop for values
		for (int i = 0; i < dimension * dimension; i++) {
			// checks for out of bounds array
			if (a > dimension * dimension * dimension - 1) {
				break;
			}
			// for loop for matrix
			for (int j = 0; j < dimension; j++) {
				
//				set value to 1
				matrix[c][a] = 1;
				a++;
				c++;
				
//				reset starting point of the column
				if (count == dimension * dimension) {
					a = startingPoint;
					count = 1;
				} else {
//					increase count
					count++;
				}

			}

		}

		return dimension * dimension;
	}

	private int createRowConstraints(int[][] matrix, int startingPoint) {
		int a = startingPoint;
		int c = 0;
		int count = 1;
		int temp = startingPoint;
		for (int i = 0; i < dimension * dimension; i++) {
//			ensures no index out of band exception
			if (a > dimension * dimension * dimension - 1) {
				break;
			}
//			set value to one eqaul to input, as each column has {dimension} potential values
			for (int j = 0; j < dimension; j++) {
				matrix[c][a] = 1;
				a++;
				c++;

			}
//			increase counter and reset the column** meaning 9 ones each representing a potential value of that cell.( 9 if it was a 9x9 grid.) 
			if (count == dimension) {
				count = 1;
				temp = a;
			} else {
				count++;
				a = temp;
			}
		}

		return dimension * dimension;
	}

	private int createCellConstraints(int[][] matrix, int header) {
		int a = 0;
		int c = 0;
//		iterate through matrix
		for (int i = 0; i < dimension * dimension; i++) {
			for (int j = 0; j < dimension; j++) {
//				assign value to the rows(as many as the dimensions
				matrix[c][a] = 1;
				c++;
			}
//			next column
			a++;
		}

		return dimension * dimension;
	}

//	 Converting Sudoku grid as a cover matrix
	public int[][] convertInCoverMatrix(int[][] grid) {
		int[][] coverMatrix = createCoverMatrix();

		// Taking into account the values already entered in Sudoku's grid instance
		for (int row = 1; row <= dimension; row++) {
			for (int column = 1; column <= dimension; column++) {
				int n = grid[row - 1][column - 1];
//				if there is a value file the rest with 0's for that cell
				if (n != -1) {
					for (int num = 1; num <= dimension; num++) {
						if (num != n) {
							Arrays.fill(coverMatrix[indexInCoverMatrix(row, column, num)], 0);
						}
					}
				}
			}
		}
//		iterate through grid
		int count = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (grid[i][j] != -1) {
//					add the rows already in the value into the solutions list
					if (!rowsInSolution.contains(count + grid[i][j] - 1))
						rowsInSolution.add((count + grid[i][j] - 1));
				}
				count += dimension;
			}
		}

		return coverMatrix;
	}

	private int indexInCoverMatrix(int row, int column, int num) {
		return (row - 1) * dimension * dimension + (column - 1) * dimension + (num - 1);
	}

	@Override
	public boolean solve(SudokuGrid grid) {
//		set dimensinos
		this.setValues(grid.getDimension());
//		initiatlise the matrix
		int[][] matrix = convertInCoverMatrix(grid.getGrid());
//		retrieve grid
		solvedGrid = grid.getGrid();
//		retrive input
		int[] input = grid.getValidInput();
		int[][] sudokuGrid = grid.getGrid();
//		call solver
		if (this.solveMatrix(matrix, solvedGrid)) {
//			conver solution to grid
		for (int next : rowsInSolution) {
			int col = next / dimension;
			int val = next - (dimension * col);
			int row = col / dimension;
			col = col % dimension;
			sudokuGrid[row][col] = input[val];
		}
//		set the new solution
		grid.setGrid(solvedGrid);
		return true;
		}else {
			return false;
		}

	}

	public int getBestColumn(int[][] grid) {
//		iterates over columns and gets the column with the least ones and is uncovered
		int count = 0;
		int[][] matrix = grid;
		int tempCount = 0;
		int column = -1;
		int x = -1;
//		iterates over matrix
		for (int i = 0; i < dimension * dimension * dimension; i++) {
			for (int j = 0; j < dimension * dimension * 4; j++) {
				if (matrix[i][j] == 1) {
					tempCount++;
					x = j;
				}
			}
			if ((tempCount < count || count == 0) && (!columnAdded.contains(x))) {
				count = tempCount;
				column = x;
			}
			tempCount = 0;
		}

		return column;
	}

	public ArrayList<Integer> getSolution() {
		return rowsInSolution;
	}

	public boolean solveMatrix(int[][] grid, int[][] sGrid) {
		if (!ass) {
			alreadyAddedNumbers(grid);
			ass = true;

		}
		int start = getBestColumn(grid);
//		1. If the matrix A has no columns, the current partial solution
//		2. Otherwise, choose a column c (deterministically). 

		if (start == -1) {
			return true;
		}
		ArrayList<Integer> tempCols = new ArrayList<>();
		ArrayList<Integer> doNotIterate = new ArrayList<>();
		// for backtracking, carries old version before row
		for (int next : rowsDoNotIterate) {
			doNotIterate.add(next);
		}
		for (int next : columnAdded) {
			tempCols.add(next);
		}

		for (int i = 0; i < dimension * dimension * dimension; i++) {

//			3. Choose a row r such that A[r] = 1 (nondeterministically). 
			if (grid[i][start] == 1 && !rowsDoNotIterate.contains(i)) {

//				4. Include row r in the partial solution.
				rowsInSolution.add(i);

				for (int j = 0; j < dimension * dimension * 4; j++) {

//					5. For each column j such that A[r][j] = 1,
					if (grid[i][j] == 1 && !columnAdded.contains(j)) {
						for (int x = 0; x < dimension * dimension * dimension; x++) {

//					        6. for each row i such that A[i][j] = 1, 
							if (grid[x][j] == 1 && !rowsDoNotIterate.contains(x)) {
								if (!rowsDoNotIterate.contains(x)) {

//						            7. delete row i from matrix A. 
									rowsDoNotIterate.add(x);
								}
							}
						}
//					    8. delete column j from matrix A. 
						if (!columnAdded.contains(j)) {
							columnAdded.add(j);
						}

					}

				}
//				validation
				if (solveMatrix(grid, sGrid) && columnAdded.size() == dimension * dimension * 4) {
					CoverToGrid(sGrid);
					return true;
				}
				if (rowsInSolution.contains(i)) {
					rowsInSolution.remove(rowsInSolution.size() - 1);
				}
				columnAdded = tempCols;
				rowsDoNotIterate = doNotIterate;

			}

		}

		return false;
	}

	private void alreadyAddedNumbers(int[][] grid) {
		int[][] matrix = grid;

		// removes duplicate columns for predefined valuesd
		for (int next : rowsInSolution) {
			for (int i = 0; i < dimension * dimension * 4; i++) {
				if (matrix[next][i] == 1) {
					for (int j = 0; j < dimension * dimension * dimension; j++) {
						if (matrix[j][i] == 1) {
							if (!rowsDoNotIterate.contains(j)) {
//					            delete row i from matrix A. 
								rowsDoNotIterate.add(j);
							}
						}
					}
					if (!columnAdded.contains(i)) {
						columnAdded.add(i);
					}
				}
			}

		}
	}

	public int[][] CoverToGrid(int[][] grid) {
//		simple arithmatic that takes each row index and converts it into a coordinate with a value for the grid
		for (int next : rowsInSolution) {
			int col = next / dimension;
			int val = next - (dimension * col);
			int row = col / dimension;
			col = col % dimension;
			grid[row][col] = val + 1;
		}
		return grid;
	}



}