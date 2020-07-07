/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.List;

import grid.SudokuGrid;

/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver {
	// TODO: Add attributes as needed.
	int dimension;
	public List<Node> result;
	int sol[][];
	private ColNode header;
	List<Node> answer = new ArrayList<>();
	List<ColNode> cNodes = new ArrayList<>();
	int[][] grid;
	int[] input;

	public DancingLinksSolver() {
		
	} 
	private ColNode createDLXGrid() {
		AlgorXSolver a = new AlgorXSolver();
		a.setValues(dimension);

		// retrieve cover matrix
		grid = a.convertInCoverMatrix(grid);


		// create root node
		ColNode hNode = new ColNode(-1);

		// collection that stores the columns
		List<ColNode> cNodes = new ArrayList<>();

		for (int i = 0; i < dimension * dimension * 4; i++) {
			ColNode n = new ColNode(i);
			cNodes.add(n);
			hNode = (ColNode) hNode.addRight(n);
		}

		hNode = hNode.right.col;

		// iterate through each row
		for (int i = 0; i < dimension * dimension * dimension; i++) {

			// set prev
			Node prev = null;

			// iterate through each column
			for (int j = 0; j < dimension * dimension * 4; j++) {
				// each time a one is found,
				if (grid[i][j] == 1) {
					// get the column for that 1
					ColNode c = cNodes.get(j);

					// initialise a new node for that column
					Node newNode = new Node(c);

					if (prev == null)
						prev = newNode;

					// link the new Node to the column
					c.top.addDown(newNode);

//					link the new node to the right of the previous node
					prev = prev.addRight(newNode);

					// increase column size
					c.size++;
				}
			}
		}

		hNode.size = dimension * dimension * 4;

		return hNode;
	}

	private ColNode getBestColumn() {
		// initialise a min size, starting column and one to iterate over.
		int min = -1;
		ColNode start = null;
		ColNode c = (ColNode) header.right;

		// loop through the column nodes and find the one with lowest size
		while (c != header) {
			if (c.size < min || min == -1) {
				min = c.size;
				start = c;
			}
			c = (ColNode) c.right;
		}
		return start;
	}

	private boolean search() {
//		all columns have been covered
		if (header.right == header) {
			sol = covertAnswerToBoard(answer);
			return true;
		} else {

			// start with the best column then cover it
			ColNode c = getBestColumn();
			c.cover();

			// We add node line to partial solution
			Node currNode = c.bottom;
			while (currNode != c) {
				answer.add(currNode);

				// We cover columns
				Node columnToCover = currNode.right;
				while (columnToCover != currNode) {
					columnToCover.col.cover();
					columnToCover = columnToCover.right;
				}

				// recursive call to level k + 1
				if (search()) {
					return true;
				}

				// backtrack, remove previous node
				currNode = answer.remove(answer.size() - 1);
				c = currNode.col;

				// We uncover columns
				columnToCover = currNode.left;
				while (columnToCover != currNode) {
					columnToCover.col.uncover();
				}
				currNode = currNode.bottom;
			}

			c.uncover();
		}
		return false;
	}

	private int[][] covertAnswerToBoard(List<Node> answer) {
		sol = new int[dimension][dimension];
		
		for (Node next : answer) {
			Node currNode = next;
			int min = currNode.col.name;

			for (Node tempNode = next.right; tempNode != next; tempNode = tempNode.right) {
				int val = tempNode.col.name;

				if (val < min) {
					min = val;
					currNode = tempNode;
				}
			}

			//convert column name to int
			int c1 = currNode.col.name;
			int row = c1 / dimension;
			int col = c1 % dimension;
			// and the value
			int num = (currNode.right.col.name % dimension);
			// we affect that on the result grid
			sol[row][col] = input[num];
		}

		return sol;
	}

	@Override
	public boolean solve(SudokuGrid grid) {
		// TODO: your implementation of the dancing links solver for Killer Sudoku.
		this.grid = grid.getGrid();
		dimension = grid.getDimension();
		input = grid.getValidInput();
		header = createDLXGrid();
		search();
		grid.setGrid(sol);

		// placeholder
		return true;
	} // end of solve()z

} // end of class DancingLinksSolver

class Node {
	public Node left, right, top, bottom;
	public ColNode col;

	public Node() {
//		initialises values of the node
		left = right = top = bottom = this;
	}

	public Node(ColNode c) {
//		assigns a label(int) for the node
		this();
		col = c;
	}

	public Node addDown(Node n) {
//		adds to the bottom of the node
		n.bottom = bottom;
		n.bottom.top = n;
		n.top = this;
		bottom = n;
		return n;
	}

	public Node addRight(Node n) {
//		add node to the righ
		n.right = right;
		n.right.left = n;
		n.left = this;
		right = n;
		return n;
	}
//	delete left and right of node
	public void delLeftRight() {
		left.right = right;
		right.left = left;
	}
	
//	add ledt and right of node
	public void addLeftRight() {
		left.right = this;
		right.left = this;
	}
	
//	delete top and bottom of node
	public void delTopBottom() {
		top.bottom = bottom;
		bottom.top = top;
	}
	
//	add top and bottom of node
	public void addTopBottom() {
		top.bottom = this;
		bottom.top = this;
	}
}

//class for column nodes
class ColNode extends Node {

	public int size;
	public int name;

	public ColNode(int n) {
		super();
		size = 0;
		name = n;
		col = this;
	}

	public void cover() {
		//remove the left and right nodes
		delLeftRight();
		
//		iterate over each row in the column and cover
		for (Node i = bottom; i != this; i = i.bottom) {
			for (Node j = i.right; j != i; j = j.right) {
				j.delTopBottom();
				j.col.size--;
			}
		}
	}

	public void uncover() {
//		start from the top then keep readding top and bottom nodes
		for (Node i = top; i != this; i = i.top) {
			for (Node j = i.left; j != i; j = j.left) {
				j.col.size++;
				j.addTopBottom();
			}
		}
		//readd left and right nodes
		addLeftRight();
	}
}