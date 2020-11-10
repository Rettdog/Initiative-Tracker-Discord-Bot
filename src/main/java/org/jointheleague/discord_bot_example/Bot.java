package org.jointheleague.discord_bot_example;

import org.javacord.api.DiscordApi;

import org.javacord.api.DiscordApiBuilder;

/**
 * Launches all of the listeners for one channel.
 * @author keithgroves and https://tinystripz.com
 *
 */
import org.jointheleague.modules.*;



public class Bot  {
	
	// The string to show the custom :vomiting_robot: emoji
	public static String emoji = "<:vomiting_robot:642414033290657803>";

	private String token;
	private String channelName;
	DiscordApi api;

	public Bot(String token, String channelName) {
		this.token = token;
		this.channelName = channelName;
	}

	public void connect(boolean printInvite) {
		
		api = new DiscordApiBuilder().setToken(token).login().join();

		// Print the URL to invite the bot
		if (printInvite) {
			System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
		}

		api.getServerTextChannelsByName(channelName).forEach(e -> e.sendMessage("Bot Connected"));
		
		//add Listeners

		//ClassName name = new ClassName(channelName);
		//api.addMessageCreateListener(name);
		//helpListener.addHelpEmbed(name.getHelpEmbed());
		
		//old way to add listeners

		api.addMessageCreateListener(new InitiativeTrackerListener(channelName));
		//api.addMessageCreateListener(name);

	}
}
