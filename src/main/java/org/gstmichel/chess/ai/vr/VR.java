package org.gstmichel.chess.ai.vr;

import java.util.ArrayList;
import java.util.List;

import org.gstmichel.chess.Board;
import org.gstmichel.chess.Move;
import org.gstmichel.chess.ai.ChessMinator;
import org.gstmichel.chess.ai.v1.BetaEvaluator;
import org.gstmichel.chess.piece.Color;
import org.gstmichel.chess.rules.BoardManager;
import org.gstmichel.chess.util.BoardUtil;

public class VR extends ChessMinator {
	
	public VR(BoardManager manager, Color color){
		super(manager, color, new BetaEvaluator());
	}
	
	protected List<Move> getListMove(){
		this.nbSearch = 0;
		
		List<Move> lstOption = new ArrayList<Move>();
		
		for(int positionA : BoardUtil.getPositionOnBoard(manager.board.getCurrent())){
			for(Board board2 : BoardUtil.getPieceAt(manager.board, positionA).getMove(manager.board, positionA)){
				lstOption.add(new Move(manager.board, board2));
			}
		}
		
		return lstOption;
	}
}