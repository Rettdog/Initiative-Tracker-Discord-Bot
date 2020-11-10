package org.jointheleague.modules;

import net.aksingh.owmjapis.api.APIException;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

public class InitiativeTrackerListener extends CustomMessageCreateListener{

    public String ADD_COMMAND = "!addInit ";
    public String CHANGE_COMMAND = "!changeInit ";
    public String VIEW_COMMAND = "!viewInits";
    public String DELETE_COMMAND = "!deleteInit";
    public String CLEAR_COMMAND = "!clearInits";
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
                event.getChannel().sendMessage("add");
                if(!initiatives.containsKey(player)) {
                    int value = getParam(input, ADD_COMMAND);
                    initiatives.put(player, value);
                }else{
                    event.getChannel().sendMessage("Could not add initiative value for a player already currently added.\n" +
                            "Try using "+CHANGE_COMMAND+" to change an initiative value");
                }
            }else if(input.length()>CHANGE_COMMAND.length()&&input.substring(0,CHANGE_COMMAND.length()).equals(CHANGE_COMMAND)){

                event.getChannel().sendMessage("change");
                if(initiatives.containsKey(player)){
                    int value = getParam(input, CHANGE_COMMAND);
                    initiatives.replace(player,value);
                }else{
                    event.getChannel().sendMessage("Could not change initiative value for a player not currently added.\n" +
                            "Try using "+ADD_COMMAND+" to add an initiative value");
                }

            }

            //no Parameters
            if(input.equals(HELP_COMMAND)){
                event.getChannel().sendMessage(
                        "This is a D&D Initiative Tracker\n" +
                                "Use "+ADD_COMMAND+" to add an initiative\n" +
                                "Use "+CHANGE_COMMAND+" to change an initiative");
            }else if(input.equals(DELETE_COMMAND)){

            }else if(input.equals(CLEAR_COMMAND)){

            }else if(input.equals(VIEW_COMMAND)){

                event.getChannel().sendMessage("view");
                System.out.println(initiatives.size());
                if(initiatives.size()!=0){
                    event.getChannel().sendMessage("Current Initiative Order"+sortInitiatives());
                }

            }

        }


    }

    public int getParam(String in, String command){
        String param = in.substring(command.length());
        return Integer.parseInt(param);
    }

    public String sortInitiatives(){
        Set<String> players = (Set<String>)initiatives.keySet();
        Collection<Integer> values = initiatives.values();
        Integer[] vals = new Integer[values.size()];
        for(int i = 0;i<vals.length;i++){
            //vals[i] = values;
        }

        String out = "";

        for(int value: values){
            for(String player: players){
                if(initiatives.get(player)==value){
                    out =  out + "\n" + player + ": " + value;
                }
            }
        }
        return out;
    }



}
