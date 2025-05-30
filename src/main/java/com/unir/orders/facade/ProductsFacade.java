package com.unir.orders.facade;

import com.unir.orders.facade.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductsFacade {

  @Value("${getProduct.url}")
  private String getProductUrl;

  private final WebClient.Builder webClient;

  public Product getProduct(String id) {

    try {
      String url = String.format(getProductUrl, id);
      log.info("Getting product with ID {}. Request to {}", id, url);
      return webClient.build()
              .get()
              .uri(url)
              .retrieve()
              .bodyToMono(Product.class)
              .block();
    } catch (HttpClientErrorException e) {
      log.error("Client Error: {}, Product with ID {}", e.getStatusCode(), id);
      return null;
    } catch (HttpServerErrorException e) {
      log.error("Server Error: {}, Product with ID {}", e.getStatusCode(), id);
      return null;
    } catch (Exception e) {
      log.error("Error: {}, Product with ID {}", e.getMessage(), id);
      return null;
    }
  }

}
