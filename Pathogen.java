// Cannen Carpenter, NID: ca768181
// COP 3503, Fall 2020
// Atom + Linux Bash Shell on Windows 10

import java.io.*;
import java.util.*;

public class Pathogen
{
	// Used to toggle "animated" output on and off (for debugging purposes).
	private static boolean animationEnabled = false;

	// "Animation" frame rate (frames per second).
	private static double frameRate = 100;

	// Setters. Note that for testing purposes you can call enableAnimation()
	// from your backtracking method's wrapper method (i.e., the first line of
	// your public findPaths() method) if you want to override the fact that the
	// test cases are disabling animation. Just don't forget to remove that
	// method call before submitting!
	public static void enableAnimation() { Pathogen.animationEnabled = true; }
	public static void disableAnimation() { Pathogen.animationEnabled = false; }
	public static void setFrameRate(double fps) { Pathogen.frameRate = fps; }

	// Maze constants.
	private static final int CLOW = 0;
	private static final int RLOW = 0;
	private static final int CHIGH = 0;
	private static final int RHIGH = 0;
	private static final char WALL       = '#';
	private static final char PERSON     = '@';
	private static final char EXIT       = 'e';
	private static final char COVID       = '*';
	private static final char BREADCRUMB = '.';  // visited
	private static final char SPACE      = ' ';  // unvisited

	// Takes a 2D char maze and returns true if it can find a path from the
	// starting position to the exit. Assumes the maze is well-formed according
	// to the restrictions above.
	public static HashSet<String> findPaths(char [][] maze)
	{

		HashSet<String> paths = new HashSet<>();
		int height = maze.length;
		int width = maze[0].length;
		String path = "";

		disableAnimation();

		// The visited array keeps track of visited positions. It also keeps
		// track of the exit, since the exit will be overwritten when the '@'
		// symbol covers it up in the maze[][] variable. Each cell contains one
		// of three values:
		//
		//   '.' -- visited
		//   ' ' -- unvisited
		//   'e' -- exit
		char [][] visited = new char[height][width];
		for (int i = 0; i < height; i++)
			Arrays.fill(visited[i], SPACE);

		// Find starting position (location of the '@' character).
		int startRow = -1;
		int startCol = -1;

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (maze[i][j] == PERSON)
				{
					startRow = i;
					startCol = j;
				}
			}
		}

		// Let's goooooooo!!
		findPaths(maze, visited, startRow, startCol, height, width, path, paths);
		//System.out.println(paths);
		return paths;
	}

	private static boolean findPaths(char [][] maze, char [][] visited,
	                                 int currentRow, int currentCol,
	                                 int height, int width, String path, HashSet<String> paths)
	{
		StringBuilder tempP = new StringBuilder(path);
		// Holds past path string
		StringBuilder builder = tempP;
		int tempEC = 0, tempER = 0;

		// This conditional block prints the maze when a new move is made.
		if (Pathogen.animationEnabled)
		{
			printAndWait(maze, height, width, "Searching...", Pathogen.frameRate);
		}

		// Hooray!
		if (visited[currentRow][currentCol] == 'e')
		{
			// Deletes space at the end of path
			builder.delete(builder.length() - 1, builder.length());
			path = builder.toString();

			// Adds path to HashSet
			paths.add(path);
			if (Pathogen.animationEnabled)
			{
				char [] widgets = {'|', '/', '-', '\\', '|', '/', '-', '\\',
				                   '|', '/', '-', '\\', '|', '/', '-', '\\', '|'};

				for (int i = 0; i < widgets.length; i++)
				{
					maze[currentRow][currentCol] = widgets[i];
					printAndWait(maze, height, width, "Hooray!", 12.0);
				}

				maze[currentRow][currentCol] = EXIT;
				printAndWait(maze, height, width, "Hooray!", Pathogen.frameRate);
			}
			// Readies exit again
			visited[tempER][tempEC] = 'e';
			return false;
		}

		// Moves: left, right, up, down
		int [][] moves = new int[][] {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

		for (int i = 0; i < moves.length; i++)
		{
			int newRow = currentRow + moves[i][0];
			int newCol = currentCol + moves[i][1];
			// Check move is in bounds, not a wall, and not marked as visited.
			if (!isLegalMove(maze, visited, newRow, newCol, height, width))
				continue;

			// Change state. Before moving the person forward in the maze, we
			// need to check whether we're overwriting the exit. If so, save the
			// exit in the visited[][] array so we can actually detect that
			// we've gotten there.

			if (maze[newRow][newCol] == EXIT)
				visited[newRow][newCol] = EXIT;

			// Marks path being taken, along with moving the 'character'
			maze[currentRow][currentCol] = BREADCRUMB;
			visited[currentRow][currentCol] = BREADCRUMB;
			maze[newRow][newCol] = PERSON;

			// Adds directional input, according to the move, to temp string builder
			if(moves[i][0] == 0 && moves[i][1] == -1)
			{
				tempP.append("l ");
			}
			else if(moves[i][0] == 0 && moves[i][1] == 1)
			{
				tempP.append("r ");
			}
			else if(moves[i][0] == -1 && moves[i][1] == 0)
			{
				tempP.append("u ");
			}
			else if(moves[i][0] == 1 && moves[i][1] == 0)
			{
				tempP.append("d ");
			}

			// Perform recursive descent.
			if (findPaths(maze, visited, newRow, newCol, height, width, builder.toString(), paths))
				return true;

			// Undo state change. Note that if we return from the previous call,
			// we know visited[newRow][newCol] did not contain the exit, and
			// therefore already contains a breadcrumb, so I haven't updated
			// that here.

			// Removes visited path when backtracking, except for the exit marker
			if(visited[newRow][newCol] != 'e')
			{
				maze[newRow][newCol] = ' ';
				visited[newRow][newCol] = ' ';
				maze[currentRow][currentCol] = PERSON;
			}
			// Removes the first step into the invalid path
			tempP.delete(tempP.length() - 2, tempP.length());

			// This conditional block prints the maze when a move gets undone
			// (which is effectively another kind of move).
			if (Pathogen.animationEnabled)
			{
				printAndWait(maze, height, width, "Backtracking...", frameRate);
			}
		}

		return false;
	}

	// Returns true if moving to row and col is legal (i.e., we have not visited
	// that position before, and it's not a wall, nor outside the maze).
	private static boolean isLegalMove(char [][] maze, char [][] visited,
	                                   int row, int col, int height, int width)
	{
		if (row == 0 || row == height || col <= 0 || col == width || maze[row][col] == WALL || visited[row][col] == BREADCRUMB || maze[row][col] == COVID)
			return false;

		return true;
	}

	// This effectively pauses the program for waitTimeInSeconds seconds.
	private static void wait(double waitTimeInSeconds)
	{
		long startTime = System.nanoTime();
		long endTime = startTime + (long)(waitTimeInSeconds * 1e9);

		while (System.nanoTime() < endTime)
			;
	}

	// Prints maze and waits. frameRate is given in frames per second.
	private static void printAndWait(char [][] maze, int height, int width,
	                                 String header, double frameRate)
	{
		if (header != null && !header.equals(""))
			System.out.println(header);

		if (height < 1 || width < 1)
			return;

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				System.out.print(maze[i][j]);
			}

			System.out.println();
		}

		System.out.println();
		wait(1.0 / frameRate);
	}

	// Read maze from file. This function dangerously assumes the input file
	// exists and is well formatted according to the specification above.
	private static char [][] readMaze(String filename) throws IOException
	{
		Scanner in = new Scanner(new File(filename));

		int height = in.nextInt();
		int width = in.nextInt();

		char [][] maze = new char[height][];

		// After reading the integers, there's still a new line character we
		// need to do away with before we can continue.

		in.nextLine();

		for (int i = 0; i < height; i++)
		{
			// Explode out each line from the input file into a char array.
			maze[i] = in.nextLine().toCharArray();
		}

		return maze;
	}

	public static double difficultyRating()
	{
		return 4;
	}

	public static double hoursSpent()
	{
		return 15;
	}

}
