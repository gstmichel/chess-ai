package org.gstmichel.chess.ai;

import java.util.List;

import org.gstmichel.chess.Move;
import org.gstmichel.chess.ai.evaluator.BoardEvaluator;
import org.gstmichel.chess.piece.Color;
import org.gstmichel.chess.piece.validator.PieceValidator;
import org.gstmichel.chess.rules.BoardManager;
import org.gstmichel.chess.util.BoardUtil;
import org.gstmichel.chess.util.PositionUtil;

public abstract class ChessMinator implements Runnable {

	public Color color;
	public BoardManager manager;
	public BoardEvaluator evaluator;
	
	public int nbNode = 0;
	public int nbSearch = 0;
	
	public long totalTime = 0;
	
	protected ChessMinator(){
		throw new IllegalArgumentException("Can't use the empty constructor");
	}
	
	public ChessMinator(BoardManager manager, Color color, BoardEvaluator evaluator){
		this.color = color;
		this.manager = manager;
		this.evaluator = evaluator;
	}
	
	public void run(){
		String name = this.getClass().getName();
		Thread.currentThread().setName(this.color + " - " + name.substring(name.lastIndexOf(".") + 1));
		
		long start = System.currentTimeMillis();
		try{
			while(true){
				synchronized (manager) {
					while(color != manager.board.turn){
						try { manager.wait(); } catch (InterruptedException e) {}
					}
					
					if(manager.ended){
						long end = System.currentTimeMillis() - start;
						System.out.println("End of the game in " + end + " mili-secondes");
						break;
					}
					
					if(manager.board.nbTurn50 == 100){
						System.out.println("End of Game, Game is Pat (more than 50 move.)");
						manager.endIt();
						manager.notifyAll();
						break;
					}
					
					long tempStart = System.currentTimeMillis();
					List<Move> lst = getListMove();
					long tempEnd = System.currentTimeMillis();
					System.out.println(this.color + " - Evaluated " + (nbNode-nbSearch) + " node and " + nbSearch + " leafs in " + (tempEnd - tempStart) + " mili-seconds");
					totalTime += (tempEnd - tempStart);
					
					if(lst.isEmpty()){
						if(PieceValidator.isCheck(manager.board)){
							System.out.println("End of Game, " + manager.board.turn + " is Mat.");
						} else {
							System.out.println("End of Game, Game is Pat.");
						}
						manager.endIt();
						manager.notifyAll();
						break;
					} else {
						int index = (int)(Math.random() * lst.size());
						Move selected = lst.get(index);
						
						manager.playTurn(selected.getPosA(), selected.getPosB());
						System.out.println(color + " - " + PositionUtil.translatePosition(selected.getPosA()) + " => " + PositionUtil.translatePosition(selected.getPosB()));
						//BoardUtil.showBoard(manager.board);
						manager.notifyAll();
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			BoardUtil.showBoard(manager.board);
		}
		
		System.out.println(this.color + " - played in " + (totalTime) + " mili-seconds");
	}
	
	protected abstract List<Move> getListMove();
	
	public static boolean log = false;
	
	protected void log(String str){
		if(log)
			System.out.println(str);
	}
	
	public synchronized void increaseSearch(){
		nbSearch ++;
	}
	
	public synchronized void increaseNode(){
		nbNode ++;
	}
}
