/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import util.NodeCo;

/**
 * Class implementing the grid for standard Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task A and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class StdSudokuGrid extends SudokuGrid {
	// TODO: Add your own attributes
	private int[][] grid;
	int dimension;
	int[] validInput;

	public StdSudokuGrid() {
		super();

		// TODO: any necessary initialisation at the constructor
	} // end of StdSudokuGrid()

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {
		int count = 0;
		Scanner sc = new Scanner(new File(filename));
		sc.useDelimiter("\n");
		String dim = " ";
		int a = 0;
		int b = 0;
		int c = 0;
		String arr;
		while (sc.hasNext()) {
			if (count == 0) {
//				gets dimension frmo file
				dim = sc.next().trim();
				dimension = Integer.parseInt(dim);
				
//				initialise grid and put all values as -1
				grid = new int[dimension][dimension];
				for (int i = 0; i < dimension; i++) {
					for (int j = 0; j < dimension; j++) {
						
//						sets grid values
						grid[i][j] = -1;
					}
				}
				count++;
			}
			if (count == 1) {
				String[] tmp = sc.next().split(" ");
				
//				initialises valid input based on input length
				validInput = new int[tmp.length];
				for (int i = 0; i < tmp.length; i++) {
					
//					string input to int input
					validInput[i] = Integer.parseInt(tmp[i].trim());

				}
				count++;
			}
			
//			convert coordinates adn values into the grid
			arr = sc.next();
			String arrVal[] = arr.split(",");
			String[] secVal = arrVal[1].split(" ");
			
//			row
			a = Integer.parseInt(arrVal[0].trim());
			
//			column
			b = Integer.parseInt(secVal[0].trim());
			
//			value
			c = Integer.parseInt(secVal[1].trim());
			grid[a][b] = c;
		}
		sc.close();

	} // end of initBoard()

	@Override
	public void outputGrid(String filename) throws FileNotFoundException, IOException {
		File file = new File(filename);
		BufferedWriter writer = null;
		StringBuilder a = new StringBuilder();
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (j != dimension - 1) {
					a.append(grid[i][j] + ",");
				} else {
					a.append(grid[i][j]);
				}
			}
			if (i != dimension - 1) {
				a.append("\n");
			}
		}
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(a.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	} // end of outputBoard()

	@Override
	public String toString() {
		StringBuilder a = new StringBuilder();
//		iterates over grid
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (j != dimension - 1) {
					if (grid[i][j] == -1) {
						a.append(" ,");

					} else {
//						adds input with comma
						a.append(grid[i][j] + ",");
					}
				} else {
//					no comma as its the last column
					if (grid[i][j] == -1) {
						a.append(" ");

					} else {
						a.append(grid[i][j]);
					}
				}
			}
//			new line unless last row
			if (i != dimension - 1) {
				a.append("\n");
			}
		}
		return String.valueOf(a);
	} // end of toString()

	public int[] getValidInput() {
		return validInput;
	}

	@Override
	public boolean validate() {
		// validate rows
		List<Integer> validate = new ArrayList<Integer>();
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (validate.contains(grid[i][j])) {
					return false;
				} else {
					if (grid[i][j] != -1) {

						validate.add(grid[i][j]);
					}else {
						return false;
					}
				}
			}
			validate.clear();
		}
		// validate columns
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {

				if (j == dimension - 1) {
				}
				if (validate.contains(grid[j][i])) {
					return false;
				} else {
					if (grid[j][i] != -1) {
						validate.add(grid[j][i]);
					}
				}
			}
			validate.clear();
		}
		int sub = (int) Math.sqrt(dimension);
		int a = sub;
		int b = sub;
		int c = 0;
		int d = 0;
		for (int i = 0; i < sub; i++) {
			for (int j = 0; j < sub; j++) {
				if (b < dimension && a < dimension) {
					
//					checks boxes
					if (!checkSubBox(grid, c, d, a, b)) {
						return false;
					}
				}

				d = b;
				b += b;
			}
			d = 0;
			b = sub;
			c = a;
			a = a + a;
		}
		return true;
	} // end of validate()

	public int[][] getGrid() {
		return grid;
	}

	public void setGrid(int[][] arr) {
		grid = arr;
	}

	public boolean checkSubBox(int[][] arr, int minI, int minJ, int maxI, int maxJ) {
		List<Integer> validate = new ArrayList<Integer>();
//		iterates based off starting and end point
		for (int i = minI; i < maxI; i++) {
			for (int j = minJ; j < maxJ; j++) {
				if (validate.contains(grid[j][i])) {
					return false;
				} else {
					if (grid[j][i] != -1) {
						validate.add(grid[j][i]);
					}
				}
			}
		}
		validate.clear();
		return true;
	}

	// get grid dimensions
	public int getDimension() {
		return dimension;
	}

	@Override
	public NodeCo[] getCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(int i, int j, NodeCo node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validatePartial() {
		// validate rows
				List<Integer> validate = new ArrayList<Integer>();
				for (int i = 0; i < dimension; i++) {
					for (int j = 0; j < dimension; j++) {
						if (validate.contains(grid[i][j])) {
							return false;
						} else {
							if (grid[i][j] != -1) {

								validate.add(grid[i][j]);
							}
						}
					}
					validate.clear();
				}
				// validate columns
				for (int i = 0; i < dimension; i++) {
					for (int j = 0; j < dimension; j++) {

						if (j == dimension - 1) {
						}
						if (validate.contains(grid[j][i])) {
							return false;
						} else {
							if (grid[j][i] != -1) {
								validate.add(grid[j][i]);
							}
						}
					}
					validate.clear();
				}
				int sub = (int) Math.sqrt(dimension);
				int a = sub;
				int b = sub;
				int c = 0;
				int d = 0;
				for (int i = 0; i < sub; i++) {
					for (int j = 0; j < sub; j++) {
						if (b < dimension && a < dimension) {
							
//							checks boxes
							if (!checkSubBox(grid, c, d, a, b)) {
								return false;
							}
						}

						d = b;
						b += b;
					}
					d = 0;
					b = sub;
					c = a;
					a = a + a;
				}
				return true;	
	}
} // end of class StdSudokuGrid
