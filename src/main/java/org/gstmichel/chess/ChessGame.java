package org.gstmichel.chess;

import org.gstmichel.chess.ai.v5.V5;
import org.gstmichel.chess.piece.Color;
import org.gstmichel.chess.rules.BoardManager;
import org.gstmichel.chess.ai.v6.Evaluator;

public class ChessGame {
	public static void main(String [] args){
		Board board = new Board();
		BoardManager manager = new BoardManager(board);

		Thread t2 = new Thread(new V5(manager, Color.WHITE, new Evaluator()));
		Thread t = new Thread(new V5(manager, Color.BLACK, new Evaluator()));
		
		t.start();
		t2.start();
	}
}