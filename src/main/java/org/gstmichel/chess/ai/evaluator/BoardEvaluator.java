package org.gstmichel.chess.ai.evaluator;

import org.gstmichel.chess.Board;
import org.gstmichel.chess.ai.ChessMinator;

public abstract class BoardEvaluator {

	public int evaluate(ChessMinator cm, Board board){
		//Take opponent as current and current as opponent because the turn has been switch in the moveNoCheck
		//System.out.println(board.turn);
		int value = evaluate(board, false);
		value -= evaluate(board, true);
		
		return value;
	}
	
	abstract protected int evaluate(Board board, boolean current);
}
