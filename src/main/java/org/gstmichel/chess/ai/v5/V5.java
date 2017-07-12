package org.gstmichel.chess.ai.v5;

import java.util.ArrayList;
import java.util.List;

import org.gstmichel.chess.Board;
import org.gstmichel.chess.Move;
import org.gstmichel.chess.ai.ChessMinator;
import org.gstmichel.chess.ai.evaluator.BoardEvaluator;
import org.gstmichel.chess.ai.search.SearchResultV2;
import org.gstmichel.chess.piece.Color;
import org.gstmichel.chess.rules.BoardManager;
import org.gstmichel.chess.util.BoardUtil;

public class V5 extends ChessMinator {
	
	SearchResultV2 result = new SearchResultV2();
	
	public V5(BoardManager manager, Color color, BoardEvaluator evaluator){
		super(manager, color, evaluator);
	}
	
	protected List<Move> getListMove(){
		this.nbSearch = 0;
		result.searchResult.clear();
		
		List<Move> option = new ArrayList<Move>();
		
		int deepness = 5;
		BoardUtil.showBoard(manager.board);
		
		System.out.println(manager.board.turn + " - Will search on " + (deepness+1) + " level.");
		
		for(int positionA : BoardUtil.getPositionOnBoard(manager.board.getCurrent())){
			for(Board subBoard : BoardUtil.getPieceAt(manager.board, positionA).getMove(manager.board, positionA)){
				//nbSearch ++;
				result.addThread(subBoard, this, deepness);
			}
		}
		
		synchronized (result) {
			while(result.getNbThread() != 0){
				//System.out.println(result.getNbThread() + " : " + nbSearch);
				try { result.wait(); } catch (InterruptedException e) {}
			}
			
			int limit = Integer.MIN_VALUE;
			
			for(Board subBoard : result.searchResult.keySet()){
				int childVal = result.searchResult.get(subBoard);
				
				//Move m = new Move(manager.board, subBoard);
				//System.out.println("found : " + PositionUtil.translatePosition(m.getPosA()) + " to " + PositionUtil.translatePosition(m.getPosB()) + " = " + childVal);
				
				if(childVal > limit) {
					limit = childVal;
					option.clear();
					option.add(new Move(manager.board, subBoard));
				}
				
				if(limit == childVal){
					option.add(new Move(manager.board, subBoard));
				}
			}
			
			System.out.println(this.color + " - Satisfaction Level : " + limit);
		}
		
		return option;
	}		
}