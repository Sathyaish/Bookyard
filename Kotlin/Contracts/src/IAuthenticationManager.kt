package bookyard.contracts;

public interface IAuthenticationManager<T> {
    fun authenticateUser(userName: String?,
                         password : String?,
                         appId: String?,
                         appSecret : String?) : OperationResult<T>;
}
