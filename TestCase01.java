// Sean Szumlanski
// COP 3503, Fall 2020

// =========================
// Pathogen: TestCase01.java
// =========================
// A brief test case to help ensure you've implemented the difficultyRating() method correctly.


import java.io.*;
import java.util.*;

public class TestCase01
{
	public static void main(String [] args)
	{
		double difficulty = Pathogen.difficultyRating();

		if (difficulty < 1.0 || difficulty > 5.0)
			System.out.println("fail whale :(");
		else
			System.out.println("Hooray!");
	}
}
