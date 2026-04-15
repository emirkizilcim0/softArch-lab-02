package tr.edu.mu.se3006.catalog;

// Package-private implementation. Hidden from the outside world.
class CatalogServiceImpl implements CatalogService {
    
    // TODO: Define ProductRepository dependency
    private final ProductRepository productRepository;


    
    // TODO: Implement Constructor Injection
    CatalogServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }    

    @Override
    public void checkAndReduceStock(Long productId, int quantity) {
        // TODO 1: Find product via repository
            Product product = productRepository.findById(productId);
            if (product == null) {
                throw new IllegalArgumentException("Product not found");
            }
        // TODO 2: Check stock (throw IllegalArgumentException if insufficient)
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        // TODO 3: Reduce stock
        product.setStock(product.getStock() - quantity);
        
        // TODO 4: Save updated product
        productRepository.save(product);
    }
}
