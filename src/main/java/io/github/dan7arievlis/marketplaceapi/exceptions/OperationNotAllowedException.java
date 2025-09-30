package io.github.dan7arievlis.marketplaceapi.exceptions;

public class OperationNotAllowedException extends RuntimeException {

  public OperationNotAllowedException(String message) {
    super(message);
  }

}
