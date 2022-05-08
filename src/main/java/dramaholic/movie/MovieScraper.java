package dramaholic.movie;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dramaholic.actor.Actor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//https://api.themoviedb.org/3/discover/tv?api_key=2a51e561a490f304053dd6d7c06dbe16&sort_by=popularity.desc&page=1&vote_average.gte=5&with_original_language=ko
//https://api.themoviedb.org/3/tv/99966?api_key=2a51e561a490f304053dd6d7c06dbe16&append_to_response=images,videos

@Component
public class MovieScraper{
    @Value("${API_KEY}")
    public String api_key;

    private boolean checkFailedResponse(ResponseEntity<String> response){
        int status = response.getStatusCode().value();
        if (status != 200) {
            System.out.println("ERROR: database returned status code " + status);
            return true;
        }
        return false;
    }

    public void uniqueMovie(List<Movie> list){
        Set<Long> s = new HashSet<>();
        Iterator<Movie> i = list.iterator();
        while (i.hasNext()) {
            Movie movie = i.next();
            if (s.contains(movie.getDbID())){
                i.remove();
            }
            else s.add(movie.getDbID());
        }
    }
    public void uniqueActor(List<Actor> list){
        Set<Long> s = new HashSet<>();
        Iterator<Actor> i = list.iterator();
        while (i.hasNext()) {
            Actor actor = i.next();
            if (s.contains(actor.getId())){
                i.remove();
            }
            else s.add(actor.getId());
        }
    }

    public List<Movie> scrapeMovies(int numMovies, String language, String genre){
        List<Movie> movies = new ArrayList<>();
        int pagecount = 1;
        while (movies.size() < numMovies) {
            HashMap<String, String> param = new HashMap<>();
            param.put("api_key", api_key);
            param.put("sort_by", "popularity.desc");
            param.put("vote_average.gte", "5");
            if (language != null) param.put("with_original_language", language);
            if (genre != null) param.put("with_genres", genre);
            param.put("page", String.valueOf(pagecount));

            ResponseEntity<String> response = getResponse("https://api.themoviedb.org/3/discover/tv", param);

            if (checkFailedResponse(response)) return movies;

            JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

            if (jsonObject.get("total_pages").getAsLong() < pagecount) return movies;

            JsonArray jsonArray = jsonObject.get("results").getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                String id = jsonElement.getAsJsonObject().get("id").getAsString();
                Movie movie = null;
                try{
                    movie = makeMovieFromID(id);
                } catch (Exception ignored){

                }
                if (movie == null) continue;
                movies.add(movie);
                System.out.println(movies.get(movies.size() - 1));
                if (movies.size() >= numMovies) break;
            }

            ++pagecount;
        }

        return movies;
    }

    public ResponseEntity<String> getResponse(String url, HashMap<String, String> param){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        param.forEach(builder::queryParam);

        String urlTemplate = builder.encode().toUriString();

        RestTemplate restOperations = new RestTemplate();


        ResponseEntity<String> response = restOperations.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response;
    }

//https://api.themoviedb.org/3/tv/99966?api_key=2a51e561a490f304053dd6d7c06dbe16&append_to_response=images,videos,credits
    public Movie makeMovieFromID(String id){
        HashMap<String, String> param = new HashMap<>();
        param.put("api_key", api_key);
        param.put("append_to_response", "images,videos,credits");

        ResponseEntity<String> response = getResponse("https://api.themoviedb.org/3/tv/"+id, param);

        if (checkFailedResponse(response)) return null;

        JsonObject json = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

        Movie movie = new Movie();

        //ID
        movie.setDbID(json.get("id").getAsLong());

        //TITLE
        movie.setTitle(json.get("name").getAsString());

        //ORIGINAL TITLE
        movie.setOriginalTitle(json.get("original_name").getAsString());

        //DIRECTOR
        json.get("created_by").getAsJsonArray().forEach(element -> movie.addDirector(element.getAsJsonObject().get("name").getAsString()));

        //DESCRIPTION
        movie.setDescription(json.get("overview").getAsString());
        if (movie.getDescription().length() > 400) return null;

        //RATING
        movie.setRating(json.get("vote_average").getAsDouble());

        //GENRES
        json.get("genres").getAsJsonArray().forEach(element -> movie.addGenres(element.getAsJsonObject().get("name").getAsString()));

        //RATING
        movie.setRating(json.get("vote_average").getAsDouble());

        //DATE
        movie.setDate(LocalDate.parse(json.get("first_air_date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-d")));

        //EPISODES, DEPRECATED
        movie.setEpisodes(json.get("number_of_episodes").getAsLong());

        //ORIGINAL LANGUAGE
        movie.setCountry(json.get("original_language").getAsString());

        //THUMBNAIL
        movie.setThumbnail("https://image.tmdb.org/t/p/original" + json.get("poster_path").getAsString());

        //LANDSCAPE THUMBNAIL
        JsonArray thumbnails = json.get("images").getAsJsonObject().get("backdrops").getAsJsonArray();
        for (JsonElement element : thumbnails){
            if (element.getAsJsonObject().get("aspect_ratio").getAsDouble() > 1){
                movie.setThumbnail_landscape("https://image.tmdb.org/t/p/original" + element.getAsJsonObject().get("file_path").getAsString());
                break;
            }
        }

        //ACTORS
        JsonArray actors = json.get("credits").getAsJsonObject().get("cast").getAsJsonArray();
        movie.setActors(new ArrayList<>());
        for (JsonElement element : actors){
            try {
                Actor actor = new Actor(
                        element.getAsJsonObject().get("id").getAsLong(),
                        element.getAsJsonObject().get("name").getAsString(),
                        "https://image.tmdb.org/t/p/original" + element.getAsJsonObject().get("profile_path").getAsString(),
                        element.getAsJsonObject().get("character").getAsString());
                if (actor.getName().length() > 50 || actor.getCharacter().length() > 50) continue;
                movie.addActor(actor);
            } catch (Exception ignored){

            }
            if (movie.getActors().size() >= 5) break;
        }


        //TRAILER/TEASER
        // MUST BE LAST ONE
        JsonArray array = json.get("videos").getAsJsonObject().get("results").getAsJsonArray();
        for (JsonElement element : array){
            if (element.getAsJsonObject().get("site").getAsString().equals("YouTube") && element.getAsJsonObject().get("type").getAsString().equals("Trailer")){
                movie.setHref("https://www.youtube.com/watch?v=" + element.getAsJsonObject().get("key").getAsString());
                break;
            }
        }

        if (movie.getHref() != null) return movie;

        for (JsonElement element : array){
            if (element.getAsJsonObject().get("site").getAsString().equals("YouTube") && element.getAsJsonObject().get("type").getAsString().equals("Teaser")){
                movie.setHref("https://www.youtube.com/watch?v=" + element.getAsJsonObject().get("key").getAsString());
                break;
            }
        }

        if (movie.getHref() != null) return movie;
        //TRAILER/TEASER

        return null;
    }
}
