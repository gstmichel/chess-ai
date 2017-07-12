package org.gstmichel.chess.ai.search;

import java.util.HashMap;
import java.util.Map;

import com.chess.Board;
import org.gstmichel.chess.ai.ChessMinator;
import com.chess.piece.validator.PieceValidator;
import com.chess.util.BoardUtil;

public class SearchResult {
	private int nbThread = 0;
	public Map<Board, Integer> searchResult = new HashMap<Board, Integer>();
	
	public synchronized void addThread(Board board, ChessMinator ai){
		Thread t = new Thread(new SearchThread(board, ai, this));
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

class SearchThread implements Runnable{
	
	static int MAX_DEEPNESS = 5;
	
	ChessMinator ai;
	Board board;
	SearchResult result;
	
	public SearchThread(Board board, ChessMinator ai, SearchResult result) {
		this.board = board;
		this.result = result;
		this.ai = ai;
		result.increaseThreadNb();
	}
	
	public void run(){
		int subBoardValue = Integer.valueOf(deepSearch(board, ai.color == ai.manager.board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE, SearchThread.MAX_DEEPNESS));
		
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

				if(current != 0){
					//System.out.println("here");
				}
				if(ai.color == board.turn){
					if(current > limit){
						limit = current;
					}
				} else {
					if(current < limit){
						limit = current;
					}
				}
			}
		}
		
		if(!haveChild){
			if(PieceValidator.isCheck(board)){
				return ai.color == board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
			} else {
				return 0;
			}
		}
		
		return limit;
	}
}