package KnightsTour;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class KnightFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPanel pMenu;
	private final JPanel pBody;
	private final JMenuBar menuBar;
	private final JMenu menu;
	private final JMenuItem autoMenuItem;
	private final JMenuItem manualMenuItem;
	private final JMenuItem diyMenuItem;
	private final JButton nextMoveBt;
	
	
	ArrayList<JButton> buttary = new ArrayList<JButton>();
	
	
	int randomRow = (int)(Math.random() * 8);
	int randomCol = (int)(Math.random() * 8);
	int btIndex = randomRow * 8 + randomCol;
	Knight manTour = new Knight(randomRow, randomCol);
	
	Knight diyTour = new Knight();
	ArrayList<Integer> visitedBtns = new ArrayList<Integer>();
	
	public KnightFrame() {
		super("Knight's Tour");
		
		pMenu = new JPanel(new FlowLayout(FlowLayout.LEADING));
		pBody = new JPanel(new GridLayout(8,8));	
		
		menuBar = new JMenuBar();
		menu = new JMenu("Start");
		autoMenuItem = new JMenuItem("Play automatically");
		manualMenuItem = new JMenuItem("Click manually");
		diyMenuItem = new JMenuItem("Try it yourself!");
		menu.add(autoMenuItem);
		menu.add(manualMenuItem);
		menu.add(diyMenuItem);
		menuBar.add(menu);
		
		nextMoveBt = new JButton("Next Move");
		nextMoveBt.setEnabled(false);
		menuBar.add(nextMoveBt);
		
		pMenu.add(menuBar);
		
		for(int i = 0; i < 64; i ++) {
			buttary.add(new JButton());
			buttary.get(i).setEnabled(false);
			pBody.add(buttary.get(i));
		}
		
		setLayout(new BorderLayout());
		add(pMenu, BorderLayout.PAGE_START);
		add(pBody);
		
		MenuItemHandler mIHandler = new MenuItemHandler();
		autoMenuItem.addActionListener(mIHandler);
		manualMenuItem.addActionListener(mIHandler);
		diyMenuItem.addActionListener(mIHandler);
		
		ButtonHandler bHandler = new ButtonHandler();
		nextMoveBt.addActionListener(bHandler);
		
		MoveBtHandler mBtHandler = new MoveBtHandler();
		for(JButton bt : buttary) {
			bt.addActionListener(mBtHandler);
		}
	}
	
	private class MenuItemHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			for(JButton bt : buttary) {
				bt.setEnabled(false);
			}
			nextMoveBt.setEnabled(false);
			if(event.getSource() == autoMenuItem) {
				int randomRow = (int)(Math.random() * 8);
				int randomCol = (int)(Math.random() * 8);
				int btIndex = randomRow * 8 + randomCol;
				
				
				Knight autoTour = new Knight(randomRow, randomCol);
				buttary.get(btIndex).setText("" + autoTour.getCounter());
				
				do {
					autoTour.nextMove();
					btIndex = autoTour.getRow() * 8 + autoTour.getCol();
					buttary.get(btIndex).setText("" + autoTour.getCounter());
				} while(autoTour.ifMoreMoves() && autoTour.getCounter() < 64);
			}
			else if(event.getSource() == manualMenuItem) {
				for(JButton bt : buttary) {
					bt.setText("");
				}
				
				if(manTour.getCounter() == 1) {
					buttary.get(btIndex).setText("" + manTour.getCounter());
				}
				else {
					int randomRow = (int)(Math.random() * 8);
					int randomCol = (int)(Math.random() * 8);
					btIndex = randomRow * 8 + randomCol;
					manTour = new Knight(randomRow, randomCol);
					buttary.get(btIndex).setText("" + manTour.getCounter());
				}
				nextMoveBt.setEnabled(true);
			}
			else if(event.getSource() == diyMenuItem) {
				nextMoveBt.setEnabled(false);
				for(JButton bt : buttary) {
					bt.setText("");
					bt.setEnabled(true);
				}
			}
			
		}
	}
	
	private class ButtonHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			for(JButton bt : buttary) {
				bt.setEnabled(false);
			}
			if(event.getSource() == nextMoveBt) {
				if(manTour.ifMoreMoves() && manTour.getCounter() < 64) {
					manTour.nextMove();
					btIndex = manTour.getRow() * 8 + manTour.getCol();
					buttary.get(btIndex).setText("" + manTour.getCounter());
				}
				else {
					nextMoveBt.setEnabled(false);
				}
			}
		}
	}
	
	private class MoveBtHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			int buttonIndex = -1;
			int flag = 1;
			for(int i = 0; i < 64 && flag == 1; i++) {
				if(event.getSource() == buttary.get(i)) {
					buttonIndex = i;
					flag = 0;
				}
			}
			
			int row = buttonIndex / 8;
			int col = buttonIndex - row * 8;
			
			if(visitedBtns.isEmpty()) {
				diyTour = new Knight(row, col);
				buttary.get(buttonIndex).setText("" + diyTour.getCounter());
				
				ArrayList<Integer> possibleMoves = diyTour.getPossibleMoves(row, col);
				ArrayList<Integer> possibleBtnsIndex = new ArrayList<Integer>();
				for(int possibleMove : possibleMoves) {
					int newRow = row + Knight.getVertical()[possibleMove];
					int newCol = col + Knight.getHorizontal()[possibleMove];
					possibleBtnsIndex.add(newRow * 8 + newCol);
				}
				
				for(JButton bt : buttary) {
					bt.setEnabled(false);
				}
				
				for(int index : possibleBtnsIndex) {
					buttary.get(index).setEnabled(true);
				}
				visitedBtns.add(buttonIndex);
			}
			else {
				diyTour.setRow(row);
				diyTour.setCol(col);
				diyTour.incrementCounter();
				buttary.get(buttonIndex).setText("" + diyTour.getCounter());
				
				ArrayList<Integer> possibleMoves = diyTour.getPossibleMoves(row, col);
				ArrayList<Integer> possibleBtnsIndex = new ArrayList<Integer>();
				
				for(int possibleMove : possibleMoves) {
					int newRow = row + Knight.getVertical()[possibleMove];
					int newCol = col + Knight.getHorizontal()[possibleMove];
					int newPosIndex = newRow * 8 + newCol;
					if(!visitedBtns.contains(newPosIndex)) {
						possibleBtnsIndex.add(newRow * 8 + newCol);
					}
				}
				
				if(!possibleBtnsIndex.isEmpty()) {
					for(JButton bt : buttary) {
						bt.setEnabled(false);
					}
					
					for(int index : possibleBtnsIndex) {
						buttary.get(index).setEnabled(true);
					}
					
					visitedBtns.add(buttonIndex);
				}
				else {
					for(JButton bt : buttary) {
						bt.setEnabled(false);
					}
					visitedBtns.clear();
				}
				
			}
		}
	}
	
}
