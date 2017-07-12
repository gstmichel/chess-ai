package org.gstmichel.chess.ui;

import java.util.Scanner;
import java.util.StringTokenizer;

import org.gstmichel.chess.piece.Color;
import org.gstmichel.chess.piece.validator.PieceValidator;
import org.gstmichel.chess.rules.BoardManager;
import org.gstmichel.chess.util.BoardUtil;
import org.gstmichel.chess.util.PositionUtil;

public class ChessPlayer implements Runnable {

	public Color color;
	public BoardManager manager;
	public Scanner scanner;
	
	protected ChessPlayer(){
		throw new IllegalArgumentException("Can't use the empty constructor");
	}
	
	public ChessPlayer(BoardManager manager, Color color){
		this.color = color;
		this.manager = manager;
		this.scanner = new Scanner(System.in);
	}
	
	public void run(){
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
					
					boolean mate = true;
					for(int i : BoardUtil.getPositionOnBoard(manager.board.getCurrent())){
						if(BoardUtil.getPieceAt(manager.board, i).getMove(manager.board, i).length != 0){
							mate = false;
						}
					}
					if(mate){
						if(PieceValidator.isCheck(manager.board)){
							System.out.println("End of Game, " + manager.board.turn + " is Mat.");
						} else {
							System.out.println("End of Game, Game is Pat.");
						}
						manager.endIt();
						manager.notifyAll();
						break;
					}
					
						
						boolean valid = false;
						do{
							//System.out.println(manager.board.enPassant);
							BoardUtil.showBoard(manager.board);
							String cmd = scanner.nextLine();
							if(cmd.length() == 5){
								StringTokenizer st = new StringTokenizer(cmd, ":");
								if(st.countTokens() == 2){
									String pos1 = st.nextToken();
									String pos2 = st.nextToken();
									
									if(pos1.length() == 2 && pos2.length() == 2){
										try{
											int posA = PositionUtil.translatePosition(pos1);
											int posB = PositionUtil.translatePosition(pos2);
											
											valid = manager.playTurn(posA, posB);
										} catch (Exception e){e.printStackTrace();}
									}
								}
							}
						} while (!valid);
						manager.notifyAll();
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			BoardUtil.showBoard(manager.board);
		}
	}
}
