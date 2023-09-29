package fun.kaituo.gameutils.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GameItemStackTag {

    public void setTeamAllAllow(String teamName) {
        canBeClickedByTeams.add(teamName);
        canBeDroppedByTeams.add(teamName);
        canBeInteractedByTeams.add(teamName);
        canBePickedUpByTeams.add(teamName);
    }

    public void removeTeamAllAllow(String teamName) {
        canBeClickedByTeams.remove(teamName);
        canBeDroppedByTeams.remove(teamName);
        canBeInteractedByTeams.remove(teamName);
        canBePickedUpByTeams.remove(teamName);
    }

    @Expose
    @SerializedName("CanBePickedUpByTeams")
    public final List<String> canBePickedUpByTeams = new ArrayList<>();

    @Expose
    @SerializedName("CanBeDroppedByTeams")
    public final List<String> canBeDroppedByTeams = new ArrayList<>();
    @Expose
    @SerializedName("CanBeInteractedByTeams")
    public final List<String> canBeInteractedByTeams = new ArrayList<>();
    @Expose
    @SerializedName("CanBeClickedByTeams")
    public final List<String> canBeClickedByTeams = new ArrayList<>();
}
