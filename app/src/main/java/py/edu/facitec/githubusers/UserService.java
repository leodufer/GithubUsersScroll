package py.edu.facitec.githubusers;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface UserService {
    @GET("/users")
    void getUsers(Callback<List<User>> callback);
    @GET("/users")
    void getUsersPagination(@Query("since") int since, Callback<List<User>> callback);
}
