package org.gstmichel.chess.ai.v4;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.Move;
import org.gstmichel.chess.ai.ChessMinator;
import org.gstmichel.chess.ai.evaluator.BoardEvaluator;
import org.gstmichel.chess.ai.search.SearchResult;
import com.chess.piece.Color;
import com.chess.rules.BoardManager;
import com.chess.util.BoardUtil;

public class V4 extends ChessMinator {
	
	SearchResult result = new SearchResult();
	
	public V4(BoardManager manager, Color color, BoardEvaluator evaluator){
		super(manager, color, evaluator);
	}
	
	protected List<Move> getListMove(){
		this.nbSearch = 0;
		result.searchResult.clear();
		
		List<Move> option = new ArrayList<Move>();
		
		for(int positionA : BoardUtil.getPositionOnBoard(manager.board.getCurrent())){
			for(Board subBoard : BoardUtil.getPieceAt(manager.board, positionA).getMove(manager.board, positionA)){
				//nbSearch ++;
				result.addThread(subBoard, this);
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