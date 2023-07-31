package application;
//abstraction
abstract class Product {
	String description;
	float totalRating;
	int totalReviews;
	String review;
    abstract String getDescription();
    abstract void addReview(String review, float rating);
    abstract float getRating();
    abstract String getReview();
}
//inheritace
public class ProductInfo extends Product {
    private String description;
    private float totalRating;
    private int totalReviews;
    private String review;

    ProductInfo(String description) {
        this.description = description;
        this.totalRating = 0;
        this.totalReviews = 0;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void addReview(String review, float rating) {
        totalRating += rating;
        totalReviews++;
        this.review = review;
        float averageRating = totalRating / totalReviews;

        System.out.println("Review(s): " + totalReviews);
        System.out.println("Average rating: " + averageRating + "/5");
        System.out.println();
    }

    public float getRating() {
        if (totalReviews == 0) {
            return 0;
        }
        return totalRating / totalReviews;
    }

    public String getReview() {
        return totalReviews + " review(s)";
    }

    public String getReviewDescription() {
        return review;
    }
}
