package org.jointheleague.modules;

import net.aksingh.owmjapis.api.APIException;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class InitiativeTrackerListener extends CustomMessageCreateListener{

    public String ADD_COMMAND = "!addinit ";
    public String CHANGE_COMMAND = "!changeinit ";
    public String VIEW_COMMAND = "!viewinits";
    public String DELETE_COMMAND = "!deleteinit";
    public String CLEAR_COMMAND = "!clearinits";
    public String HELP_COMMAND = "!help";
    public HashMap<String, Integer> initiatives = new HashMap<>();

    public InitiativeTrackerListener(String channelName) {
        super(channelName);
    }

    @Override
    public void handle(MessageCreateEvent event) throws APIException {
        String input = event.getMessageContent();
        String player = event.getMessageAuthor().getDisplayName();

        //check if bot sent message
        if(!player.equals("Initiative Tracker Bot")){

            //one Parameter
            if(input.length()>ADD_COMMAND.length()&&input.substring(0,ADD_COMMAND.length()).equals(ADD_COMMAND)){

                //adds an initiative
                if(!initiatives.containsKey(player)) {
                    int value = getParam(input, ADD_COMMAND);
                    initiatives.put(player, value);
                    sendThumbsUp(event);
                }else{
                    sendThumbsDown(event);
                    event.getChannel().sendMessage("Could not add initiative value for a player already added.\n" +
                            "Use "+CHANGE_COMMAND+" to change an initiative value");
                }

            }else if(input.length()>CHANGE_COMMAND.length()&&input.substring(0,CHANGE_COMMAND.length()).equals(CHANGE_COMMAND)){

                //changes an initiative
                if(initiatives.containsKey(player)){
                    int value = getParam(input, CHANGE_COMMAND);
                    initiatives.replace(player,value);
                    sendThumbsUp(event);
                }else{
                    sendThumbsDown(event);
                    event.getChannel().sendMessage("Could not change initiative value for a player not yet added.\n" +
                            "Use "+ADD_COMMAND+" to add an initiative value");
                }

            }

            //no Parameters
            if(input.equals(HELP_COMMAND)){

                //sends a help message
                sendThumbsUp(event);
                event.getChannel().sendMessage(
                        "This is a D&D Initiative Tracker\n" +
                                "Use "+ADD_COMMAND+" to add an initiative\n" +
                                "Use "+CHANGE_COMMAND+" to change an initiative\n" +
                                "Use "+DELETE_COMMAND+" to change an initiative\n" +
                                "Use "+VIEW_COMMAND+" to view the initiative order\n" +
                                "Use "+CLEAR_COMMAND+" to clear all initiatives");

            }else if(input.equals(DELETE_COMMAND)){

                //deletes the player's initiative
                if(initiatives.containsKey(player)){
                    initiatives.remove(player);
                    sendThumbsUp(event);
                }else{
                    sendThumbsDown(event);
                    event.getChannel().sendMessage("Could not remove initiative value for a player not yet added.\n" +
                            "Use "+ADD_COMMAND+" to add an initiative value");
                }

            }else if(input.equals(CLEAR_COMMAND)){

                //clears all current initiatives
                if(initiatives.size()!=0){
                    for(String plyr: initiatives.keySet()) {
                        initiatives.remove(player);
                    }
                    sendThumbsUp(event);
                }else{
                    sendThumbsDown(event);
                    event.getChannel().sendMessage("Could not clear without any current initiative values.\n" +
                            "Use "+ADD_COMMAND+" to add an initiative value");
                }

            }else if(input.equals(VIEW_COMMAND)){

                //views the initiative order
                if(initiatives.size()!=0){
                    sendThumbsUp(event);
                    event.getChannel().sendMessage("Current Initiative Order"+sortInitiatives());
                }else{
                    sendThumbsDown(event);
                    event.getChannel().sendMessage("Could not view without any current initiative values.\n" +
                            "Use "+ADD_COMMAND+" to add an initiative value");
                }

            }

        }


    }

    public void sendThumbsUp(MessageCreateEvent event){
        CompletableFuture add = event.addReactionsToMessage("\uD83D\uDC4D");
        add.join();
        boolean complete = add.complete(null);
    }

    public void sendThumbsDown(MessageCreateEvent event){
        CompletableFuture add = event.addReactionsToMessage("\uD83D\uDC4E");
        add.join();
        boolean complete = add.complete(null);
    }


    public int getParam(String in, String command){
        String param = in.substring(command.length());
        return Integer.parseInt(param);
    }

    public String sortInitiatives() {

        //set up vars
        Set<String> players = (Set<String>) initiatives.keySet();
        Collection<Integer> values = initiatives.values();
        ArrayList<Integer> vals = new ArrayList<>();

        //turn collection to arraylist
        //remove duplicate values
        for (int i = 0; i < values.size(); i++) {
            int getValue = (Integer) values.toArray()[i];
            if (!vals.contains(getValue)){
                vals.add(getValue);
            }
        }

        //sort values
        Collections.sort(vals);

        //turn to string in descending order
        String out = "";
        for (int i = vals.size()-1;i>=0;i--) {
            int value = vals.get(i);
            for (String player : players) {
                if (initiatives.get(player) == value) {
                    out = out + "\n" + player + ": " + value;
                }
            }
        }
        return out;

    }

}
