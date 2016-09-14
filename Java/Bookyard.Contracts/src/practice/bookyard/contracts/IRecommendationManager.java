package practice.bookyard.contracts;

public interface IRecommendationManager<T> {
	
	OperationResult<T> GetBooks(String accessToken);
}