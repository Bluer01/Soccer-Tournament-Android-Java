package com.example.interfaces.interfacesapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

@SuppressWarnings("ALL")
class Tournament {
	private int numTeams, numRounds;
	private Team teamList[];
	private Round rounds[];
	private String name;
	private String type;
	private Bundle tourBun;
	private int[] roundMatches;


	private Team oddOneOut;

	/**
	 * Create an empty Tournament with the specified name, number of teams, and number of rounds.
	 * 
	 * @param name
	 * @param numTeams
	 * @param numRounds
	 */
	public Tournament(String name, String type, int numTeams, int numRounds) {
		this.name = name;
		this.type = type;
		this.numTeams = numTeams;
		this.numRounds = numRounds;
		teamList = new Team[numTeams];
		rounds = new Round[numRounds];

	}

	/**
	 * Loads a pre-existing tournament from the bundle.
	 *
	 */
	public Tournament(Context context, Bundle tourInfo) {

		try {
            tourBun = tourInfo;
			int lineNum, numMatches, score1, score2;
            String[] teamNames;
			int[] teamJerseys;

			Team team1, team2;

			name = tourBun.getString("name");
			type = tourBun.getString("type");
			numTeams = tourBun.getInt("numTeams");
			numRounds = tourBun.getInt("numRounds");
            teamNames = tourBun.getStringArray("teamNames");
			teamJerseys = tourBun.getIntArray("teamJerseys");
            roundMatches = tourBun.getIntArray("roundMatches");

            Log.i("READ_TOUR", "Loading \""+type+"\" tournament \""+name+"\" ("+numTeams+" teams, "+numRounds+" rounds)");

			teamList = new Team[numTeams];
			rounds = new Round[numRounds];

			for ( int i = 0; i < numTeams; i++ ) {

				Log.i("READ_TOUR", "Loaded team ["+i+"]");

                assert teamNames != null;
                teamList[i] = new Team(teamNames[i]);
			}

			// Reading matches
			for ( int i = 0; i < numRounds; i++ ) {
                numMatches = roundMatches[i];
				Log.i("READ_TOUR", "Loading match ["+numMatches + "]...");

				rounds[i] = new Round(numMatches);

				for ( int j = 0; j < numMatches; j++ ) {

						team1 = teamList[tourBun.getInt("round"+i+"Match"+j+"Team1")];
						team2 = teamList[tourBun.getInt("round"+i+"Match"+j+"Team2")];

						score1 = tourBun.getInt("round" + i + "Match" + j + "Team1Goals");

						if ( score1 == -1 )
							rounds[i].addMatch(new Match(team1, team2));
						else {
							score2 = tourBun.getInt("round"+i+"Match"+j+"Team2Goals");

							rounds[i].addMatch(new Match(team1, team2, score1, score2));
						}

					}

				}

		} catch ( Exception e ) {}

	}

	/**
	 * Getter
	 *
	 * @return the number of teams.
	 */
	public int getNumTeams() {
		return numTeams;
	}

	/**
	 * Getter
	 *
	 * @return the tournament name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter
	 *
	 * @return the tournament type.
	 */
	public String getType() {
		return type;
	}

    public Bundle getTourBun() { return tourBun; }

	/**
	 * Getter
	 * 
	 * @return the number of rounds.
	 */
	public int getNumRounds() {
		return numRounds;
	}

	/**
	 * Adds a team to the tournament. Stores in an array of size [numTeams] with placement in the first
	 * available slot.
	 * 
	 * @param team
	 * @return True if successful, false if not.
	 */
	public boolean addTeam(Team team) {
		if (team != null) {
			for (int i = 0; i < numTeams; i++) {
				if (teamList[i] == null) {
					teamList[i] = team;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Creates a new new with the provided name and adds it to the tournament. Stores in an array of size
	 * [numTeams] with placement in the first available slot.
	 * 
	 * @param name
	 * @return True if successful, false if not.
	 */
	public boolean addTeam(String name) {
		if (name != null)
			for (int i = 0; i < numTeams; i++) {
				if (teamList[i] == null) {
					teamList[i] = new Team(name);
					return true;
				}
			}
		return false;
	}

	public boolean addTeam(String name, int jersey) {
		if (name != null && jersey != 0)
			for (int i = 0; i < numTeams; i++) {
				if (teamList[i] == null) {
					teamList[i] = new Team(name, jersey);
					return true;
				}
			}
		return false;
	}

	/**
	 * Getter
	 * 
	 * @param index
	 * @return The team at the specified index.
	 */
	public Team getTeam(int index) {
		if (index < 0 || index > numTeams - 1)
			throw new IndexOutOfBoundsException("If you want a message, put it here");
		return teamList[index];
	}

	/**
	 * 
	 * @param team
	 * @return The team's position in the array.
	 */
	public int getTeamIndex(Team team) {
		return java.util.Arrays.asList(teamList).indexOf(team);
	}

	/**
	 * Replaces the team at the specified index with the new team. Requires that the index contains an
	 * existing team.
	 * 
	 * @param index
	 *            Integer value representing the target team's location in the array.
	 * @param team
	 *            The new team to be added to the tournament.
	 * @return True if successful, false if not.
	 */
	public boolean editTeam(int index, Team team) {
		if (teamList[index] == null)
			return false;
		if (index < 0 || index > numTeams - 1)
			throw new IndexOutOfBoundsException("If you want a message, put it here");
		teamList[index] = team;
		return true;
	}

	/**
	 * Saves the tournament to a .txt file. Will overwrite any existing tournaments with the same name.
	 * 
	 * @return True if successful, false if not.
	 */
	public boolean saveTournament() {
		try {
            tourBun = new Bundle();
			tourBun.putString("name", name);
			tourBun.putString("type", type);
			tourBun.putInt("numTeams", numTeams);
			tourBun.putInt("numRounds", numRounds);
			int[] teamJerseys = new int[numTeams];

            String[] teamNames = new String[numTeams];
			for (int i = 0; i < numTeams; i++) {
				teamNames[i] = teamList[i].getName();
				teamJerseys[i] = teamList[i].getJersey();
				Log.i("SAVE_TOUR", "Team " + i + "'s Jersey: " + String.valueOf(teamJerseys[i]));
			}

			for (int i = 0; i < numTeams; i++) {
				tourBun.putStringArray("teamNames", teamNames);
				tourBun.putIntArray("teamJerseys", teamJerseys);
			}

			int score1;

			roundMatches = new int[numRounds];

			for (int i = 0; i < numRounds; i++) {

                roundMatches[i] = rounds[i].getNumMatches();


				for (int j = 0; j < rounds[i].getNumMatches(); j++) {

					if ( rounds[i].getMatch(j) != null ) {

						tourBun.putInt("round"+i+"Match"+j+"Team1",(java.util.Arrays.asList(teamList)
								.indexOf(rounds[i].getMatch(j).getTeam1())));

						tourBun.putInt("round" + i + "Match" + j + "Team2", (java.util.Arrays.asList(teamList)
								.indexOf(rounds[i].getMatch(j).getTeam2())));

						score1 = rounds[i].getMatch(j).getTeam1Goals();

						if (score1 == -1) {
							tourBun.putInt("score1", -1);
						} else {
							tourBun.putInt("round" + i + "Match" + j + "Team1Goals", score1);
							tourBun.putInt("round" + i + "Match" + j + "Team2Goals", rounds[i].getMatch(j).getTeam2Goals());
						}
					} else
						tourBun.putInt("null", -1);
				}
			}

            tourBun.putIntArray("roundMatches", roundMatches);
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * Inserts the round into the next available space in the rounds list.
	 * 
	 * @param round
	 * @return True if successful, false if not.
	 */
	public boolean addRound(Round round) {
		if (round != null)
			for (int i = 0; i < numRounds; i++) {
				if (rounds[i] == null) {
					rounds[i] = round;
					return true;
				}
			}
		return false;
	}

	/**
	 * Creates a new Round and inserts it into the next available space in the rounds list.
	 * 
	 * @param numMatches
	 * @return True if successful, false if not.
	 */
	public boolean addRound(int numMatches) {
		if (numMatches > 0)
			for (int i = 0; i < numRounds; i++) {
				if (rounds[i] == null) {
					rounds[i] = new Round(numMatches);
					return true;
				}
			}
		return false;
	}

	public void getRoundMatches() {
        for (int i = 0; i <  roundMatches.length; i++) {
            System.out.println("numMatches in round " + i + " is " + roundMatches[i]);
        }
	}

	public int getRoundMatches(int roundIndex) {
		return this.roundMatches[roundIndex];
	}

	/**
	 * Returns the round at given index.
	 *
	 * @param index
	 * @return Returns the round object
	 */
	public Round getRound(int index) {
		Round temp = rounds[index];
		return temp;
	}

	/**
	 * Calculates and returns statistics for the team at the specified index.
	 * 
	 * @param index
	 * @return An array of size 5. int[goals for, goals against, wins, losses, ties];
	 */

	public int[] getTeamStats(int index) {
		Team winner;// A temporary variable used to count the number of wins.
		if (index < 0 || index > numTeams - 1)
			throw new IndexOutOfBoundsException("If you want a message, put it here");
		Match match;
		int stats[] = new int[4];
		for (int i = 0; i < numRounds; i++) {
			for (int j = 0; j < rounds[i].getNumMatches(); j++) {
				if (rounds[i].getMatch(0) != null) {
					match = rounds[i].getMatch(j);
					if (match.getTeam1().equals(teamList[index])) {
						stats[0] += match.getTeam1Goals();
						stats[1] += match.getTeam2Goals();
						try {
							winner = match.getWinner();
							if (winner != null && winner.equals(teamList[index])) {// If the team won
								stats[2]++;
							} else if (winner != null){// If the team lost
								stats[3]++;
							}
						} catch (IndexOutOfBoundsException e) {// A LOT of out of bounds exceptions will be
							// thrown.

						}
					} else if (match.getTeam2().equals(teamList[index])) {
						stats[0] += match.getTeam2Goals();
						stats[1] += match.getTeam1Goals();
						try {
							winner = match.getWinner();
							if (winner != null && winner.equals(teamList[index])) {// If the team won
								stats[2]++;
							} else if (winner != null) {// If the team lost
								stats[3]++;
							}
						} catch (IndexOutOfBoundsException e) {// A LOT of out of bounds exceptions will be
							// thrown.

						}
					}
				}
			}
		}
			return stats;
	}

	/**
	 * Makes the tournament active and populates the first round with the teams in a random order.
	 * 
	 * @return True if successful, false if not.
	 */
	public boolean randomStart() {
		if (rounds[0].getMatch(0) != null)
			return false;
		int randomizer[] = new int[numTeams];
		int temp, index;
		for (int i = 0; i < numTeams; i++) {
			randomizer[i] = i;
		}
		Random random = new Random();
		for (int i = numTeams - 1; i > 0; i--) {
			index = random.nextInt(i + 1);
			temp = randomizer[i];
			randomizer[i] = randomizer[index];
			randomizer[index] = temp;
		}
		for (int i = 0; i < rounds[0].getNumMatches() * 2; i = i + 2) {
			rounds[0].addMatch(new Match(teamList[randomizer[i]], teamList[randomizer[i + 1]]));
		}
		oddOneOut = teamList[randomizer[teamList.length - 1]];
		return true;
	}

	public Team getOddOneOut() {
		return oddOneOut;
	}

	/**
	 * Sets the score of the specified game.
	 * 
	 * @param roundNum
	 * @param matchNum
	 * @param team1Goals
	 * @param team2Goals
	 * @return True if successful, false if not.
	 */
	public boolean enterScore(int roundNum, int matchNum, int team1Goals, int team2Goals) {
		if (team1Goals < 0 || team2Goals < 0 || roundNum < 0 || roundNum > numRounds || matchNum < 0
				|| matchNum > rounds[roundNum].getNumMatches())
			return false;
		rounds[roundNum].getMatch(matchNum).setTeam1Goals(team1Goals);
		rounds[roundNum].getMatch(matchNum).setTeam2Goals(team2Goals);
		return true;
	}

	/**
	 * Returns the winner of the specified math.
	 * 
	 * @param roundNum
	 * @param matchNum
	 * @return Returns the victorious team. In the even the match was a tie, team1 is returned.
	 */
	public Team getWinner(int roundNum, int matchNum) {
		Team temp = rounds[roundNum].getMatch(matchNum).getWinner();
		if (temp == null)
			return rounds[roundNum].getMatch(matchNum).getTeam1();
		return temp;
	}

	/**
	 * Adds a new match between the two specified teams to the first available spot in the round.
	 * 
	 * @param roundNum
	 * @param team1
	 * @param team2
	 * @return True if successful, false if not.
	 */
	public boolean addMatch(int roundNum, int team1, int team2) {
		if (rounds[roundNum].canAdd()) {
			rounds[roundNum].addMatch(new Match(teamList[team1], teamList[team2]));
			return true;
		}

		return false;
	}

	/**
	 * Adds a new match between the two specified teams to the first available spot in the round.
	 * 
	 * @param roundNum
	 * @param team1
	 * @param team2
	 * @return True if successful, false if not.
	 */
	public boolean addMatch(int roundNum, Team team1, Team team2) {

		if (rounds[roundNum].canAdd()) {
			rounds[roundNum].addMatch(new Match(team1, team2));
			return true;
		}
		return false;

	}

	/**
	 * Gets a team's number of goals in a specific game.
	 * 
	 * @param numRound
	 * @param numMatch
	 * @param numTeam
	 * @return The score of the specified team in the specified match.
	 */
	public int getScore(int numRound, int numMatch, int numTeam) {
		if (teamList[numTeam].equals(rounds[numRound].getMatch(numMatch).getTeam1()))
			return rounds[numRound].getMatch(numMatch).getTeam1Goals();
		else if (teamList[numTeam].equals(rounds[numRound].getMatch(numMatch).getTeam2()))
			return rounds[numRound].getMatch(numMatch).getTeam2Goals();
		return -2;
	}

	/**
	 * Return the progress (out of 100) of a tournament
	 *
	 * @return Tournament progress (out of 100)
	 *
	 * @author Anatolie Diordita
	 */
	public int getProgress() {

		int numRounds = this.getNumRounds();
		double numMatches = 0;
		double numMatchesComplete = 0;

		Round tempRound;
		Match tempMatch;

		// Loop through each round
		for ( int i = 0; i < numRounds; i++ ) {
			tempRound = this.getRound(i);

			numMatches += tempRound.getNumMatches();

			// Loop through each match
			for ( int j = 0; j < tempRound.getNumMatches(); j++ ) {
				tempMatch = tempRound.getMatch(j);

				// Only count completed matches as those that exist/have score
				if (tempMatch != null && tempMatch.getTeam1Goals() != -1)
					numMatchesComplete++;

			}

		}

		return (int)( (numMatchesComplete / numMatches) * 100 );

	}

	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append(name + "\n");
		for (int i = 0; i < numRounds; i++) {
			if (rounds[i] != null)
				out.append(rounds[i].toString());
		}
		return out.toString();
	}
}
