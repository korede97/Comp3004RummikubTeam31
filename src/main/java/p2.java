	import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class p2 implements PlayerStrategy{
	Support function = new Support();
	HandleJoker checkMeld = new HandleJoker();
	
	PlayerStrategy2 old_p2 = new PlayerStrategy2();
	//split hand into 5 lists with 4 colors red, blue, green, orange and joker.
	ArrayList<Tile> blue = new ArrayList<Tile>();
	ArrayList<Tile> green = new ArrayList<Tile>();
	ArrayList<Tile> red = new ArrayList<Tile>();
	ArrayList<Tile> orange = new ArrayList<Tile>();
	ArrayList<Tile> joker = new ArrayList<Tile>();
	
	
	
	
	public boolean playTheGame(Player p) {
		int num = checkMeld.NumberOfJoker(p.getHand().getTiles());
		boolean complete_first_turn = p.getIsFirstMeldComplete();
		
		ArrayList<Tile> sample = new ArrayList<Tile>(p.getHand().getTiles());
		ArrayList<Tile> sample1 = new ArrayList<Tile>(p.getHand().getTiles());
		
		ArrayList<ArrayList<Tile>> output = new ArrayList<ArrayList<Tile>>();
		checkMeld.separateList(p.getHand().getTiles()); //  initial tile for each list of 5
		
		if(num == 0 && p.getIsFirstMeldComplete() == false) return old_p2.playTheGame(p);
		else {
			if(p.getTable().getNumberOfTile() > 0 && p.getIsFirstMeldComplete() == false) {
				ArrayList<ArrayList<Tile>> first = new ArrayList<ArrayList<Tile>>();
				ArrayList<ArrayList<Tile>> second = new ArrayList<ArrayList<Tile>>();
				
				ArrayList<ArrayList<Tile>> object = new ArrayList<ArrayList<Tile>>();
				ArrayList<ArrayList<Tile>> object1 = new ArrayList<ArrayList<Tile>>();
				
				//get runs, then get sets
				first = checkMeld.getFirst(complete_first_turn,num,sample);
				second = checkMeld.getSecond(complete_first_turn,num,sample);
				if(checkMeld.getPoint(first) <= checkMeld.getPoint(second)) object = second;
				else object = first;
				
				
				//get sets then get runs
				first = checkMeld.getSecond(complete_first_turn,num,sample1);
				second = checkMeld.getFirst(complete_first_turn,num,sample1);
				if(checkMeld.getPoint(first) <= checkMeld.getPoint(second)) object1 = second;
				else object1 = first;
				
				
				if(checkMeld.getPoint(object) <= checkMeld.getPoint(object1)) output = object1;
				else output = object;
				
				checkMeld.initialOutput(output,p.getPlayerHand().getTiles());
				if(function.getSizeOf(output) == p.getHand().sizeOfHand()) {
					for(int i =0; i < p.getHand().getTiles().size();i++) {
						p.getHand().getTiles().remove(i);
						i--;
					}
					p.setWinner();}
				
				if(checkMeld.getPoint(output) >= 30) {
					String out = "";
					int a = 0;
					loop :for(int i = output.size()-1; i > -1 ;i--) {
						p.getTable().addTiles(output.get(i));
						a += checkMeld.getPointOfSeq(output.get(i));
						for(int u = 0; u < output.get(i).size();u++) {
							if(output.get(i).get(u).isJoker()) {
								myloop: for(int k =0; k < p.getHand().getTiles().size();k++) {
									if(p.getHand().getTiles().get(k).isJoker() && p.getHand().getTiles().get(k).getJokerColor().equals(output.get(i).get(u).getJokerColor())
										&& p.getHand().getTiles().get(k).getJokerPoint() == output.get(i).get(u).getJokerPoint()) {
										p.getHand().getTiles().remove(k);
										break myloop;
									}
								}	
							}
							else
								p.getHand().getTiles().remove(output.get(i).get(u));
							out += output.get(i).get(u).toString();
						}
						if( a >= 30) break loop;
					}
					out += "\n";
					p.set_report(out);
					return true;
				}
				return false;
			}
			else if (p.getIsFirstMeldComplete()) {
				/*
				 * Get as much as meld as possible from the player hand (p2).
				 */
				ArrayList<ArrayList<Tile>> first = new ArrayList<ArrayList<Tile>>();
				ArrayList<ArrayList<Tile>> second = new ArrayList<ArrayList<Tile>>();
				
				ArrayList<ArrayList<Tile>> object = new ArrayList<ArrayList<Tile>>();
				ArrayList<ArrayList<Tile>> object1 = new ArrayList<ArrayList<Tile>>();
				
				//get runs, then get sets
				first = checkMeld.getFirst(complete_first_turn,num,sample);
				second = checkMeld.getSecond(complete_first_turn,num,sample);
				if(checkMeld.getPoint(first) <= checkMeld.getPoint(second)) object = second;
				else object = first;
				
				
				//get sets then get runs
				first = checkMeld.getSecond(complete_first_turn,num,sample1);
				second = checkMeld.getFirst(complete_first_turn,num,sample1);
				if(checkMeld.getPoint(first) <= checkMeld.getPoint(second)) object1 = second;
				else object1 = first;
				
				
				if(function.getSizeOf(object) <= function.getSizeOf(object1)) output = object1;
				else output = object;
				
				checkMeld.initialOutput(output,p.getPlayerHand().getTiles());
				/*
				 * Add tiles are not used to create meld in player in a list
				 * So tiles in the list will be used to play on the table
				 */
				ArrayList<Tile> useless_tile = new ArrayList<Tile>(p.getHand().getTiles());
				for(int i = output.size()-1; i > -1 ;i--) {
					for(int u = 0; u < output.get(i).size();u++) {
						if(output.get(i).get(u).isJoker()) {
							for(int k =0; k < useless_tile.size( );k++) {
								if(useless_tile.get(k).isJoker() && useless_tile.get(k).getJokerColor().equals(output.get(i).get(u).getJokerColor())
										&& useless_tile.get(k).getJokerPoint() == output.get(i).get(u).getJokerPoint()) 
								{
									useless_tile.remove(useless_tile.get(k));
								}
							}	
						}
						else {
							if (useless_tile.contains(output.get(i).get(u))) {
								useless_tile.remove(output.get(i).get(u));
							}
						}
					}
				}
				ArrayList<ArrayList<Tile>> table = new ArrayList<ArrayList<Tile>>(p.getTable().getTable());
				
				// index of meld on the table, which contain joker tiles
				int index = 999;
				int index_meld = 0; // index of joker tile in the meld
				
				int index1= 999;
				int index_meld1 = 0;// index of joker tile in the meld
				// find the joker meld in the table
				for(int i =0; i < table.size();i++) {
					for(int u =0; u < table.get(i).size();u++) {
						if(table.get(i).get(u).isJoker()) 
							if(index == 999 ) {index = i; index_meld = u;}
							else {index1 = i; index_meld1 = u;}
					}
				}	
				//check useless tile can replace joker tiles on the table or not.
				
				int indexToReplace = 999;
				int indexToReplace1 = 999;
				
				
				if(index != 999) {
					for(int i =0; i < useless_tile.size();i++) {
						if(useless_tile.get(i).getNumber() == table.get(index).get(index_meld).getJokerPoint()) {
							if(checkMeld.isSet(table.get(index))) {
								HashSet<String> checking_color = new HashSet<String>();
								for(int x =0; x< table.get(index).size();x++) {
									checking_color.add(table.get(index).get(x).getColor());
								}
								if(checking_color.add(useless_tile.get(i).getColor())) {
									indexToReplace = i;
								}
							}
							else {
								if(useless_tile.get(i).getColor().equals(table.get(index).get(index_meld).getJokerColor())) {
									indexToReplace = i;
								}
							}
						}
					}
				}
				if(index1 != 999) {
					for(int i =0; i < useless_tile.size();i++) {
						if(useless_tile.get(i).getNumber() == table.get(index1).get(index_meld1).getJokerPoint()) {
							if(checkMeld.isSet(table.get(index1))) {
								HashSet<String> checking_color = new HashSet<String>();
								for(int x =0; x< table.get(index1).size();x++) {
									checking_color.add(table.get(index1).get(x).getColor());
								}
								if(checking_color.add(useless_tile.get(i).getColor()));{
									indexToReplace1 = i;
									
									}
							}
							else {
								if(useless_tile.get(i).getColor().equals(table.get(index1).get(index_meld1).getJokerColor()))
									indexToReplace1 = i;
							}
						}
					}
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			return false;	
			}
		return false;
		}
	}
}
/*
	// replace the joker on the table
						if(canReplace) {
							table.get(i).get(table_index).setJoker(false);
							table.get(i).get(table_index).setColor(current.get(index_current).getColor());
							table.get(i).get(table_index).setNumber(current.get(index_current).getNumber());
							current.get(index_current).setJoker(true);
							current.get(index_current).setColor("J");
							current.get(index_current).setNumber(14);
							// use joker tile to play tile on hand first.
							ArrayList<ArrayList<Tile>> melds = new ArrayList<ArrayList<Tile>>();
							if(checkMeld.getJokerSequences(true, 1, current).size() > 0 && checkMeld.getPoint(checkMeld.getJokerSequences(true, 1, current)) > 0) {
								melds = checkMeld.getJokerSequences(true, 1, current);
							}
							else if (checkMeld.getJokerSet(true, 1, current).size() > 0) {
								melds = checkMeld.getJokerSet(true, 1, current);
							}
							else {
								// if can't use joker to play tiles on hand, find place to put it back to the table.
								loop : for(int u =0; u < table.get(i).size();u++) {
									ArrayList<Tile> a = new ArrayList<Tile>(table.get(i));
									a.add(current.get(index_current));
									if(checkMeld.isJokerSequences(a)) {
										table.get(i).add(current.get(index_current));
										break loop;
									}
									else if (checkMeld.isJokerSet(a)) {
										table.get(i).add(current.get(index_current));
										break loop;
									}
								}
								return true;
							}
							// if can play all tiles, p2 play all the tiles and become a winner
							if(function.getSizeOf(melds) == useless_tile.size()) {
								for(int u =0; u < p.getHand().sizeOfHand();u++) {
									p.getHand().playTileFromHand(p.getHand().getTile(u));
									u--;
								}
								p.getHand().HandReader();
								p.setWinner();
								return true;
							}
							// else it plays as most as tiles as it can and put in on the table
							else if(melds.size() > 0 ) {
								// remove tiles from player' hand
								for(int k =0; k < melds.size();k++) {
									p.getTable().addTiles(melds.get(k));
									for(int  u =0; u < melds.get(k).size();u++) {
										if(melds.get(k).get(u).isJoker()) {
											loop: for(int x =0; x < p.getHand().sizeOfHand();x++) {
												if(p.getHand().getTile(x).isJoker()) {
													if(p.getHand().getTile(x).getJokerPoint() == melds.get(k).get(u).getJokerPoint() && 
															p.getHand().getTile(x).getColor().equals( melds.get(k).get(u).getJokerColor()))
														p.getHand().removeTile(p.getHand().getTile(x));
												}
											}
										}
										else
											p.getHand().playTileFromHand(melds.get(k).get(u));	
									}
								}
								return true;
							}
							// return false if melds == 0
							else 
								return false;
						}
					}
					
*/
