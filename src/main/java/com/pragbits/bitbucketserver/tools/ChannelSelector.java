package com.pragbits.bitbucketserver.tools;


import java.util.*;

public class ChannelSelector {

    private String globalChannel;
    private String localChannel;
    private String selectedChannel;
    private Hashtable<String, String> patterns = new Hashtable<>();
    private List<String> elems;

    public  ChannelSelector(String globalChannel, String localChannel) {
        this.globalChannel = globalChannel;
        this.localChannel = localChannel;
        this.selectedChannel = "";

        setChannel();
    }

    public String getSelectedChannel() {
        return selectedChannel;
    }

    public Map<String, String> getChannels() {
        return patterns;
    }

    public boolean isEmptyOrSingleValue() {
        return selectedChannel.isEmpty() || !elems.get(0).contains("->");
    }

    private void setChannel() {
        if (!globalChannel.isEmpty()) {
            selectedChannel = globalChannel;
        }
        if (!localChannel.isEmpty()) {
            selectedChannel = localChannel;
        }

        elems = Arrays.asList(selectedChannel.split("\\s*,\\s*"));
        if (elems.get(0).contains("->")) {
            for (String elem: elems) {
                String[] pair = elem.split("\\s*->\\s*");
                patterns.put(pair[0], pair[1]);
            }
        }
    }
}
