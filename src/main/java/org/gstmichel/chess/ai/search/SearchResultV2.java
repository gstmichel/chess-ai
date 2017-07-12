package org.gstmichel.chess.ai.search;

import java.util.HashMap;
import java.util.Map;

import org.gstmichel.chess.Board;
import org.gstmichel.chess.ai.ChessMinator;
import org.gstmichel.chess.piece.validator.PieceValidator;
import org.gstmichel.chess.util.BoardUtil;

public class SearchResultV2 {
	private int nbThread = 0;
	public Map<Board, Integer> searchResult = new HashMap<Board, Integer>();
	
	public synchronized void addThread(Board board, ChessMinator ai, int maxDeepness){
		Thread t = new Thread(new SearchThreadV2(board, ai, this, maxDeepness));
		
		t.start();
	}
	
	public int getNbThread(){
		return nbThread;
	}
	
	public synchronized void increaseThreadNb(){
		this.nbThread ++;
	}
	
	public synchronized void decreaseThreadNb(){
		this.nbThread --;
		notifyAll();
	}
}

class SearchThreadV2 implements Runnable{
	
	int MAX_DEEPNESS;
	
	ChessMinator ai;
	Board board;
	SearchResultV2 result;
	
	public SearchThreadV2(Board board, ChessMinator ai, SearchResultV2 result, int maxDeepness) {
		MAX_DEEPNESS = maxDeepness;
		
		this.board = board;
		this.result = result;
		this.ai = ai;
		result.increaseThreadNb();
	}
	
	public void run(){
		int subBoardValue = Integer.valueOf(deepSearch(board, ai.color == ai.manager.board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE, this.MAX_DEEPNESS));
		
		//Move m = new Move(ai.manager.board, board);
		//System.out.println("found : " + PositionUtil.translatePosition(m.getPosA()) + " to " + PositionUtil.translatePosition(m.getPosB()) + " = " + subBoardValue);
		
		synchronized (result.searchResult) {
			result.searchResult.put(board, subBoardValue);
		}
		result.decreaseThreadNb();
	}
	
	protected int deepSearch(Board board, int parent, int deepness){
		if(deepness == 1) { 
			ai.nbSearch ++;
			return ai.evaluator.evaluate(ai, board); 
		}

		int limit = ai.color == board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		
		boolean haveChild = false;
		
		for(int positionA : BoardUtil.getPositionOnBoard(board.getCurrent())){
			for(Board subBoard : BoardUtil.getPieceAt(board, positionA).getMove(board, positionA)){
				haveChild = true;
				//ai.nbSearch ++;
				
				int current = deepSearch(subBoard, limit, deepness - 1);

				//if(current != 0){
					//System.out.println("here");
				//}
				if(ai.color == board.turn){
					if(current > limit){
						limit = current;
						if(limit >= parent){
							return limit;
						}
					}
				} else {
					if(current < limit){
						limit = current;
						if(limit <= parent){
							return limit;
						}
					}
				}
			}
		}
		
		if(!haveChild){
			if(PieceValidator.isCheck(board)){
				//BoardUtil.showBoard(board);
				//The division allow to simply get the shortest way to MATE , closer to the root, biger is the result
				//(so we don't turn around because we have many posibility of MATE)
				int eliminationSpeed = ai.color == board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
				//System.out.println(MAX_DEEPNESS - (MAX_DEEPNESS % 2));
				//System.out.println(deepness);
				//System.out.println(((MAX_DEEPNESS - deepness) / 2));
				return eliminationSpeed / (MAX_DEEPNESS + 1 - deepness);
				
				//return ai.color == board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
			} else {
				return 0;
			}
		}
		
		return limit;
	}
}