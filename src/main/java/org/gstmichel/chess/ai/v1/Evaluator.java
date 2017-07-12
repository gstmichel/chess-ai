package org.gstmichel.chess.ai.v1;

import com.chess.Board;
import org.gstmichel.chess.ai.evaluator.BoardEvaluator;

public class Evaluator extends BoardEvaluator {
	
	protected int evaluate(Board board, boolean current){
		long color = current ? board.getCurrent() : board.getOpponent();
		
		int value = 0;
		int piece = 0;
		
		piece = Long.bitCount(color & board.PAWN);
		value += piece;
		
		piece = Long.bitCount(color & board.KNIGHT);
		value += piece * 5;
		
		piece = Long.bitCount(color & board.BISHOP);
		value += piece * 10;
		
		piece = Long.bitCount(color & board.ROOK);
		value += piece * 15;
		
		piece = Long.bitCount(color & board.QUEEN);
		value += piece * 25;
		
		return value;
	}
}
