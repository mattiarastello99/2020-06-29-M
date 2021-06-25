package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map<Integer, Director> idMap){
		
		String sql = "SELECT * FROM directors";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				idMap.put(res.getInt("id"), director);
			}
			conn.close();
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public List<Director> getVertexDirector(Map<Integer, Director> idMap, Integer anno){
		
		String sql = "SELECT DISTINCT md.director_id AS id "
				+ "FROM movies_directors md, movies m "
				+ "WHERE md.movie_id = m.id AND m.year=?";
		List<Director> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				
				if(idMap.get(res.getInt("id"))!=null) {
					Director d = idMap.get(res.getInt("id"));
					result.add(d);
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> getArchi(Map<Integer, Director> idMap, Integer anno){
		
		String sql = "SELECT md1.director_id as d1 ,md2.director_id AS d2,COUNT(*) as peso "
				+ "FROM  movies_directors md1, roles r1, roles r2, movies_directors md2, movies m1, movies m2 "
				+ "WHERE md1.movie_id=r1.movie_id AND r2.actor_id=r1.actor_id AND md2.movie_id = r2.movie_id "
				+ "	AND md1.director_id>md2.director_id "
				+ "	AND md1.movie_id=m1.id AND md2.movie_id = m2.id AND m1.year = ? AND m2.year = ? "
				+ "GROUP BY md1.director_id, md2.director_id";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				
				if(idMap.get(res.getInt("d1"))!=null && idMap.get(res.getInt("d2"))!=null) {
					Director d1 = idMap.get(res.getInt("d1"));
					Director d2 = idMap.get(res.getInt("d2"));
					Integer peso = res.getInt("peso");
					
					Arco a = new Arco(d1,d2, peso);
					result.add(a);
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	
	
	
	
	
}
