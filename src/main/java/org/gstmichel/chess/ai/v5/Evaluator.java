package org.gstmichel.chess.ai.v5;

import com.chess.Board;
import org.gstmichel.chess.ai.evaluator.BoardEvaluator;
import com.chess.piece.Color;
import com.chess.util.BoardUtil;

public class Evaluator extends BoardEvaluator{
	
	protected int evaluate(Board board, boolean current){
		long color = current ? board.getCurrent() : board.getOpponent();
		int value = 0;
		
		value += Long.bitCount(color & board.PAWN) * 100;
		value += Long.bitCount(color & board.KNIGHT) * 320;
		value += Long.bitCount(color & board.BISHOP) * 333;
		value += Long.bitCount(color & board.ROOK) * 510;
		value += Long.bitCount(color & board.QUEEN) * 880;
		
		if( 	(board.turn == Color.WHITE && current) ||
				(board.turn == Color.BLACK && !current)){
			//Evaluate the long board with WHITE pattern
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.PAWN)){
				value += WHITE_PAWN[i];
			}
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.ROOK)){
				value += WHITE_ROOK[i];
			}
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.KNIGHT)){
				value += WHITE_KNIGHT[i];
			}
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.BISHOP)){
				value += WHITE_BISHOP[i];
			}
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.QUEEN)){
				value += WHITE_QUEEN[i];
			}
			
			value += WHITE_KING[BoardUtil.getSinglePosition(color & board.KING)];
		} else {
			//Evaluate the long board with BLACK pattern
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.PAWN)){
				value += BLACK_PAWN[i];
			}
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.ROOK)){
				value += BLACK_ROOK[i];
			}
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.KNIGHT)){
				value += BLACK_KNIGHT[i];
			}
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.BISHOP)){
				value += BLACK_BISHOP[i];
			}
			
			for(int i : BoardUtil.getPositionOnBoard(color & board.QUEEN)){
				value += BLACK_QUEEN[i];
			}
			
			value += BLACK_KING[BoardUtil.getSinglePosition(color & board.KING)];
		}
		
		return value;
	}
	
	int [] WHITE_PAWN = new int []{
			 0,  0,  0,  0,  0,  0,  0,  0,
			 5, 10, 10,-20,-20, 10, 10,  5,
			 5, -5,-10,  0,  0,-10, -5,  5,
			 0,  0,  0, 20, 20,  0,  0,  0,
			 5,  5, 10, 25, 25, 10,  5,  5,
			10, 10, 20, 30, 30, 20, 10, 10,
			50, 50, 50, 50, 50, 50, 50, 50,
			 0,  0,  0,  0,  0,  0,  0,  0};
	
	int [] WHITE_KNIGHT = new int []{
			-50,-40,-30,-30,-30,-30,-40,-50,
			-40,-20,  0,  5,  5,  0,-20,-40,
			-30,  5, 10, 15, 15, 10,  5,-30,
			-30,  0, 15, 20, 20, 15,  0,-30,
			-30,  5, 15, 20, 20, 15,  5,-30,
			-30,  0, 10, 15, 15, 10,  0,-30,
			-40,-20,  0,  0,  0,  0,-20,-40,
			-50,-40,-30,-30,-30,-30,-40,-50};
	
	int [] WHITE_BISHOP = new int []{
			-20,-10,-10,-10,-10,-10,-10,-20,
			-10,  5,  0,  0,  0,  0,  5,-10,
			-10, 10, 10, 10, 10, 10, 10,-10,
			-10,  0, 10, 10, 10, 10,  0,-10,
			-10,  5,  5, 10, 10,  5,  5,-10,
			-10,  0,  5, 10, 10,  5,  0,-10,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-20,-10,-10,-10,-10,-10,-10,-20};
	
	int [] WHITE_ROOK = new int []{
			 0,  0,  0,  5,  5,  0,  0,  0,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			 5, 10, 10, 10, 10, 10, 10,  5,
			 0,  0,  0,  0,  0,  0,  0,  0};
	
	int [] WHITE_QUEEN = new int []{
			-20,-10,-10, -5, -5,-10,-10,-20,
			-10,  0,  5,  0,  0,  0,  0,-10,
			-10,  5,  5,  5,  5,  5,  0,-10,
			  0,  0,  5,  5,  5,  5,  0, -5,
			 -5,  0,  5,  5,  5,  5,  0, -5,
			-10,  0,  5,  5,  5,  5,  0,-10,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-20,-10,-10, -5, -5,-10,-10,-20};
	
	int [] WHITE_KING = new int []{
			  20, 30, 10,  0,  0, 10, 30, 20,
			  20, 20,  0,  0,  0,  0, 20, 20,
			 -10,-20,-20,-20,-20,-20,-20,-10,
			 -20,-30,-30,-40,-40,-30,-30,-20,
			 -30,-40,-40,-50,-50,-40,-40,-30,
			 -30,-40,-40,-50,-50,-40,-40,-30,
			 -30,-40,-40,-50,-50,-40,-40,-30,
			 -30,-40,-40,-50,-50,-40,-40,-30};
	
	int [] BLACK_PAWN = new int []{
			0,  0,  0,  0,  0,  0,  0,  0,
			50, 50, 50, 50, 50, 50, 50, 50,
			10, 10, 20, 30, 30, 20, 10, 10,
			 5,  5, 10, 25, 25, 10,  5,  5,
			 0,  0,  0, 20, 20,  0,  0,  0,
			 5, -5,-10,  0,  0,-10, -5,  5,
			 5, 10, 10,-20,-20, 10, 10,  5,
			 0,  0,  0,  0,  0,  0,  0,  0};
	
	int [] BLACK_KNIGHT = new int []{
			-50,-40,-30,-30,-30,-30,-40,-50,
			-40,-20,  0,  0,  0,  0,-20,-40,
			-30,  0, 10, 15, 15, 10,  0,-30,
			-30,  5, 15, 20, 20, 15,  5,-30,
			-30,  0, 15, 20, 20, 15,  0,-30,
			-30,  5, 10, 15, 15, 10,  5,-30,
			-40,-20,  0,  5,  5,  0,-20,-40,
			-50,-40,-30,-30,-30,-30,-40,-5};
	
	int [] BLACK_BISHOP = new int []{
			-20,-10,-10,-10,-10,-10,-10,-20,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-10,  0,  5, 10, 10,  5,  0,-10,
			-10,  5,  5, 10, 10,  5,  5,-10,
			-10,  0, 10, 10, 10, 10,  0,-10,
			-10, 10, 10, 10, 10, 10, 10,-10,
			-10,  5,  0,  0,  0,  0,  5,-10,
			-20,-10,-10,-10,-10,-10,-10,-20};
	
	int [] BLACK_ROOK = new int []{
			0,  0,  0,  0,  0,  0,  0,  0,
			  5, 10, 10, 10, 10, 10, 10,  5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			  0,  0,  0,  5,  5,  0,  0,  0};
	
	int [] BLACK_QUEEN = new int []{
			-20,-10,-10, -5, -5,-10,-10,-20,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-10,  0,  5,  5,  5,  5,  0,-10,
			 -5,  0,  5,  5,  5,  5,  0, -5,
			  0,  0,  5,  5,  5,  5,  0, -5,
			-10,  5,  5,  5,  5,  5,  0,-10,
			-10,  0,  5,  0,  0,  0,  0,-10,
			-20,-10,-10, -5, -5,-10,-10,-20};
	
	int [] BLACK_KING = new int []{
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-20,-30,-30,-40,-40,-30,-30,-20,
			-10,-20,-20,-20,-20,-20,-20,-10,
			 20, 20,  0,  0,  0,  0, 20, 20,
			 20, 30, 10,  0,  0, 10, 30, 20};
}