package com.example.interfaces.interfacesapp;

/**
 * A team within the tournament. To keep things simple, each team is assigned a name.
 *
 * @author Willem Kowal
 *
 */
public class Team {
	private String name;
	private int jersey;
	//private String jerseyImage;
	//private int jerseyId;

	/**
	 * Creates a new team with default values of color=0 and name ="Enter a name".
	 */
	public Team() {
		jersey = 0;
		name = "Enter a name";
	}

	/**
	 * Creates a team with the specified name.
	 *
	 * @param name
	 */
	public Team(String name, int jersey) {
		setJersey(jersey);
		this.name = name;
	}

	public Team(String name) {
		this.name = name;
		jersey=0;
	}

	/**
	 * Getter
	 *
	 * @return The team's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the team's name.
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 *
	 * @return jersey
	 */
	public int getJersey() {
		return jersey;
	}

	/**
	 * Sets the team's jersey
	 * @param jersey
	 */
	public void setJersey(int jersey) {
		if (jersey != 0 && jersey <= 20) { //CHANGED AND ADDED SOME IF STATEMENTS
			// ADDED BY LUCAS PARSCHE ON 08/04/2016
			switch (jersey) {
				case 1:
					this.jersey= R.drawable.shirt1;
					break;
				case 2:
					this.jersey= R.drawable.shirt2;
					break;
				case 3:
					this.jersey= R.drawable.shirt3;
					break;
				case 4:
					this.jersey= R.drawable.shirt4;
					break;
				case 5:
					this.jersey= R.drawable.shirt5;
					break;
				case 6:
					this.jersey= R.drawable.shirt6;
					break;
				case 7:
					this.jersey= R.drawable.shirt7;
					break;
				case 8:
					this.jersey= R.drawable.shirt8;
					break;
				case 9:
					this.jersey= R.drawable.shirt9;
					break;
				case 10:
					this.jersey= R.drawable.shirt10;
					break;
				case 11:
					this.jersey= R.drawable.shirt11;
					break;
				case 12:
					this.jersey= R.drawable.shirt12;
					break;
				case 13:
					this.jersey= R.drawable.shirt13;
					break;
				case 14:
					this.jersey= R.drawable.shirt14;
					break;
				case 15:
					this.jersey= R.drawable.shirt15;
					break;
				case 16:
					this.jersey= R.drawable.shirt16;
					break;
				case 17:
					this.jersey= R.drawable.shirt17;
					break;
				case 18:
					this.jersey= R.drawable.shirt18;
					break;
				case 19:
					this.jersey= R.drawable.shirt19;
					break;
				case 20:
					this.jersey= R.drawable.shirt20;
					break;
			}
		}
		else if(jersey > 20){ //means that when I pass it the R.drawable value, it doesn't return 0.
			this.jersey = jersey;
		}
	}

	/**
	 * Returns a string representation of the class.
	 */
	public String toString() {
		return "Team name: " + name + " Team jersey: " + jersey;
	}

}
