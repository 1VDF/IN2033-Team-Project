package boxoffice;

import boxoffice.models.Show;
import java.util.ArrayList;
import java.util.List;

public class ShowManager {
    private List<Show> shows;

    public ShowManager() {
        this.shows = new ArrayList<>();
    }

    public List<Show> getAvailableShows() {
        return shows;
    }
    public Show getShowByName(String showName) {
        for (Show show : shows) {
            if (show.getShowName().equalsIgnoreCase(showName)) {
                return show;
            }
        }
        return null;
    }

    public void addShow(Show show) {
        shows.add(show);
    }
}
