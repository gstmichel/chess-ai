package org.gstmichel.chess.ai.v6;

import java.util.HashMap;
import java.util.Map;

import com.chess.Board;
import org.gstmichel.chess.ai.ChessMinator;
import com.chess.piece.validator.PieceValidator;
import com.chess.util.BoardUtil;

public class Search {
	private int nbThread = 0;
	public Map<Board, Integer> searchResult = new HashMap<Board, Integer>();
	
	public synchronized void addThread(Board board, ChessMinator ai, int maxDeepness){
		Thread t = new Thread(new SearchThread(board, ai, this, maxDeepness));
		
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
	
	int MAX_DEEPNESS;
	
	ChessMinator ai;
	Board board;
	Search result;
	
	public SearchThread(Board board, ChessMinator ai, Search result, int maxDeepness) {
		MAX_DEEPNESS = maxDeepness;
		
		this.board = board;
		this.result = result;
		this.ai = ai;
		result.increaseThreadNb();
	}
	
	public void run(){
		int subBoardValue = Integer.valueOf(deepSearch(board, ai.color == ai.manager.board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE, this.MAX_DEEPNESS));
		//ai.manager.board.setBetterChild(board);
		
		//Move m = new Move(ai.manager.board, board);
		//System.out.println(ai.color + " - Found : " + PositionUtil.translatePosition(m.getPosA()) + " to " + PositionUtil.translatePosition(m.getPosB()) + " = " + subBoardValue + "\t\t" + ai.manager.board.getChildSequence());
		
		synchronized (result.searchResult) {
			result.searchResult.put(board, subBoardValue);
		}
		result.decreaseThreadNb();
	}
	
	protected int deepSearch(Board board, int parent, int deepness){
		ai.nbNode ++;
		
		if(deepness == 1) {
			ai.nbSearch ++;
			return ai.evaluator.evaluate(ai, board); 
		}

		int limit = ai.color == board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		
		boolean haveChild = false;
		
		for(int positionA : BoardUtil.getPositionOnBoard(board.getCurrent())){
			for(Board subBoard : BoardUtil.getPieceAt(board, positionA).getMove(board, positionA)){
				haveChild = true;
				int current = 0;
				//ai.nbSearch ++;
				
				//Move m = new Move(board, subBoard);
				
				if(board.turn == ai.color && 
					Long.bitCount(board.QUEEN & board.getCurrent()) != Long.bitCount(subBoard.QUEEN & subBoard.getOpponent())){
					//Si le joueurs courant s'apr&egrave;te &agrave; perdre sa reine, on v&eacute;rifie 2 pli supl&eacute;mentaire
					current = deepSearch(subBoard, limit, deepness + 1);
				//} else if (deepness == 2 && PieceValidator.isCheck(subBoard)){
					//Si le joueur est en &eacute;chec, on v&eacute;rifie 1 pli supl&eacute;mentaire
				//	current = deepSearch(subBoard, limit, deepness);
				//} else if(deepness == 2 && Long.bitCount(board.getOpponent() & PositionUtil.POSITIONS[m.getPosB()]) == 1){
					//si le joueur adverse perd une pi&egrave;ce et qu'on est &agrave; une feuille
					//, on v&eacute;rifie 1 pli supl&eacute;mentaire
				//	current = deepSearch(subBoard, limit, deepness);
				} else {
					current = deepSearch(subBoard, limit, deepness - 1);
				}

				if(ai.color == board.turn){
					if(current > limit){
						limit = current;
					} 
					
					if(current == limit){
						if(limit >= parent){
							return limit;
						}
					}
				} else {
					if(current < limit){
						limit = current;
					}
					
					if(current == limit){
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