package org.gstmichel.chess.ai.v3;

import java.util.ArrayList;
import java.util.List;

import com.chess.Board;
import com.chess.Move;
import org.gstmichel.chess.ai.ChessMinator;
import org.gstmichel.chess.ai.evaluator.BoardEvaluator;
import com.chess.piece.Color;
import com.chess.piece.validator.PieceValidator;
import com.chess.rules.BoardManager;
import com.chess.util.BoardUtil;
import com.chess.util.PositionUtil;

public class V3 extends ChessMinator {
	
	int maxDeepness = 5;
	
	public V3(BoardManager manager, Color color, BoardEvaluator evaluator){
		super(manager, color, evaluator);
	}
	
	protected List<Move> getListMove(){
		this.nbSearch = 0;
		
		return deepSearch();
	}
	
	protected List<Move> deepSearch(){
		int limit = this.color == manager.board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		List<Move> option = new ArrayList<Move>();

		for(int positionA : BoardUtil.getPositionOnBoard(manager.board.getCurrent())){
			for(Board subBoard : BoardUtil.getPieceAt(manager.board, positionA).getMove(manager.board, positionA)){
				nbSearch ++;
				Move m = new Move(manager.board, subBoard);
				//log(PositionUtil.translatePosition(m.getPosA()) + "=>" + PositionUtil.translatePosition(m.getPosB()));
				
				if("E6=>H3".equals(PositionUtil.translatePosition(m.getPosA()) + "=>" + PositionUtil.translatePosition(m.getPosB()))){
					//this.log = true;
				}
				
				int childVal = deepSearch(subBoard, limit, maxDeepness);
				log(PositionUtil.translatePosition(m.getPosA()) + "=>" + PositionUtil.translatePosition(m.getPosB()) + " : " + childVal);
				
				if("E6=>H3".equals(PositionUtil.translatePosition(m.getPosA()) + "=>" + PositionUtil.translatePosition(m.getPosB()))){
					//this.log = false;
				}
				
				//System.out.println("found : " + PositionUtil.translatePosition(m.getPosA()) + " to " + PositionUtil.translatePosition(m.getPosB()) + " = " + childVal);
				if(childVal > limit) {
					limit = childVal;
					option.clear();
				}
				
				if(limit == childVal)
					option.add(m);
			}
		}
		
		System.out.println(this.color + " - Satisfaction Level : " + limit);
		
		return option;
	}
	
	protected int deepSearch(Board board, int parent, int deepness){
		String tab = "";
		for(int i = maxDeepness - deepness + 1; i != 0; i--){
			tab += "\t";
		}
		
		if(deepness == 1) { return evaluator.evaluate(this, board); }

		int limit = this.color == board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		
		//if(deepness == maxDeepness){
		//	System.out.println("");
		//}
		
		boolean haveChild = false;
		
		for(int positionA : BoardUtil.getPositionOnBoard(board.getCurrent())){
			for(Board subBoard : BoardUtil.getPieceAt(board, positionA).getMove(board, positionA)){
				haveChild = true;
				
				nbSearch ++;
				Move m = new Move(board, subBoard);
				//if(deepness - 1 != 1){
					//BoardUtil.showBoard(subBoard);
				//	log(tab + PositionUtil.translatePosition(m.getPosA()) + "=>" + PositionUtil.translatePosition(m.getPosB()));
					//System.out.println("");
				//}
				int current = deepSearch(subBoard, limit, deepness - 1);
				if(deepness == maxDeepness){
					//BoardUtil.showBoard(subBoard);
					log(tab + PositionUtil.translatePosition(m.getPosA()) + "=>" + PositionUtil.translatePosition(m.getPosB()) + " : " + current);
					//System.out.println("");
				}

				if(this.color != subBoard.turn){
					if(current > limit){
						limit = current;
						//if(limit <= parent){
							//log(tab.substring(1) + limit);
						//	return limit;
						//}
					}
				} else {
					if(current < limit){
						limit = current;
						//if(limit >= parent){
							//log(tab.substring(1) + limit);
						//	return limit;
						//}
					}
				}
			}
		}
		
		if(!haveChild){
			if(PieceValidator.isCheck(board)){
				return this.color == board.turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
			} else {
				return 0;
			}
		}
		
		//log(tab.substring(1) + limit);
		return limit;
	}
}