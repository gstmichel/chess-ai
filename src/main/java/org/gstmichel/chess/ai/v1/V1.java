package org.gstmichel.chess.ai.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.Board;
import com.chess.Move;
import org.gstmichel.chess.ai.ChessMinator;
import com.chess.piece.Color;
import com.chess.rules.BoardManager;
import com.chess.util.BoardUtil;

public class V1 extends ChessMinator {
	
	public V1(BoardManager manager, Color color){
		super(manager, color, new BetaEvaluator());
	}
	
	protected List<Move> getListMove(){
		this.nbSearch = 0;
		
		return getListMove(manager.board, color, 0);
	}
	
	Map<String, Board> mapMove = new HashMap<String, Board>();
	public static List<Move> empty = new ArrayList<Move>();
	
	protected List<Move> getListMove(Board board, Color color, int deepness){
		//if(deepness == 1) System.out.println(System.currentTimeMillis());
		//if(deepness == 2) System.out.println("\t"+System.currentTimeMillis());
		//if(deepness == 3) System.out.println("\t\t"+System.currentTimeMillis());
		if(deepness == 5) return V1.empty;
		
		String str = String.valueOf(board.hashCode());
		synchronized (mapMove) {
			if(mapMove.get(str) != null){
				return new ArrayList<Move>();
			}
			mapMove.put(str, board);
		}
		
		List<Move> lstOption = new ArrayList<Move>();
		
		for(int positionA : BoardUtil.getPositionOnBoard(board.getCurrent())){
			for(Board b : BoardUtil.getPieceAt(board, positionA).getMove(board, positionA)){
				lstOption.add(new Move(board, b));
			}
		}
		
		/*for(Move move : lstOption){
			Board clone = board.clone();
			BoardUtil.getPieceAt(clone, move.getPosA()).moveNoCheck(clone, move.getPosA(), move.getPosB());
			
			move.getMove().addAll(getListMove(clone, color == Color.WHITE ? Color.BLACK : Color.WHITE, deepness + 1));
		}*/
		
		return lstOption;
	}
	
	public static void main(String [] args){
		V1 v = new V1(new BoardManager(new Board()), Color.WHITE);
		
		long start = System.currentTimeMillis();
		List<Move> lst = v.getListMove(v.manager.board, v.color, 0);
		long end = System.currentTimeMillis();
		
		int total = lst.size();
		for(Move current : lst){
			total += current.getMoveQte();
		}
		
		System.out.println("Qte of readed move : " + total);
		System.out.println("Read all that in : " + (end - start) + " mili-seconds.");
	}
}