// Sean Szumlanski
// COP 3503, Fall 2020

// =========================
// Pathogen: TestCase08.java
// =========================


// =============================================================================
// Maze Input File Format:
// =============================================================================
//
// The first line of the input file must have the height and width of the maze
// as integers, separated by a single space. The following lines should contain
// an ASCII maze whose dimensions are height x width. This program assumes
// there's exactly one person and one exit per maze.
//
// =============================================================================
// Example:
// =============================================================================
//
// 8 13
// #############
// #@# #  *#   #
// #   # # # # #
// # ### # # # #
// #     #   # #
// # ##### #####
// #          e#
// #############
//
// =============================================================================
// Legend:
// =============================================================================
//
// # - wall
// @ - person
// e - exit
// * - coronavirus


import java.io.*;
import java.util.*;

public class TestCase08
{
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

	// Read solution strings (representing paths through the maze) from input
	// file and return HashSet containing all such strings.
	private static HashSet<String> readSolutions(String filename) throws IOException
	{
		Scanner in = new Scanner(new File(filename));
		HashSet<String> solutions = new HashSet<>();

		while (in.hasNextLine())
			solutions.add(in.nextLine());

		return solutions;
	}

	public static void main(String [] args) throws IOException
	{
		// Load maze and solution paths from file.
		char [][] maze = readMaze("input_files/maze08.txt");
		HashSet<String> solutionPaths = readSolutions("input_files/maze08-paths.txt");

		// Call backtracking algorithm to find all paths through maze.
		Pathogen.disableAnimation();
		HashSet<String> yourPaths = Pathogen.findPaths(maze);

		// Check for success. Print set contents if sets differ.
		if (yourPaths.equals(solutionPaths))
		{
			System.out.println("Hooray!");
		}
		else
		{
			System.out.println("Whoops! Those string sets differ!");

			System.out.println();

			System.out.println("======================");
			System.out.println("Expected Path Strings:");
			System.out.println("======================");
			for (String s : solutionPaths)
				System.out.println(s);

			System.out.println();

			System.out.println("======================");
			System.out.println("Returned Path Strings:");
			System.out.println("======================");
			for (String s : yourPaths)
				System.out.println(s);
		}
	}
}
