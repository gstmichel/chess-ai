package org.gstmichel.chess.ai.v4;

import com.chess.Board;
import org.gstmichel.chess.ai.evaluator.BoardEvaluator;

public class Evaluator extends BoardEvaluator{
	
	protected int evaluate(Board board, boolean current){
		long color = current ? board.getCurrent() : board.getOpponent();
		
		int value = 0;
		int piece = 0;
		
		piece = Long.bitCount(color & board.PAWN);
		value += piece * 100;
		
		piece = Long.bitCount(color & board.KNIGHT);
		value += piece * 320;
		
		piece = Long.bitCount(color & board.BISHOP);
		value += piece * 333;
		
		piece = Long.bitCount(color & board.ROOK);
		value += piece * 510;
		
		piece = Long.bitCount(color & board.QUEEN);
		value += piece * 880;
		
		return value;
	}
}
