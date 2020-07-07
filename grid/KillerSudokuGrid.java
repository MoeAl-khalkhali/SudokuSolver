/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import util.NodeCo;

/**
 * Class implementing the grid for Killer Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task E and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid {
	private int[][] grid;
	int dimension;
	int sub;

	int[] validInput;
	NodeCo[] cageCollection;
	// TODO: Add your own attributes

	public KillerSudokuGrid() {
		super();

	} // end of KillerSudokuGrid()\
	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {
		int count = 0;
		Scanner sc = new Scanner(new File(filename));
		sc.useDelimiter("\n");
		String dim = " ";
		int totalCages;
		String arr;
		int total;
		int secCounter = 0;
		while (sc.hasNext()) {
			// initialise grid
			if (count == 0) {
				dim = sc.next().trim();
				dimension = Integer.parseInt(dim);
				sub = (int) Math.sqrt(dimension);
				grid = new int[dimension][dimension];
				for (int i = 0; i < dimension; i++) {
					for (int j = 0; j < dimension; j++) {
						grid[i][j] = -1;
					}
				}
				count++;
			}
			// create valid input array of possible inputs
			if (count == 1) {
				String[] tmp = sc.next().split(" ");
				validInput = new int[dimension];
				for (int i = 0; i < tmp.length; i++) {
					validInput[i] = Integer.parseInt(tmp[i].trim());
				}
				count++;
			}
			// create object of cages
			if (count == 2) {
				totalCages = Integer.parseInt(sc.next().trim());
				cageCollection = new NodeCo[totalCages];
				count++;
			}
			arr = sc.next();
			String arrVal[] = arr.split(" ");
			
			//total for cages
			total = Integer.parseInt(arrVal[0].trim());
			
			//initialise array for coordinates
			int[] val = new int[(arrVal.length - 1) * 2];
			int counter = 0;
			String[] secVal;
			for (int i = 1; i < arrVal.length; i++) {
				secVal = arrVal[i].split(",");
				for (int j = 0; j < secVal.length; j++) {
					
//					string values to integer
					val[counter] = Integer.parseInt(secVal[j].trim());
					counter++;
				}

			}
			// initialise new object in collection
			cageCollection[secCounter] = new NodeCo(total, val);
			secCounter++;
		}
		sc.close();

		// TODO
	} // end of initBoard()

	@Override
	public void outputGrid(String filename) throws FileNotFoundException, IOException {
//		opens file
		File file = new File(filename);
		BufferedWriter writer = null;
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
					
//					adds empty input
					if (grid[i][j] == -1) {
						a.append(" ");

					} else {
						
//						adds input without comma as its the last column
						a.append(grid[i][j]);
					}
				}
			}
//			adds input without newline as its the last row

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

	public NodeCo[] getCollection() {
		return cageCollection;
	}

	@Override
	public String toString() { // TODO
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
		// placeholder
		return String.valueOf(a);
	} // end of toString()

//	validation for the full grid
	@Override
	public boolean validate() {
		int cLength = cageCollection.length;
		int row;
		int column;
		int tempTotal;
		boolean allThere;
		int[] next;
		int nextCol;
		int len;
		
//		cage validation
//		iterate through cages
		for (int i = 0; i < cLength; i++) {
			 row = 0;
			 column = 0;
			 tempTotal = 0;
			 allThere = true;
			 next = cageCollection[i].val;
			 nextCol = cageCollection[i].total;
			 len = cageCollection[i].val.length;
			 
//			 iterate through coordinates
			for (int j = 0; j < len; j = j + 2) {
				row = next[j];
				column = next[j + 1];
				
//				checks if column is filled
				if (grid[row][column] != -1) {
					tempTotal += grid[row][column];
					
//					checks if the tempTotal is too high
					if (tempTotal > nextCol) {
						return false;
					}
					allThere = true;
				} else {
					if (tempTotal >= nextCol) {
						return false;
					}
					allThere = false;
					break;
				}
			}
			
//			checks if the total is what is desired
			if (tempTotal != nextCol && allThere) {
				return false;
			}

		}
		// validate rows
		Queue<Integer> validate = new LinkedList<>();
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				
				//check if number is present, if it is its invalud
				if (validate.contains(grid[i][j])) {
					return false;
				} else {
					if (grid[i][j] != -1) {
						
//						if not add it in
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
				
				//check if number is present
				if (validate.contains(grid[j][i])) {
					return false;
				} else {
					if (grid[j][i] != -1) {
						
//						if not add it in
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
//		iterates over grid getting min and max value of all boxes
		for (int i = 0; i < sub; i++) {
			for (int j = 0; j < sub; j++) {
				if (b < dimension && a < dimension) {
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

	@Override
	public int getDimension() {
		return dimension;
	}

	@Override
	public int[] getValidInput() {
		return validInput;
	}

	public boolean checkSubBox(int[][] arr, int minI, int minJ, int maxI, int maxJ) {
		Queue<Integer> validate = new LinkedList<>();
		
//		iterates over box
		for (int i = minI; i < maxI; i++) {
			for (int j = minJ; j < maxJ; j++) {
				
//				checks if value is already present
				if (validate.contains(grid[j][i])) {
					return false;
				} else {
					if (grid[j][i] != -1) {
						
//						if value isnt present, add it to the queue
						validate.add(grid[j][i]);
					}
				}
			}
		}
		validate.clear();
		return true;
	}

	@Override
	public boolean validate(int x, int z, NodeCo nodeChosen) {
		int row = 0;
		int column = 0;
		int tempTotal = 0;
		boolean allThere = true;
		int[] next;
		int nextCol;
		int len;

		
		Stack<Integer> stack = new Stack<Integer>();
//		gets starting points
		int r = x - x % sub;
		int c = z - z % sub;
		for (int i = r; i < r + sub; i++) {
			for (int j = c; j < c + sub; j++) {
				if (grid[i][j] != -1) {
					
					//check if number is present
					if (stack.search(grid[i][j]) == -1) {
						
//						if not its added in
						stack.push(grid[i][j]);
					} else {
						return false;
					}
				}

			}
		}
		stack.clear();
		for (int j = 0; j < dimension; j++) {
			if (grid[x][j] != -1) {
				//check if number is present
				if (stack.search(grid[x][j]) == -1) {
//					if not its added in
					stack.push(grid[x][j]);
				} else {
					return false;
				}
			}
		}
		stack.clear();

		for (int j = 0; j < dimension; j++) {
			if (grid[j][z] != -1) {
				if (stack.search(grid[j][z]) == -1) {
					stack.push(grid[j][z]);
				} else {
					return false;
				}
			}
		}
		stack.clear();

		//iterates through cage
			row = 0;
			column = 0;
			tempTotal = 0;
			next = nodeChosen.val;
			nextCol = nodeChosen.total;
			len = nodeChosen.val.length;
			
//			iterates through values
			for (int j = 0; j < len; j = j + 2) {
				row = next[j];
				column = next[j + 1];
				
//				checks if value is there
				if (grid[row][column] != -1) {
					
//					adds to temp total
					tempTotal += grid[row][column];
					
//					checks if it exceeds the required value
					if (tempTotal > nextCol) {
						return false;
					}
				} else {
					
//					checks if it exceeds the required value
					if (tempTotal > nextCol) {
						return false;
					}
					return true;
				}
			}
			
//			if there incorrect toal and all values are present return false
			if (tempTotal != nextCol && allThere) {
				return false;
			}

		return true;
	} // end of validate()

	@Override
	public boolean validatePartial() {
		// TODO Auto-generated method stub
		int cLength = cageCollection.length;
		int row;
		int column;
		int tempTotal;
		boolean allThere;
		int[] next;
		int nextCol;
		int len;
		
//		cage validation
//		iterate through cages
		for (int i = 0; i < cLength; i++) {
			 row = 0;
			 column = 0;
			 tempTotal = 0;
			 allThere = true;
			 next = cageCollection[i].val;
			 nextCol = cageCollection[i].total;
			 len = cageCollection[i].val.length;
			 
//			 iterate through coordinates
			for (int j = 0; j < len; j = j + 2) {
				row = next[j];
				column = next[j + 1];
				
//				checks if column is filled
				if (grid[row][column] != -1) {
					tempTotal += grid[row][column];
					
//					checks if the tempTotal is too high
					if (tempTotal > nextCol) {
						return false;
					}
					allThere = true;
				} else {
					if (tempTotal >= nextCol) {
						return false;
					}
					allThere = false;
					break;
				}
			}
			
//			checks if the total is what is desired
			if (tempTotal != nextCol && allThere) {
				return false;
			}

		}
		// validate rows
		Queue<Integer> validate = new LinkedList<>();
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				
				//check if number is present, if it is its invalud
				if (validate.contains(grid[i][j])) {
					return false;
				} else {
					if (grid[i][j] != -1) {
						
//						if not add it in
						validate.add(grid[i][j]);
					}
				}
			}
			validate.clear();
		}

		// validate columns
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				
				//check if number is present
				if (validate.contains(grid[j][i])) {
					return false;
				} else {
					if (grid[j][i] != -1) {
						
//						if not add it in
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
//		iterates over grid getting min and max value of all boxes
		for (int i = 0; i < sub; i++) {
			for (int j = 0; j < sub; j++) {
				if (b < dimension && a < dimension) {
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

		return true;	}

} // end of class KillerSudokuGrid
