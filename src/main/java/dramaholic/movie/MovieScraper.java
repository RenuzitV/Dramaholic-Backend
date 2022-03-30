package dramaholic.movie;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

    public void unique(List<Movie> list){
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

    public List<Movie> scrapeMovies(int numMovies, String language){
        List<Movie> movies = new ArrayList<>();
        int pagecount = 1;
        while (movies.size() < numMovies) {
            HashMap<String, String> param = new HashMap<>();
            param.put("api_key", api_key);
            param.put("sort_by", "popularity.desc");
            param.put("vote_average.gte", "5");
            if (language != null) param.put("with_original_language", language);
            param.put("page", String.valueOf(pagecount));

            ResponseEntity<String> response = getResponse("https://api.themoviedb.org/3/discover/tv", param);

            if (checkFailedResponse(response)) return movies;

            JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

            if (jsonObject.get("total_pages").getAsLong() < pagecount) return movies;

            JsonArray jsonArray = jsonObject.get("results").getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                String id = jsonElement.getAsJsonObject().get("id").getAsString();
                Movie movie = makeMovieFromID(id);
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

//https://api.themoviedb.org/3/tv/99966?api_key=2a51e561a490f304053dd6d7c06dbe16&append_to_response=images,videos
    public Movie makeMovieFromID(String id){
        HashMap<String, String> param = new HashMap<>();
        param.put("api_key", api_key);
        param.put("append_to_response", "images,videos");

        ResponseEntity<String> response = getResponse("https://api.themoviedb.org/3/tv/"+id, param);

        if (checkFailedResponse(response)) return null;

        JsonObject json = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

        Movie movie = new Movie();

        movie.setDbID(json.get("id").getAsLong());

        movie.setTitle(json.get("name").getAsString());

        movie.setOriginalTitle(json.get("original_name").getAsString());

        json.get("created_by").getAsJsonArray().forEach(element -> movie.addDirector(element.getAsJsonObject().get("name").getAsString()));

        movie.setDescription(json.get("overview").getAsString());
        if (movie.getDescription().length() > 400) return null;

        movie.setRating(json.get("vote_average").getAsDouble());

        json.get("genres").getAsJsonArray().forEach(element -> movie.addGenres(element.getAsJsonObject().get("name").getAsString()));

        movie.setRating(json.get("vote_average").getAsDouble());

        movie.setDate(LocalDate.parse(json.get("first_air_date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-d")));

        movie.setEpisodes(json.get("number_of_episodes").getAsLong());

        movie.setCountry(json.get("original_language").getAsString());

        movie.setThumbnail("https://image.tmdb.org/t/p/original" + json.get("poster_path").getAsString());

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

        return null;
    }

    @Bean
    public void run() throws Exception{
//        List<Movie> movies = scrapeMovies(50, "ko");
//        movies.addAll(scrapeMovies(50, ""));
//        System.out.println(movies.size());
//        unique(movies);
//        System.out.println(movies.size());
    }
}
