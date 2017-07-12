package org.gstmichel.chess.ai.v6;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.Move;
import org.gstmichel.chess.ai.ChessMinator;
import org.gstmichel.chess.ai.evaluator.BoardEvaluator;
import com.chess.piece.Color;
import com.chess.rules.BoardManager;
import com.chess.util.BoardUtil;

public class V6 extends ChessMinator {
	
	Search result = new Search();
	
	public V6(BoardManager manager, Color color){
		super(manager, color, new Evaluator());
	}
	
	public V6(BoardManager manager, Color color, BoardEvaluator evaluator){
		super(manager, color, evaluator);
	}
	
	protected List<Move> getListMove(){
		this.nbSearch = 0;
		result.searchResult.clear();
		
		List<Move> option = new ArrayList<Move>();
		
		int deepness = 5;
		//int pieceNb = Long.bitCount(manager.board.QUEEN | manager.board.KNIGHT);
		//if(pieceNb == 0){
		//	deepness ++;
		//}
		
		//pieceNb = Long.bitCount(manager.board.ROOK | manager.board.BISHOP);
		//if(pieceNb < 5){
		//	deepness ++;
		//}
		
		BoardUtil.showBoard(manager.board);
		System.out.println(manager.board.turn + " - Will search on " + (deepness+1) + " level.  Current evaluation : " + evaluator.evaluate(this, manager.board));
		
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
				
				if(childVal > limit) {
					limit = childVal;
					option.clear();
				}
				
				if(limit == childVal){
					option.add(new Move(manager.board, subBoard));
				}
			}
			
			//manager.board.printBestChildEvaluation(this, "");
			
			System.out.println(this.color + " - Satisfaction evaluation : " + limit);
		}
		
		return option;
	}		
}