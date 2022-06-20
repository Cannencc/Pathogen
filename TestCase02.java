// Sean Szumlanski
// COP 3503, Fall 2020

// =========================
// Pathogen: TestCase02.java
// =========================
// A brief test case to help ensure you've implemented the hoursSpent() method correctly.


import java.io.*;
import java.util.*;

public class TestCase02
{
	public static void main(String [] args)
	{
		double hours = Pathogen.hoursSpent();

		if (hours <= 0.0)
			System.out.println("fail whale :(");
		else
			System.out.println("Hooray!");
	}
}
